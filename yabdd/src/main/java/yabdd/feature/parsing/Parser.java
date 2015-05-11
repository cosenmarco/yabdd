package yabdd.feature.parsing;

import lombok.Getter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yabdd.feature.*;
import yabdd.gherkin.GherkinBaseListener;
import yabdd.gherkin.GherkinLexer;
import yabdd.gherkin.GherkinListener;
import yabdd.gherkin.GherkinParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Facade to the Parsing functionality
 * Created by Marco Cosentino on 11/03/15.
 */
public class Parser {
    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

    private static class FeatureParserListener extends GherkinBaseListener {
        @Getter
        private Feature feature;

        private final String resourcePath;
        private List<Tag> featureTags = new ArrayList<Tag>();
        private List<Scenario> scenarios = new ArrayList<Scenario>();
        private List<Given> backgroundGivens = Collections.emptyList();
        private List<Tag> backgroundTags = new ArrayList<Tag>();

        private List<Tag> currentTags;
        private List<Given> currentGivens;
        private List<When> currentWhens;
        private List<Then> currentThens;

        public FeatureParserListener(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        private void resetScenario() {
            currentTags = new ArrayList<Tag>();
            currentGivens = new ArrayList<Given>();
            currentWhens = new ArrayList<When>();
            currentThens = new ArrayList<Then>();
        }

        @Override
        public void enterBackground(GherkinParser.BackgroundContext ctx) {
            resetScenario();
        }

        @Override
        public void exitBackground(GherkinParser.BackgroundContext ctx) {
            for(TerminalNode tag : ctx.Tag()) {
                backgroundTags.add(new Tag(tag.getText().trim()));
            }
            backgroundGivens = currentGivens;
        }

        @Override
        public void exitGiven(GherkinParser.GivenContext ctx) {
            String ruleText = ctx.firstGiven().ruleBody().ruleText().getText().trim();
            currentGivens.add(new Given(ruleText));
            for(GherkinParser.MoreGivenContext moregivenCtx : ctx.moreGiven()) {
                ruleText = moregivenCtx.ruleBody().ruleText().getText().trim();
                currentGivens.add(new Given(ruleText));
            }
        }

        @Override
        public void exitWhen(GherkinParser.WhenContext ctx) {
            String ruleText = ctx.firstWhen().ruleBody().ruleText().getText().trim();
            currentWhens.add(new When(ruleText));
            for(GherkinParser.MoreWhenContext moreWhenCtx : ctx.moreWhen()) {
                ruleText = moreWhenCtx.ruleBody().ruleText().getText().trim();
                currentWhens.add(new When(ruleText));
            }
        }

        @Override
        public void exitThen(GherkinParser.ThenContext ctx) {
            String ruleText = ctx.firstThen().ruleBody().ruleText().getText().trim();
            currentThens.add(new Then(ruleText));
            for(GherkinParser.MoreThenContext moreThenCtx : ctx.moreThen()) {
                ruleText = moreThenCtx.ruleBody().ruleText().getText().trim();
                currentThens.add(new Then(ruleText));
            }
        }

        @Override
        public void enterScenario(GherkinParser.ScenarioContext ctx) {
            resetScenario();
            currentGivens.addAll(backgroundGivens);
            currentTags.addAll(backgroundTags);
        }

        @Override
        public void enterOutlineScenario(GherkinParser.OutlineScenarioContext ctx) {
            resetScenario();
            currentGivens.addAll(backgroundGivens);
            currentTags.addAll(backgroundTags);
        }

        @Override
        public void exitScenario(GherkinParser.ScenarioContext ctx) {
            String title = ctx.restOfLine().getText().trim();
            for(TerminalNode tag : ctx.Tag()) {
                currentTags.add(new Tag(tag.getText().trim()));
            }
            scenarios.add(new Scenario(currentTags, title, buildDescription(ctx.blockDesc()),
                    currentGivens, currentWhens, currentThens));
        }

        @Override
        public void exitOutlineScenario(GherkinParser.OutlineScenarioContext ctx) {
            String title = ctx.restOfLine().getText().trim();
            for(TerminalNode tag : ctx.Tag()) {
                currentTags.add(new Tag(tag.getText().trim()));
            }
            scenarios.add(new Scenario(currentTags, title, buildDescription(ctx.blockDesc()),
                    currentGivens, currentWhens, currentThens));
        }

        private String buildDescription(List<GherkinParser.BlockDescContext> ctxs) {
            StringBuilder description = new StringBuilder();
            for(GherkinParser.BlockDescContext blockDescCtx : ctxs) {
                description.append(blockDescCtx.restOfLine().getText().trim()).append(" ");
            }
            return description.toString().trim();
        }

        @Override
        public void exitFeature(GherkinParser.FeatureContext ctx) {
            String title;
            StringBuilder description = new StringBuilder();

            title = ctx.featHeader().restOfLine().getText().trim();
            for(GherkinParser.FeatDescContext featDescCtx : ctx.featHeader().featDesc()) {
                description.append(featDescCtx.restOfLine().getText().trim()).append(" ");
            }

            for(TerminalNode tag : ctx.featHeader().Tag()) {
                featureTags.add(new Tag(tag.getText().trim()));
            }
            this.feature = new Feature(featureTags, title, description.toString().trim(), scenarios, resourcePath);
        }


    }

    public static Feature parse(InputStream featureStream, String resourcePath) {
        ANTLRInputStream antlrInputStream;
        try {
            antlrInputStream = new ANTLRInputStream(new InputStreamReader(featureStream));
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
        GherkinLexer lexer = new GherkinLexer(antlrInputStream);
        GherkinParser parser = new GherkinParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.feature();

        FeatureParserListener listener = new FeatureParserListener(resourcePath);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);

        Feature result = listener.getFeature();
        LOG.debug("Parsed feature {}", result);
        return result;
    }
}
