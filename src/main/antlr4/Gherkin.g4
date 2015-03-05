// TODO Copyright note
// https://github.com/cucumber/cucumber/wiki/Gherkin
// https://github.com/cucumber/yabdd.gherkin/wiki/BNF

grammar Gherkin;

@header {
package yabdd.gherkin;
}

feature: featHeader featBody;

featHeader: Space* Feature restOfLine NewLine+ featDesc*;
featDesc: ~(Background | Scenario | ScenarioOutline) restOfLine* NewLine+ ;

featBody: background? (scenario | outlineScenario)+;

background: Space* Background restOfLine NewLine+ blockDesc blockBody;
blockDesc: (~Given)*;
blockBody: given when then;

scenario: Space* Scenario restOfLine NewLine+  blockDesc blockBody;
outlineScenario: Space* ScenarioOutline blockDesc blockBody;

given: firstGiven moreGiven*;
firstGiven: Space* Given ruleBody;
moreGiven: Space* (And | But | Given) ruleBody;

when: firstWhen moreWhen*;
firstWhen: Space* When ruleBody;
moreWhen: Space* (And | But | When) ruleBody;

then: firstThen moreThen*;
firstThen: Space* Then ruleBody;
moreThen: Space* (And | But | Then) ruleBody;

ruleBody: ruleText (NewLine | EOF);
ruleText: restOfLine;

restOfLine: Space* Word (Word|Space)*;


//tag:  '@' TAGNAME WS+;
//TAGNAME: ~(WS|'@')+;

Comment: Space* '#' .*? NewLine -> skip;

Space : [ \t];
NewLine : '\r'? '\n' | '\r';
Word: ~[ \t\r\n]+?;

And: 'And ';
But: 'But ';
Given: 'Given ';
When: 'When ';
Then: 'Then ';
Background: 'Background:';
Scenario: 'Scenario:';
ScenarioOutline: 'Scenario Outline:';
Feature: 'Feature:';