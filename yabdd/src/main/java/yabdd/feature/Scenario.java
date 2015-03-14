package yabdd.feature;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * Represents a Gherkin Scenario
 * This class is immutable
 * Created by Marco Cosentino on 11/03/15.
 */
@Data
public class Scenario {
    @NonNull private final ImmutableList<Tag> tags;
    @NonNull private final String title;
    @NonNull private final String description;
    @NonNull private final ImmutableList<Given> givens;
    @NonNull private final ImmutableList<When> whens;
    @NonNull private final ImmutableList<Then> thens;

    public Scenario(List<Tag> tags, String title, String description,
                    List<Given> givens, List<When> whens, List<Then> thens) {
        this.tags = ImmutableList.copyOf(tags);
        this.title = title;
        this.description = description;
        this.givens = ImmutableList.copyOf(givens);
        this.whens = ImmutableList.copyOf(whens);
        this.thens = ImmutableList.copyOf(thens);
    }
}
