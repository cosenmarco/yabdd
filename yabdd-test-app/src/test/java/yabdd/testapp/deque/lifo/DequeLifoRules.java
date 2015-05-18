package yabdd.testapp.deque.lifo;

import yabdd.Context;
import yabdd.annotations.Then;
import yabdd.annotations.When;

import java.util.Deque;

import static org.junit.Assert.assertEquals;

/**
 * Rules to test a Deque as a FIFO structure
 * Created by Marco Cosentino on 15/05/15.
 */
public class DequeLifoRules {
    private Context context;

    public DequeLifoRules(Context context) {
        this.context = context;
    }

    @When("I push value (.*)")
    public void whenIPushValue() {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        testDeque.push(context.getRuleCapture(0));
    }

    @Then("I pop value (.*)")
    public void thenIPopValue() {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        String poppedValue = testDeque.pop();
        assertEquals(context.getRuleCapture(0), poppedValue);
    }

}
