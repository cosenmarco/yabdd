Feature: Deque in LIFO mode
  A linear collection that supports element insertion and removal at both ends.
  The name deque is short for "double ended queue" and is usually pronounced "deck".
  Most Deque implementations place no fixed limits on the number of elements they may contain, but
  this interface supports capacity-restricted deques as well as those with no fixed size limit.

  # Note: text above is copied from javadocs for Deque

  Scenario: Can work like a Stack
    Given an empty Deque
    When I push value A
    And I push value B
    And I push value C
    Then I pop value C
    And I pop value B
    And I pop value A
    And the deque is empty