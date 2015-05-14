package yabdd.rules.store;

import lombok.Data;
import yabdd.RuleContext;
import yabdd.rules.Rule;

/**
 * Represents the result of a matching operation within the RuleStore
 * Created by Marco Cosentino on 13/05/15.
 */
@Data
public class RuleMatch {
    private final Rule matchedRule;
    private final RuleContext ruleContext;
}
