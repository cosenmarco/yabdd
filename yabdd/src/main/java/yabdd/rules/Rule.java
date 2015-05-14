package yabdd.rules;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.ToString;
import yabdd.Context;
import yabdd.annotations.Given;
import yabdd.annotations.When;
import yabdd.annotations.Then;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a "rule method" in a form we can work with
 * Created by Marco Cosentino on 03/04/15.
 */
@ToString
public class Rule {
    @Getter
    private final RuleType type;

    @Getter
    private final Method theMethod;

    @Getter
    private final RulePackage rulePackage;

    @Getter
    private final String value;

    public Rule(RuleType type, Method theMethod) {
        this.type = type;
        this.theMethod = theMethod;

        // Sanity checks
        if( ! Modifier.isStatic(theMethod.getModifiers()) ) {
            throw new IllegalArgumentException("The annotated method " + getFullMethodName(theMethod) +
                    " should be static");
        }
        Type[] parameterTypes =  theMethod.getGenericParameterTypes();
        if( parameterTypes.length != 1 || ! parameterTypes[0].equals(Context.class)) {
            throw new IllegalArgumentException("The annotated method " + getFullMethodName(theMethod) +
                    " should accept only one parameter of type yabdd.Context");
        }

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

    private String getFullMethodName(Method aMethod) {
        return aMethod.getDeclaringClass().getCanonicalName() + "." + theMethod.getName() + "()";
    }

    private transient Pattern pattern;
    public ImmutableList<String> match(String text) {
        if(pattern == null) {
            pattern = Pattern.compile(value);
        }
        Matcher matcher = pattern.matcher(text);
        if(matcher.matches()) {
            String[] result = new String[matcher.groupCount()];
            for(int i = 0; i < matcher.groupCount(); i++) {
                result[i] = matcher.group(i + 1);
            }
            return ImmutableList.copyOf(result);
        } else {
            return null;
        }
    }

    public void execute(Context context) throws InvocationTargetException, IllegalAccessException {
        theMethod.invoke(null, context);
    }
}
