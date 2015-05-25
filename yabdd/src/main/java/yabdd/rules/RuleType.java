package yabdd.rules;

import com.google.common.base.CaseFormat;
import yabdd.annotations.Given;
import yabdd.annotations.Then;
import yabdd.annotations.When;

import java.lang.annotation.Annotation;

/**
 * Created by Marco Cosentino on 11/05/15.
 */
public enum RuleType {
    GIVEN(Given.class),
    WHEN(When.class),
    THEN(Then.class);

    private final Class<?> klass;
    private RuleType(Class<?> klass) {
        this.klass = klass;
    }

    public Class<?> getAnnotationClass() {
        return klass;
    }

    public static RuleType byAnnotation(Class<? extends Annotation> ann) {
        if (ann.equals(Given.class)) {
            return RuleType.GIVEN;
        } else if (ann.equals(When.class)) {
            return RuleType.WHEN;
        } else if (ann.equals(Then.class)) {
            return RuleType.THEN;
        } else {
            throw new IllegalArgumentException("Unable to find a RuleType for annotation " + ann.getName());
        }
    }

    public String getTypeString() {
        switch(this) {
            case GIVEN:
                return "Given";
            case WHEN:
                return "When";
            case THEN:
                return "Then";
            default:
                return "UNKNOWN";
        }
    }
}
