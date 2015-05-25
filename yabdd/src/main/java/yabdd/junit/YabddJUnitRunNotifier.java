package yabdd.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import yabdd.Context;
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

    private Description buildDescription(Context context) {
        return Description.createSuiteDescription(context.getFeature().getFullName() + " [YABDD Feature]");
    }

    @Override
    public void fireTestStarted(Context context) {
        notifier.fireTestStarted(buildDescription(context));
    }

    @Override
    public void fireTestFinished(Context context) {
        notifier.fireTestFinished(buildDescription(context));
    }

    @Override
    public void fireTestFailure(Context context, Throwable t) {
        notifier.fireTestFailure(new Failure(buildDescription(context), t));
    }

    @Override
    public void fireTestIgnored(Context context) {
        notifier.fireTestIgnored(buildDescription(context));
    }
}
