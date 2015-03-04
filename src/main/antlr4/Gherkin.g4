// TODO Copyright note
// https://github.com/cucumber/cucumber/wiki/Gherkin
// https://github.com/cucumber/yabdd.gherkin/wiki/BNF

grammar Gherkin;

@header {
package yabdd.gherkin;
}

feature: featHeader featBody;

featHeader: SP* FEATURE restOfLine NL+ featDesc*;
//featTitle: restOfLine;
featDesc: ~(BACKGROUND | SCENARIO | OUTLINE_SCENARIO) restOfLine* NL+ ;

featBody: background? (scenario | outlineScenario)+;

background: SP* BACKGROUND restOfLine NL+ blockDesc blockBody;
blockDesc: (~GIVEN)*;
blockBody: given when then;

scenario: SP* SCENARIO restOfLine NL+  blockDesc blockBody;
outlineScenario: SP* OUTLINE_SCENARIO blockDesc blockBody;

given: firstGiven moreGiven*;
firstGiven: SP* GIVEN ruleBody;
moreGiven: SP* (AND | BUT | GIVEN) ruleBody;

when: firstWhen moreWhen*;
firstWhen: SP* WHEN ruleBody;
moreWhen: SP* (AND | BUT | WHEN) ruleBody;

then: firstThen moreThen*;
firstThen: SP* THEN ruleBody;
moreThen: SP* (AND | BUT | THEN) ruleBody;

ruleBody: ruleText (NL | EOF);
ruleText: restOfLine;

restOfLine: (WORD|SP)+;


//tag:  '@' TAGNAME WS+;
//TAGNAME: ~(WS|'@')+;

Comment: SP* '#' .*? NL;

SP : [ \t];
NL : '\r'? '\n' | '\r';
WORD: ~[ \t\r\n]+?;
fragment White : SP | NL;

AND: 'And ';
BUT: 'But ';
GIVEN: 'Given ';
WHEN: 'When ';
THEN: 'Then ';
BACKGROUND: 'Background:';
SCENARIO: 'Scenario:';
OUTLINE_SCENARIO: 'Scenario Outline:';
FEATURE: 'Feature:';