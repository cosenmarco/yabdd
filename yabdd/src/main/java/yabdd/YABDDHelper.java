package yabdd;

import org.reflections.Reflections;
import yabdd.annotations.Given;
import yabdd.rules.Rule;
import yabdd.rules.RulePackage;
import yabdd.rules.RuleType;
import yabdd.rules.store.RulesStore;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Helpful, right?
 * Created by Marco Cosentino on 25/05/15.
 */
public class YABDDHelper {

    public static void findAndRegisterRulesForAnnotation(RulesStore ruleStore, Reflections reflections,
                                                         Class<? extends Annotation> ann) {
        Set<Method> foundMethods = reflections.getMethodsAnnotatedWith(ann);
        for(Method method : foundMethods) {
            ruleStore.registerRule(new Rule(RuleType.byAnnotation(ann), method));
        }
    }

    public static void registerStandardRuleMethod(RulesStore ruleStore, Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for(RuleType type : RuleType.values()) {
            for(Annotation annotation : annotations) {
                if(type.getAnnotationClass().equals(annotation.annotationType())) {
                    ruleStore.registerRule(new Rule(type, RulePackage.ROOT, method));
                }
            }
        }
    }
}
