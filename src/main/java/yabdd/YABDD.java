package yabdd;

import com.google.common.collect.ImmutableList;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yabdd.feature.Feature;
import yabdd.feature.parsing.Parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * The main orchestrator of the test suite. It is built once per test execution
 * Created by Marco Cosentino on 28/02/15.
 */
public class YABDD {
    private final Logger LOG = LoggerFactory.getLogger(YABDD.class);
    private final Class<?> klass;
    Reflections reflections;

    public YABDD(Class<?> klass) {
        this.klass = klass;
        reflections = new Reflections(
                ClasspathHelper.forClass(klass),
                new ResourcesScanner());
    }

    public void run(RunNotifier notifier) {
        ImmutableList<Feature> features;

        // Build configuration
        // Fetch and build the rules

        // Fetch and parse the features
        features = fetchAndParseFeatures();

        // Run the features
        runFeatures(features, notifier);
    }

    private ImmutableList<Feature> fetchAndParseFeatures() {
        List<Feature> features = new ArrayList<Feature>();
        Set<String> featureFiles = reflections.getResources(Pattern.compile(".*\\.feature"));
        LOG.info("Found feature files: {}", featureFiles);

        for(String resourcePath : featureFiles) {
            InputStream featureInputStream = klass.getClassLoader().getResourceAsStream(resourcePath);
            features.add(Parser.parse(featureInputStream, resourcePath));
        }
        return ImmutableList.copyOf(features);
    }

    private void runFeatures(Object o, RunNotifier notifier) {
        notifier.fireTestStarted(new Description());
        System.out.println("I ran");
        notifier.fireTestFinished(new Description());
    }
}
