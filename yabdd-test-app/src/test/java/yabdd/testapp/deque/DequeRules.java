package yabdd.testapp.deque;

import yabdd.annotations.Given;
import yabdd.annotations.Then;
import yabdd.annotations.When;

public class DequeRules {
    @Given("an empty Deque")
    public void givenAnEmptyDeque() {
        System.out.println("GIVENNNNNNNNNNNNNNNN");
    }

    @When("I push value (.*)")
    public void whenIPushValue() {
        System.out.println("WHEN");
    }

    @Then("I pop value (.*)")
    public void thenIPopValue() {
        System.out.println("THEN");
    }
}