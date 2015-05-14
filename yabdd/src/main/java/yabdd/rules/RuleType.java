package yabdd.rules;

import com.google.common.base.CaseFormat;
import yabdd.annotations.Given;
import yabdd.annotations.Then;
import yabdd.annotations.When;

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

    Class<?> getAnnotationClass() {
        return klass;
    }

    public String getTypeString() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name());
    }
}
