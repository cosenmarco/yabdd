package yabdd.rules.standard;

import yabdd.Context;
import yabdd.annotations.Then;

import static org.junit.Assert.*;

/**
 * A container for all the standard rules (of general use) made available in the root package
 * Created by Marco Cosentino on 20/05/15.
 */
public class StandardRules {
    public static final String EXCEPTION = "exception";

    private final Context context;

    public StandardRules(Context context) {
        this.context = context;
    }

    @Then("an exception is thrown")
    public void thenAnExceptionIsThrown() {
        assertNotNull(context.get(EXCEPTION));
    }

    @Then("(.*) is thrown")
    public void thenSomeSpecificExceptionIsThrown(String exceptionClassName) throws ClassNotFoundException {
        Class<?> exceptionType = Class.forName(exceptionClassName);
        Object exception = context.get(EXCEPTION);
        assertNotNull(exception);
        assertTrue(exceptionType.isInstance(exception));
    }
}
