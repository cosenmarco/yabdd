package yabdd.testapp.deque.fifo;

import yabdd.Context;
import yabdd.annotations.Then;
import yabdd.annotations.When;

import java.util.Deque;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Rules to test a Deque as a FIFO structure
 * Created by Marco Cosentino on 15/05/15.
 */
public class DequeFifoRules {
    private Context context;

    public DequeFifoRules(Context context) {
        this.context = context;
    }

    @When("I add value (.*)")
    public void whenIPushValue() {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        testDeque.add(context.getRuleCapture(0));
    }

    @Then("I remove value (.*)")
    public void thenIPopValue() {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        String removedValue = testDeque.remove();
        assertEquals(context.getRuleCapture(0), removedValue);
    }

    @When("I remove a value")
    public void whenIRemoveaValue() {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        try {
            String removedValue = testDeque.remove();
        } catch(Exception e) {
            context.put("exception", e);
        }
    }

    @Then("I poll null")
    public void thenIPollNull() {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        Object polledObject = testDeque.poll();
        assertNull(polledObject);
    }
}
