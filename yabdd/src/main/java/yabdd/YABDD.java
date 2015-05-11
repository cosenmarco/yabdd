package yabdd;

import com.google.common.collect.ImmutableList;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yabdd.annotations.Given;
import yabdd.annotations.Then;
import yabdd.annotations.When;
import yabdd.feature.Feature;
import yabdd.feature.parsing.Parser;
import yabdd.rules.Rule;
import yabdd.rules.RuleType;
import yabdd.rules.store.RulesStore;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * The main orchestrator of the test suite. It is built once per test execution
 * Created by Marco Cosentino on 28/02/15.
 */
public class YABDD {
    private final static String FEATURE_EXT = "feature";
    private final Logger LOG = LoggerFactory.getLogger(YABDD.class);
    private final Class<?> klass;

    private final Reflections reflections;

    public YABDD(Class<?> klass) {
        this.klass = klass;
        reflections = new Reflections(
                ClasspathHelper.forClass(klass),
                new ResourcesScanner(), new MethodAnnotationsScanner());
    }

    public void run(RunNotifier notifier) {
        ImmutableList<Feature> features;

        // Build configuration
        // Fetch and build the rules
        RulesStore rules = fetchAndBuildRules();

        // Fetch and parse the features
        features = fetchAndParseFeatures();

        // Run the features
        runFeatures(features, notifier);
    }

    private RulesStore fetchAndBuildRules() {
        List<Rule> rules = new ArrayList<Rule>();

        Set<Method> givenMethods = reflections.getMethodsAnnotatedWith(Given.class);
        for(Method givenMethod : givenMethods) {
            rules.add(new Rule(RuleType.GIVEN, givenMethod));
        }

        Set<Method> whenMethods = reflections.getMethodsAnnotatedWith(When.class);
        for(Method method : whenMethods) {
            rules.add(new Rule(RuleType.WHEN, method));
        }

        Set<Method> thenMethods = reflections.getMethodsAnnotatedWith(Then.class);
        for(Method method : thenMethods) {
            rules.add(new Rule(RuleType.THEN, method));
        }

        LOG.debug("All rules: {}", rules);
        return null;
    }

    private ImmutableList<Feature> fetchAndParseFeatures() {
        List<Feature> features = new ArrayList<Feature>();
        Set<String> featureFiles = reflections.getResources(Pattern.compile(".*\\." + FEATURE_EXT));
        LOG.info("Found feature files: {}", featureFiles);

        for(String resourcePath : featureFiles) {
            InputStream featureInputStream = klass.getClassLoader().getResourceAsStream(resourcePath);
            Feature feature;
            try {
                feature = Parser.parse(featureInputStream, resourcePath);
                features.add(feature);
            } catch (Exception ex) {
                LOG.error("Cannot parse feature {}", resourcePath, ex);
            }
        }
        return ImmutableList.copyOf(features);
    }

    private void runFeatures(Object o, RunNotifier notifier) {
        notifier.fireTestStarted(new Description());
        System.out.println("I ran");
        notifier.fireTestFinished(new Description());
    }
}
