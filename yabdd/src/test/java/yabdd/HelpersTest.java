package yabdd;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;

import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
* Test for the Helpers
* Created by Marco Cosentino on 14/03/15.
*/
public class HelpersTest {

    @Test
    public void findFeatureFilesTest() {
        Reflections reflections = new Reflections(
                ClasspathHelper.forClass(getClass()),
                new ResourcesScanner());
        Set<String> result = Helpers.findFeatureFiles(reflections);

        assertTrue(result.contains("yabdd/findme.feature"));
        assertTrue(result.contains("yabdd/gherkin/tagged.feature"));
        assertTrue(result.contains("yabdd/feature/parsing/test.feature"));
        assertTrue(result.contains("yabdd/gherkin/simplest.feature"));
        assertTrue(result.contains("yabdd/gherkin/simple.feature"));
    }
}
