# A test feature file to test the correctness of the parser

  @FeatTag
  Feature: Feature Title

    feat description goes here
      and continues here.

  @TagBackground Background: can have a title
    and a description
    Given I'm executed every time
    And I'm as well

    @TagScenario
    Scenario: scenario
      description for a scenario

      Given given
      And given2
      But given 3 (which is nice)
      When when
      Then then
      And then2


    Scenario: other scenario
      When when
      Then then
