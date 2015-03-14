


@Featuretag Feature: this is a test feature
  feat description
  @Tag01_test @Othertagtest
  Scenario: test scenario
  scenario description


   #  Comment

    Given I have a rule

    When the rule is parsed

    Then I am happy



  Scenario: test2
    Given the valve is closed
    And the indicator is off


    When I press the switch
    Then the valve is open

    And the indicator is on