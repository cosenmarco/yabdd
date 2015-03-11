package yabdd.feature;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * Represents a package in which a Feature is declared
 * Created by Marco Cosentino on 11/03/15.
 */
@Data
public class Package {
    @NonNull
    private final ImmutableList<String> components;

    public Package(List<String> components) {
        this.components = ImmutableList.copyOf(components);
    }
}
