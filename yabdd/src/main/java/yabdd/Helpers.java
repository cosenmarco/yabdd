package yabdd;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Static helpers for YABDD
 * Created by Marco Cosentino on 14/03/15.
 */
public class Helpers {
    private final Logger LOG = LoggerFactory.getLogger(Helpers.class);

    public static final String FEATURE_FILE_EXT = "feature";

    public static Set<String> findFeatureFiles(Reflections reflections) {
        return reflections.getResources(Pattern.compile(".*\\." + FEATURE_FILE_EXT));
    }
}
