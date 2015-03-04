# Why

I think BDD is a great thing which should be very widely adopted across organizations for two reasons:
1. It makes the behaviour of the software readable and verifiable by non-technical (or non language X aware) people
2. It helps test developers to write clean, concise and scoped tests

In JVM environment there are some frameworks to write BDD tests and execute them.
While being all of them great pieces of software I personally find no framework satisfying my requirements. TODO: Explain why.

## Features
- Features language is Gherkin
- Simple: no or minimal configuration. Convention over configuration (with conventions that hopefully make sense).
- Automatically fits in Maven projects
- Integrated with jUnit and testNG
- Uses Java annotations to mark rule methods and to define what they match in the features (pretty much like Cucumber and jBehave)
- Each feature file belongs to a Java package. The rules are matched according to the packages structure.
- The feature files are crawled starting from location "src/test/features/". Packages are established via directory separation or
dot character (eg. "src/test/features/com.mycompany/project.subproject/package/Awesome.feature".
- Mocking things is made easy within a scenario through the use of "contexts" to enable true rules re-usability


## Reporting
- Logging of the framework activity through slf4j
- Test results through jUnit with a Test Case granularity
- TODO

# How

## Concepts
- A test suite is generated for each Scenario. A test case is generated for each use case.
- Each class containing Rules must accept the injection of a Context instance which is reset on every Scenario execution.
- The Context object is shared across all the Rule's classes independently of the Rule's package.

