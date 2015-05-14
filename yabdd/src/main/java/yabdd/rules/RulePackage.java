package yabdd.rules;

import com.google.common.base.Joiner;
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

    public RulePackage(String[] pathParts) {
        parts = ImmutableList.copyOf(pathParts);
    }

    public static RulePackage fromResourcePath(String resourcePath) {
        int lastSlash = resourcePath.lastIndexOf('/');
        if(lastSlash >= 0) {
            String[] pathParts = resourcePath.substring(0, lastSlash).split("/");
            return new RulePackage(pathParts);
        } else {
            return new RulePackage(new String[]{});
        }
    }

    public String getName() {
        return Joiner.on(".").join(parts);
    }
}
