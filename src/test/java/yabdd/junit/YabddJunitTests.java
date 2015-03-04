package yabdd.junit;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Created by technics on 28/02/15.
 */
@RunWith(YabddJUnitRunner.class)
public class YabddJunitTests {
    @Test
    public void truthTest() {
        assertTrue(true);
    }
}
