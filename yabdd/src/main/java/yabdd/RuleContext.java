package yabdd;

import com.google.common.collect.ImmutableList;
import lombok.Data;

import java.util.List;

/**
 * A simple data object that holds some rule specific info
 * Created by Marco Cosentino on 13/05/15.
 */
@Data
public class RuleContext {
    private final String content;
    private final ImmutableList<String> captures;
}
