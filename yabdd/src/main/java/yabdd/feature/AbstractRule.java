package yabdd.feature;

import lombok.Data;
import lombok.NonNull;

/**
 * The basis for Given, When, Then rules
 * This class is immutable
 * Created by Marco Cosentino on 11/03/15.
 */
@Data
public class AbstractRule {
    @NonNull private final String body;
}
