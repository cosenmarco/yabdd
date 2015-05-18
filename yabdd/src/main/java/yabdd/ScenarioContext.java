package yabdd;

import lombok.Getter;
import lombok.Setter;
import yabdd.feature.Feature;
import yabdd.feature.Scenario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This context object is passed into every rule when
 * Created by Marco Cosentino on 13/05/15.
 */
public class ScenarioContext implements Context {
    private final Map<String, Object> whiteboard;
    private Feature feature;
    private Scenario scenario;

    @Getter
    @Setter
    private RuleContext ruleContext;

    public ScenarioContext(Feature feature, Scenario scenario) {
        this.feature = feature;
        this.scenario = scenario;
        this.whiteboard = new HashMap<String, Object>();
    }

    @Override
    public Scenario getScenario() {
        return scenario;
    }

    @Override
    public Feature getFeature() {
        return feature;
    }

    @Override
    public void put(String label, Object object) {
        whiteboard.put(label, object);
    }

    @Override
    public Object get(String label) {
        return whiteboard.get(label);
    }

    @Override
    public List<String> getRuleCaptures() {
        return ruleContext.getCaptures();
    }
}
