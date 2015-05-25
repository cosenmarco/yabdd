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
import yabdd.rules.standard.StandardRules;
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

        fetchAndBuildStandardRules(ruleStore);

        YABDDHelper.findAndRegisterRulesForAnnotation(ruleStore, reflections, Given.class);
        YABDDHelper.findAndRegisterRulesForAnnotation(ruleStore, reflections, When.class);
        YABDDHelper.findAndRegisterRulesForAnnotation(ruleStore, reflections, Then.class);

        LOG.debug("Rules Store after finding all rules: {}", ruleStore);
        return ruleStore;
    }

    private void fetchAndBuildStandardRules(RulesStore ruleStore) {
        for(Method method : StandardRules.class.getDeclaredMethods()) {
            YABDDHelper.registerStandardRuleMethod(ruleStore, method);
        }
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
                ScenarioContext context = new ScenarioContext(feature, scenario);
                notifier.fireTestStarted(context);
                try {
                    for (yabdd.feature.Given given : scenario.getGivens()) {
                        runRule(context, ruleStore, given, RuleType.GIVEN);
                    }
                    for (yabdd.feature.When when : scenario.getWhens()) {
                        runRule(context, ruleStore, when, RuleType.WHEN);
                    }
                    for (yabdd.feature.Then then : scenario.getThens()) {
                        runRule(context, ruleStore, then, RuleType.THEN);
                    }
                    notifier.fireTestFinished(context);
                } catch (Exception e) {
                    notifier.fireTestFailure(context, e);
                }
            }
        }
    }

    private void runRule(ScenarioContext context, RulesStore ruleStore, AbstractRule rule,
                         RuleType ruleType) throws Exception {

        RulePackage featurePackage = context.getFeature().getPackg();
        RuleMatch ruleMatch = ruleStore.matchRuleBy(featurePackage, ruleType, rule.getBody());

        if(ruleMatch != null) {
            context.setRuleContext(ruleMatch.getRuleContext());
            try {
                ruleMatch.getMatchedRule().execute(context);
            } catch (InvocationTargetException e) {
                if(e.getCause() != null && e.getCause() instanceof AssertionError){
                    AssertionError error = (AssertionError) e.getCause();
                    LOG.error("Rule '{} {}' asserted something wrong", ruleType.getTypeString(),
                            rule.getBody(), error);
                    throw error;
                } else {
                    throw e;
                }
            }
        } else {
            LOG.error("Cannot find any rule matching {}", rule);
            throw new RuntimeException("Rule " + rule.toString() + " cannot be matched");
        }
    }
}
