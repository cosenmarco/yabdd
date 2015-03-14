package yabdd.feature.parsing;

import org.junit.Test;
import yabdd.feature.Feature;
import yabdd.feature.Scenario;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the feature Parser
 * Created by Marco Cosentino on 13/03/15.
 */
public class ParserTest {
    @Test
    public void parserTest () {
        Feature resultFeature = Parser.parse(getClass().getResourceAsStream("test.feature"), "test.feature");

        assertEquals("FeatTag", resultFeature.getTags().get(0).getTagText());
        assertEquals("Feature Title", resultFeature.getTitle());
        assertEquals("feat description goes here and continues here.", resultFeature.getDescription());

        Scenario resultScenario = resultFeature.getScenarios().get(0);
        assertEquals("scenario", resultScenario.getTitle());
        assertEquals("description for a scenario", resultScenario.getDescription());

        assertEquals(2, resultScenario.getTags().size());
        assertEquals("TagBackground", resultScenario.getTags().get(0).getTagText());
        assertEquals("TagScenario", resultScenario.getTags().get(1).getTagText());

        assertEquals(5, resultScenario.getGivens().size());
        assertEquals("I'm executed every time", resultScenario.getGivens().get(0).getBody());
        assertEquals("I'm as well", resultScenario.getGivens().get(1).getBody());
        assertEquals("given", resultScenario.getGivens().get(2).getBody());
        assertEquals("given2", resultScenario.getGivens().get(3).getBody());
        assertEquals("given 3 (which is nice)", resultScenario.getGivens().get(4).getBody());

        assertEquals(1, resultScenario.getWhens().size());
        assertEquals("when", resultScenario.getWhens().get(0).getBody());

        assertEquals(2, resultScenario.getThens().size());
        assertEquals("then", resultScenario.getThens().get(0).getBody());
        assertEquals("then2", resultScenario.getThens().get(1).getBody());

        Scenario resultScenario2 = resultFeature.getScenarios().get(1);
        assertEquals("other scenario", resultScenario2.getTitle());
        assertEquals("", resultScenario2.getDescription());

        assertEquals(1, resultScenario2.getTags().size());
        assertEquals("TagBackground", resultScenario2.getTags().get(0).getTagText());

        assertEquals(2, resultScenario2.getGivens().size());
        assertEquals("I'm executed every time", resultScenario2.getGivens().get(0).getBody());
        assertEquals("I'm as well", resultScenario2.getGivens().get(1).getBody());

        assertEquals(1, resultScenario2.getWhens().size());
        assertEquals("when", resultScenario2.getWhens().get(0).getBody());

        assertEquals(1, resultScenario2.getThens().size());
        assertEquals("then", resultScenario2.getThens().get(0).getBody());
    }
}
