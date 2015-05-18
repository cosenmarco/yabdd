Feature: Deque in FIFO mode
  A linear collection that supports element insertion and removal at both ends.
  The name deque is short for "double ended queue" and is usually pronounced "deck".
  Most Deque implementations place no fixed limits on the number of elements they may contain, but
  this interface supports capacity-restricted deques as well as those with no fixed size limit.

  # Note: text above is copied from javadocs for Deque

  Scenario: Can work like a Queue
    Given an empty Deque
    When I add value A
    And I add value B
    And I add value C
    Then I remove value A
    And I remove value B
    And I remove value C
    And the deque is empty

  Scenario: Exception when removing from empty Deque
    Given an empty Deque
    When I remove a value
    Then java.util.NoSuchElementException is thrown

  Scenario: Can use poll without Exception on an empty deque
    Given an empty Deque
    Then I poll null

  Scenario: Peek functionality
    Given an empty Deque
