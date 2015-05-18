package yabdd.testapp;

import yabdd.Context;
import yabdd.annotations.Then;

import static org.junit.Assert.*;

public class GeneralRules {
    private Context context;

    public GeneralRules(Context context) {
        this.context = context;
    }

    @Then("(.*) is thrown")
    public void someExceptionIsThrown() throws ClassNotFoundException {
        Class<?> exceptionType = Class.forName(context.getRuleCapture(0));
        Object exception = context.get("exception");
        assertNotNull(exception);
        assertTrue(exceptionType.isInstance(exception));
    }
}