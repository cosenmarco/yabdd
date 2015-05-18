package yabdd.rules;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.ToString;
import yabdd.Context;
import yabdd.ScenarioContext;
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
    private transient Class<?>[] parameterTypes;
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
        parameterTypes = theMethod.getParameterTypes();

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
            int matchesCount = matcher.groupCount() + 1;
            String[] result = new String[matchesCount];
            for(int i = 0; i < matchesCount; i++) {
                result[i] = matcher.group(i);
            }
            return ImmutableList.copyOf(result);
        } else {
            return null;
        }
    }

    public void execute(ScenarioContext context) throws InvocationTargetException, IllegalAccessException,
            InstantiationException {
        Object rulesHost;
        Object[] parameters = new Object[parameterTypes.length];

        if(contextConstructor != null) {
            rulesHost = contextConstructor.newInstance(context);
        } else {
            rulesHost = defaultConstructor.newInstance();
        }

        List<String> captures = context.getRuleContext().getCaptures();

        // Sanity check
        assert captures.size() == parameterTypes.length + 1 : "The number of parameters of rule method " +
                getFullMethodName(theMethod) + " must match the number of effective captures in the rule's regex";

        for(int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if(String.class.isAssignableFrom(parameterType)) {
                parameters[i] = captures.get(i + 1);
            } else if(Integer.class.isAssignableFrom(parameterType)){
                parameters[i] = Integer.valueOf(captures.get(i + 1));
            } else if(Double.class.isAssignableFrom(parameterType)) {
                parameters[i] = Double.valueOf(captures.get(i + 1));
            } else {
                throw new UnsupportedOperationException("Unsupported parameter type for parameter " + (i + 1) +
                        "  rule method " + getFullMethodName(theMethod));
            }
        }
        theMethod.invoke(rulesHost, parameters);
    }
}
