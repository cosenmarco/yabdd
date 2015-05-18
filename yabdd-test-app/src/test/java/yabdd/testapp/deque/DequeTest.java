package yabdd.testapp.deque;

import org.junit.runner.RunWith;
import yabdd.Context;
import yabdd.annotations.Given;
import yabdd.annotations.Then;
import yabdd.junit.YabddJUnitRunner;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(YabddJUnitRunner.class)
public class DequeTest {
    private Context context;

    public DequeTest(Context context) {
        this.context = context;
    }

    @Given("an empty Deque")
    public void givenAnEmptyDeque() {
        context.put("deque", new ArrayDeque<String>());
    }

    @Then("the deque is empty")
    public void theDequeIsEmpty() {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        assertTrue(testDeque.isEmpty());
    }
}