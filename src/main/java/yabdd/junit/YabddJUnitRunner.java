package yabdd.junit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import yabdd.YABDD;

/**
 * The jUnit Runner for YABDD. Represents the entry point for a jUnit based test suite
 * Created by Marco Cosentino on 28/02/15.
 */
public class YabddJUnitRunner extends Runner {
    public YabddJUnitRunner(Class<?> klass) {}

    @Override
    public Description getDescription() {
        return Description.EMPTY;
    }

    @Override
    public void run(RunNotifier runNotifier) {
        yabdd.RunNotifier yabddNotifier = new YabddJUniteRunNotifier(runNotifier);
        YABDD yabdd = new YABDD();
        yabdd.run(yabddNotifier);
    }
}
