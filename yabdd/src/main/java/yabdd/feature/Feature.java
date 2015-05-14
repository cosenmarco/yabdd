package yabdd.feature;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.NonNull;
import yabdd.rules.RulePackage;

import java.util.List;

/**
 * This class represents a Feature expressed in Gherkin
 * This class is immutable
 * Created by Marco Cosentino on 28/02/15.
 */
@Data
public class Feature {
    @NonNull private final ImmutableList<Tag> tags;
    @NonNull private final String title;
    @NonNull private final String description;
    @NonNull private final ImmutableList<Scenario> scenarios;
    @NonNull private final RulePackage packg;
    @NonNull private final String name;

    public Feature(List<Tag> tags, String title, String description, List<Scenario> scenarios,
                   RulePackage packg, String name) {
        this.tags = ImmutableList.copyOf(tags);
        this.title = title;
        this.description = description;
        this.scenarios = ImmutableList.copyOf(scenarios);
        this.packg = packg;
        this.name = name;
    }

    /**
     * @return a string built using the package and the simple name
     */
    public String getFullName() {
        return packg.getName() + "." + name;
    }
}
