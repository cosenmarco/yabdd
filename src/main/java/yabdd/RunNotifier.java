package yabdd;

/**
 * Interface for a notifier which handles the various test runs
 * Created by Marco Cosentino on 28/02/15.
 */
public interface RunNotifier {
    void fireTestStarted(Description description);
    void fireTestFinished(Description description);
    void fireTestFailure(Failure failure);
    void fireTestIgnored(Description description);
}
