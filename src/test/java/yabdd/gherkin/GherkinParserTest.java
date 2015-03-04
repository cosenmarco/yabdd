package yabdd.gherkin;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.Tree;
import org.junit.Test;
import yabdd.gherkin.GherkinListener;
import yabdd.gherkin.GherkinParser;
import yabdd.gherkin.GherkinLexer;
import yabdd.gherkin.GherkinBaseListener;

import static org.junit.Assert.assertEquals;

/**
 * Tests the Gherkin parser
 * Created by Marco Cosentino on 01/03/15.
 */
public class GherkinParserTest {
    private class SimplestListener extends GherkinBaseListener {

        @Override
        public void enterFeature(GherkinParser.FeatureContext ctx) {
            assertEquals("valve feature", ctx.featHeader().restOfLine().getText().trim());
        }

        @Override
        public void enterScenario(GherkinParser.ScenarioContext ctx) {
            assertEquals("test scenario", ctx.restOfLine().getText().trim());
            assertEquals("I have a rule", ctx.blockBody().given().firstGiven().ruleBody().getText().trim());
            assertEquals("the rule is parsed", ctx.blockBody().when().firstWhen().ruleBody().getText().trim());
            assertEquals("I am happy", ctx.blockBody().then().firstThen().ruleBody().getText().trim());
        }
    }

    @Test
    public void testCorrectParsingSimplest() throws Exception {
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(getClass().getResourceAsStream("simplest.feature"));
        GherkinLexer lexer = new GherkinLexer(antlrInputStream);
        GherkinParser parser = new GherkinParser(new CommonTokenStream(lexer));
        Tree tree = parser.feature();

        GherkinListener testListener = new SimplestListener();
    }

    private class SimpleListener extends GherkinBaseListener {
        private int scenario = 0;

        @Override
        public void enterFeature(GherkinParser.FeatureContext ctx) {
            assertEquals("this is a test feature", ctx.featHeader().restOfLine().getText().trim());
        }

        @Override
        public void enterScenario(GherkinParser.ScenarioContext ctx) {
            if(scenario == 0) {
                scenario ++;
                assertEquals("test scenario", ctx.restOfLine().getText().trim());
                assertEquals("I have a rule", ctx.blockBody().given().firstGiven().ruleBody().getText().trim());
                assertEquals("the rule is parsed", ctx.blockBody().when().firstWhen().ruleBody().getText().trim());
                assertEquals("I am happy", ctx.blockBody().then().firstThen().ruleBody().getText().trim());
            } else {
                assertEquals("test2", ctx.restOfLine().getText().trim());
                assertEquals("the valve is closed", ctx.blockBody().given().firstGiven().ruleBody().getText().trim());
                assertEquals("the indicator is off", ctx.blockBody().given().moreGiven(0).ruleBody().getText().trim());
                assertEquals("I press the switch", ctx.blockBody().when().firstWhen().ruleBody().getText().trim());
                assertEquals("the valve is open", ctx.blockBody().then().firstThen().ruleBody().getText().trim());
                assertEquals("the indicator is on", ctx.blockBody().then().moreThen(0).ruleBody().getText().trim());
            }
        }
    }

    @Test
    public void testCorrectParsingSimple() throws Exception {
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(getClass().getResourceAsStream("simple.feature"));
        GherkinLexer lexer = new GherkinLexer(antlrInputStream);
        GherkinParser parser = new GherkinParser(new CommonTokenStream(lexer));
        Tree tree = parser.feature();

        GherkinListener testListener = new SimpleListener();
    }
}
