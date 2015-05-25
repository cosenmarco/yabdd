package yabdd.testapp.deque;

import org.junit.runner.RunWith;
import yabdd.Context;
import yabdd.annotations.Given;
import yabdd.annotations.Then;
import yabdd.junit.YabddJUnitRunner;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
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

    @Given("a Deque with elements (.*)")
    public void givenADequeWithElements(List<String> elements) {
        Deque<String> testDeque = new ArrayDeque<String>();
        for(String element : elements) {
            testDeque.add(element);
        }
        context.put("deque", testDeque);
    }

}