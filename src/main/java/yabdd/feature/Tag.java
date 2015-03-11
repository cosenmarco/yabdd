package yabdd.feature;

import lombok.Data;
import lombok.NonNull;

/**
 * Represents a Gherkin Tag
 * Created by Marco Cosentino on 11/03/15.
 */
@Data
public class Tag {
    @NonNull
    private final String tag;
}
