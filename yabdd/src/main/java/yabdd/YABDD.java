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
import yabdd.feature.AbstractRule;
import yabdd.feature.Feature;
import yabdd.feature.Scenario;
import yabdd.feature.parsing.Parser;
import yabdd.rules.Rule;
import yabdd.rules.RulePackage;
import yabdd.rules.RuleType;
import yabdd.rules.store.RuleMatch;
import yabdd.rules.store.RulesStore;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
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
        runFeatures(features, rules, notifier);
    }

    private RulesStore fetchAndBuildRules() {
        RulesStore ruleStore = new RulesStore();

        Set<Method> givenMethods = reflections.getMethodsAnnotatedWith(Given.class);
        for(Method givenMethod : givenMethods) {
            ruleStore.registerRule(new Rule(RuleType.GIVEN, givenMethod));
        }

        Set<Method> whenMethods = reflections.getMethodsAnnotatedWith(When.class);
        for(Method method : whenMethods) {
            ruleStore.registerRule(new Rule(RuleType.WHEN, method));
        }

        Set<Method> thenMethods = reflections.getMethodsAnnotatedWith(Then.class);
        for(Method method : thenMethods) {
            ruleStore.registerRule(new Rule(RuleType.THEN, method));
        }

        LOG.debug("Rules Store: {}", ruleStore);
        return ruleStore;
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

    private void runFeatures(ImmutableList<Feature> features, RulesStore ruleStore, RunNotifier notifier) {
        for(Feature feature : features) {

            for(Scenario scenario : feature.getScenarios()) {
                RulePackage featurePackage = RulePackage.fromResourcePath(feature.getResourcePath());
                ScenarioContext context = new ScenarioContext(feature, scenario);
                Description featureDesc = new Description(context);
                notifier.fireTestStarted(featureDesc);
                AbstractRule currentRule = null;
                try {
                    for (yabdd.feature.Given given : scenario.getGivens()) {
                        currentRule = given;
                        runRule(context, ruleStore, given, featurePackage, RuleType.GIVEN);
                    }
                    for (yabdd.feature.When when : scenario.getWhens()) {
                        currentRule = when;
                        runRule(context, ruleStore, when, featurePackage, RuleType.WHEN);
                    }
                    for (yabdd.feature.Then then : scenario.getThens()) {
                        currentRule = then;
                        runRule(context, ruleStore, then, featurePackage, RuleType.THEN);
                    }
                    notifier.fireTestFinished(featureDesc);
                } catch (Exception e) {
                    notifier.fireTestFailure(new Failure(featureDesc, e));
                }
            }
        }
    }

    private void runRule(ScenarioContext context, RulesStore ruleStore, AbstractRule rule,
                         RulePackage featurePackage, RuleType ruleType) throws Exception {

        RuleMatch ruleMatch = ruleStore.matchRuleBy(featurePackage, ruleType, rule.getBody());

        if(ruleMatch != null) {
            context.setRuleContext(ruleMatch.getRuleContext());
            try {
                ruleMatch.getMatchedRule().execute(context);
            } catch (InvocationTargetException e) {
                if(e.getCause() != null && e.getCause() instanceof AssertionError){
                    throw (Exception) e.getCause();
                } else {
                    throw e;
                }
            }
        } else {
            LOG.error("Cannot find any rule matching {}", rule);
        }
    }
}
