package yabdd.feature.parsing;

import lombok.Getter;
import yabdd.feature.*;
import yabdd.feature.Package;
import yabdd.gherkin.GherkinBaseListener;
import yabdd.gherkin.GherkinParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Facade to the Parsing functionality
 * Created by Marco Cosentino on 11/03/15.
 */
public class Parser {
    private class GherkinListener extends GherkinBaseListener {
        @Getter
        private Feature feature;

        private final Package packg;
        private List<Tag> featureTags = new ArrayList<Tag>();
        private List<Scenario> scenarios = new ArrayList<Scenario>();
        private List<Given> backgroundGivens;
        private List<Tag> backgroundTags = new ArrayList<Tag>();

        private List<Tag> currentTags;
        private List<Given> currentGivens;
        private List<When> currentWhens;
        private List<Then> currentThens;

        public GherkinListener(Package packg) {
            this.packg = packg;
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
            backgroundTags = currentTags;
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
            scenarios.add(new Scenario(currentTags, title, buildDescription(ctx.blockDesc()),
                    currentGivens, currentWhens, currentThens));
        }

        @Override
        public void exitOutlineScenario(GherkinParser.OutlineScenarioContext ctx) {
            String title = ctx.restOfLine().getText().trim();
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
            this.feature = new Feature(featureTags, title, description.toString().trim(), scenarios, packg);
        }
    }

    public static Feature parse(File featureFile) {
        return null;
    }
}
