package yabdd.feature;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * This class represents a Feature expressed in Gherkin
 * This class is immutable
 * Created by technics on 28/02/15.
 */
@Data
public class Feature {
    @NonNull private final ImmutableList<Tag> tags;
    @NonNull private final String title;
    @NonNull private final String description;
    @NonNull private final ImmutableList<Scenario> scenarios;
    @NonNull private final String resourcePath;

    public Feature(List<Tag> tags, String title, String description, List<Scenario> scenarios, String resourcePath) {
        this.tags = ImmutableList.copyOf(tags);
        this.title = title;
        this.description = description;
        this.scenarios = ImmutableList.copyOf(scenarios);
        this.resourcePath = resourcePath;
    }
}
