package yabdd.rules;

import com.google.common.collect.ImmutableList;
import lombok.Data;

/**
 * Represents a Package into which the various rules are defined
 * Created by Marco Cosentino on 03/04/15.
 */
@Data
public class RulePackage {
    private final ImmutableList<String> parts;
    public RulePackage(String packageName) {
        String[] localParts = packageName.split("\\.");
        parts = ImmutableList.copyOf(localParts);
    }
}
