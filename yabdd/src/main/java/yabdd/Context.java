package yabdd;

import yabdd.feature.Feature;
import yabdd.feature.Scenario;
import yabdd.rules.RulePackage;

import java.util.List;

/**
 * Public interface for the context objects that get passed in the methods annotated
 * as rules (@Given, @When, @Then)
 * Created by Marco Cosentino on 13/05/15.
 */
public interface Context {
    /**
     * @return The current Scenario
     */
    Scenario getScenario();

    /**
     * @return The current feature
     */
    Feature getFeature();

    /**
     * Can be used to store objects in the context that can be reused in other rules later
     * @param label a String that marks the object to store and is used later to retrieve the stored object
     * @param object the object to store in the context
     */
    void put(String label, Object object);

    /**
     * Can be used to get an object stored using put()
     * @param label the string that marks the object you want to retrieve from te context
     * @return the previously stored object corresponding to the label
     */
    Object get(String label);

    /**
     * Gives all the captures of the matching process
     * @return a list of captures. The first capture is always the full matched string
     */
    List<String> getRuleCaptures();
}
