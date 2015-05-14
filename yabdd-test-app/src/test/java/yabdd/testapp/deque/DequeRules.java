package yabdd.testapp.deque;

import yabdd.Context;
import yabdd.annotations.Given;
import yabdd.annotations.Then;
import yabdd.annotations.When;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.Assert.*;

public class DequeRules {
    @Given("an empty Deque")
    public static void givenAnEmptyDeque(Context context) {
        context.put("deque", new ArrayDeque<String>());
    }

    @When("I push value (.*)")
    public static void whenIPushValue(Context context) {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        testDeque.push(context.getRuleCapture(0));
    }

    @Then("I pop value (.*)")
    public static void thenIPopValue(Context context) {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        String poppedValue = testDeque.pop();
        assertEquals(context.getRuleCapture(0), poppedValue);
    }

    @Then("the deque is empty")
    public static void theDequeIsEmpty(Context context) {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        assertTrue(testDeque.isEmpty());
    }
}