package yabdd;

/**
 * The main orchestrator of the test suite. It is built once per test execution
 * Created by Marco Cosentino on 28/02/15.
 */
public class YABDD {

    private final Package packg;

    public YABDD(Package packg) {
        this.packg = packg;
    }

    public void run(RunNotifier notifier) {
        // Build configuration
        // Fetch and build the rules

        // Fetch and parse the features


        // Run the features
        runFeatures(null, notifier);
    }

    private void runFeatures(Object o, RunNotifier notifier) {
        notifier.fireTestStarted(new Description());
        System.out.println("I ran");
        notifier.fireTestFinished(new Description());
    }
}
