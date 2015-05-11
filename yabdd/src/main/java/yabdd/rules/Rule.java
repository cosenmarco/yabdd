package yabdd.rules;

import lombok.ToString;
import yabdd.annotations.Given;
import yabdd.annotations.When;
import yabdd.annotations.Then;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Represents a "rule method" in a form we can work with
 * Created by Marco Cosentino on 03/04/15.
 */
@ToString
public class Rule {
    private final RuleType type;
    private final Method theMethod;
    private final RulePackage rulePackage;
    private final String value;

    public Rule(RuleType type, Method theMethod) {
        this.type = type;
        this.theMethod = theMethod;

        Class<?> klass = theMethod.getDeclaringClass();
        rulePackage = new RulePackage(klass.getPackage().getName());

        switch(type) {
            case GIVEN:
                Given given = theMethod.getAnnotation(Given.class);
                this.value = given.value();
                break;
            case WHEN:
                When when = theMethod.getAnnotation(When.class);
                this.value = when.value();
                break;
            case THEN:
                Then then = theMethod.getAnnotation(Then.class);
                this.value = then.value();
                break;
            default:
                this.value = "";
        }
    }

    public RulePackage getPackage() {
        return null;
    }
}
