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
@ToString(of = {"type", "value", "rulePackage"})
public class Rule {
    @Getter
    private RuleType type;

    @Getter
    private Method theMethod;

    @Getter
    private String value;

    @Getter
    private RulePackage rulePackage;

    private transient Constructor<?> defaultConstructor;
    private transient Constructor<?> contextConstructor;
    private transient Class<?>[] parameterTypes;
    private transient Class<?> klass;

    public Rule(RuleType type, Method theMethod) {
        this.type = type;
        this.theMethod = theMethod;

        klass = theMethod.getDeclaringClass();
        rulePackage = new RulePackage(klass.getPackage().getName());

        init();
    }

    /**
     * This constructor allows to override the package
     */
    public Rule(RuleType type, RulePackage packg, Method theMethod) {
        this.type = type;
        this.theMethod = theMethod;

        klass = theMethod.getDeclaringClass();
        rulePackage = packg;

        init();
    }

    private void init() {
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
            throw new IllegalArgumentException("The class declaring method " + RuleHelper.getFullMethodName(theMethod) +
            " should implement either the default constructor or a constructor which takes a yabdd.Context object");
        }

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
        String fullName = RuleHelper.getFullMethodName(theMethod);

        // Sanity check
        assert captures.size() == parameterTypes.length + 1 : "The number of parameters of rule method " +
                fullName + " must match the number of effective captures in the rule's regex";

        for(int i = 0; i < parameterTypes.length; i++) {
            try {
                parameters[i] = RuleHelper.convertMatchBasedOnParameterType(parameterTypes[i], captures.get(i + 1));
            } catch(UnsupportedOperationException ex) {
                throw new UnsupportedOperationException("Unsupported parameter type for parameter " + (i + 1) +
                        " of rule method " + fullName);
            }
        }
        theMethod.invoke(rulesHost, parameters);
    }
}
