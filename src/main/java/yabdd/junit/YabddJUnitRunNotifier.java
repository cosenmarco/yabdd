package yabdd.junit;

import yabdd.Description;
import yabdd.Failure;
import yabdd.RunNotifier;

/**
 * A RunNotifier implementation that links the events with jUnit
 * Created by Marco Cosentino on 28/02/15.
 */
public class YabddJUnitRunNotifier implements RunNotifier {
    private final org.junit.runner.notification.RunNotifier notifier;

    public YabddJUnitRunNotifier(org.junit.runner.notification.RunNotifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void fireTestStarted(Description description) {
        notifier.fireTestStarted(org.junit.runner.Description.EMPTY);
    }

    @Override
    public void fireTestFinished(Description description) {
        notifier.fireTestFinished(org.junit.runner.Description.EMPTY);
    }

    @Override
    public void fireTestFailure(Failure failure) {
        // TODO
    }

    @Override
    public void fireTestIgnored(Description description) {
        // TODO
    }
}
