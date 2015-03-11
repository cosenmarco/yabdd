// TODO Copyright note
// https://github.com/cucumber/cucumber/wiki/Gherkin
// https://github.com/cucumber/gherkin/wiki/BNF

grammar Gherkin;

@header {
package yabdd.gherkin;
}

feature: featHeader featBody;

featHeader: (Space | NewLine)* Tag* Feature restOfLine NewLine+ featDesc*;
featDesc: ~(Background | Scenario | ScenarioOutline) restOfLine NewLine+ ;

featBody: background? (scenario | outlineScenario)+;

background: (Space | NewLine)* Tag* Background restOfLine NewLine+ blockDesc* given;
blockDesc: ~(Given) restOfLine NewLine+ ;
blockBody: given when then;

scenario: (Space | NewLine)* Tag* Scenario restOfLine NewLine+  blockDesc* blockBody;
outlineScenario: (Space | NewLine)* Tag* ScenarioOutline restOfLine NewLine+ blockDesc* blockBody;

given: firstGiven moreGiven*;
firstGiven: (Space | NewLine)* Given ruleBody;
moreGiven: (Space | NewLine)* (And | But | Given) ruleBody;

when: firstWhen moreWhen*;
firstWhen: (Space | NewLine)* When ruleBody;
moreWhen: (Space | NewLine)* (And | But | When) ruleBody;

then: firstThen moreThen*;
firstThen: (Space | NewLine)* Then ruleBody;
moreThen: (Space | NewLine)* (And | But | Then) ruleBody;

ruleBody: ruleText (NewLine | EOF);
ruleText: restOfLine;

restOfLine: Space* Word (Word|Space)*;

// Tokens
Tag:  '@' WD (Space | NewLine)+;
Comment: Space* '#' .*? NewLine -> skip;

And: 'And ';
But: 'But ';
Given: 'Given ';
When: 'When ';
Then: 'Then ';
Background: 'Background:';
Scenario: 'Scenario:';
ScenarioOutline: 'Scenario Outline:';
Feature: 'Feature:';

Space : [ \t];
NewLine : '\r'? '\n' | '\r';
Word: WD;

fragment WD: ~[ \t\r\n]+?;
