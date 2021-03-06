Note: UNMAINTAINED. Cucmber for the JVM has evelved and it's my go-to choice for BDD on the JVM

#YABDD

stands for Yet Another Behaviour Driven Development (Java) framework.

## Intro
in src/test/resources/dequetest/fifo/DequeFifo.feature:
```gherkin
Feature: Deque in FIFO mode

  Scenario: Can work like a Queue
    Given an empty Deque
    When I add value A
    And I add value B
    And I add value C
    Then I remove value A
    And I remove value B
    And I remove value C
    And the deque is empty
```

in src/test/java/dequetest/DequeTest.java:
```java
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
```

in src/test/java/dequetest/fifo/DequeFifoRules.java:
```java
public class DequeFifoRules {
    private Context context;

    public DequeFifoRules(Context context) {
        this.context = context;
    }

    @When("I add value (.*)")
    public void whenIPushValue(String value) {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        testDeque.add(value);
    }

    @Then("I remove value (.*)")
    public void thenIPopValue(String value) {
        Deque<String> testDeque = (Deque<String>) context.get("deque");
        String removedValue = testDeque.remove();
        assertEquals(value, removedValue);
    }
}
```

# Why

I think BDD is a great thing which should be very widely adopted across organizations for two reasons:

1. It makes the behaviour of the software readable and verifiable by non-technical (or non language X aware) people
2. It helps test developers to write clean, concise and focused tests by abstracting the API under test and by providing a standard structure for the test.

In JVM environment there are some frameworks to write BDD tests and execute them.
While being all of them great pieces of software I personally find no framework satisfying my requirements. TODO: Explain why.

## Features
- Features language is Gherkin
- Simple: no or minimal configuration. Convention over configuration (with conventions that hopefully make sense).
- Automatically fits in Maven projects
- Fully integrated with jUnit
- Uses Java annotations to mark rule methods and to define what they match in the features (pretty much like Cucumber and jBehave)
- Each feature file belongs to a Java package. The rules are matched according to the packages structure.
- The feature files are crawled within the classpath. Feature's package is inferred by the resource's path (eg. "com/mycompany/project/subproject/package/Awesome.feature" belongs to package com.mycompany.project.subproject.package).
- Rule classes can accept Context objects at construction. This enables rules to communicate with each other and to extract information about the context in which they are running supporting easy reusability of rules.
- Upon execution rules are injected the captures from the matching process accordingly to the rule's method signature.
- Provides some pre-implemented rules that belong to package "/" that aim to be very generic and re-usable in several different occasions


## Reporting
- Logging of the framework activity through slf4j
- Test results through jUnit with a Scenario granularity

# How

## Concepts
- A jUnit test suite is generated for each Scenario.
- Each rule class can accept the injection of a Context instance which is created on every Scenario execution.
- The Context object contains facilities for storing objects within the Context which can be used by subsequent rules within the execution of a Scenario.

