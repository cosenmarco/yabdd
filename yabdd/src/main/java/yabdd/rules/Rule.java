package yabdd.rules;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.ToString;
import yabdd.Context;
import yabdd.annotations.Given;
import yabdd.annotations.When;
import yabdd.annotations.Then;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
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

    private transient Constructor<?> defaultConstructor;
    private transient Constructor<?> contextConstructor;
    private transient Class<?> klass;

    public Rule(RuleType type, Method theMethod) {
        this.type = type;
        this.theMethod = theMethod;

        klass = theMethod.getDeclaringClass();

        for( Constructor<?> constructor : klass.getConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if(parameterTypes.length == 0) {
                defaultConstructor = constructor;
            }
            if(parameterTypes.length == 1 && Context.class.isAssignableFrom(parameterTypes[0])) {
                contextConstructor = constructor;
            }
        }

        // Sanity checks
        if(defaultConstructor == null && contextConstructor == null) {
            throw new IllegalArgumentException("The class declaring method " + getFullMethodName(theMethod) +
            " should implement either the default constructor or a constructor which takes a yabdd.Context object");
        }

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

    private static String getFullMethodName(Method aMethod) {
        return aMethod.getDeclaringClass().getCanonicalName() + "." + aMethod.getName() + "()";
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

    public void execute(Context context) throws InvocationTargetException, IllegalAccessException,
            InstantiationException {
        Object rulesHost;
        if(contextConstructor != null) {
            rulesHost = contextConstructor.newInstance(context);
        } else {
            rulesHost = defaultConstructor.newInstance();
        }
        theMethod.invoke(rulesHost);
    }
}
