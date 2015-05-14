package yabdd;

/**
 * Interface for a notifier which handles the various test runs
 * Created by Marco Cosentino on 28/02/15.
 */
public interface RunNotifier {
    void fireTestStarted(Context context);
    void fireTestFinished(Context context);
    void fireTestFailure(Context context, Throwable t);
    void fireTestIgnored(Context context);
}
