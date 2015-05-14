package yabdd.rules.store;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import yabdd.RuleContext;
import yabdd.rules.Rule;
import yabdd.rules.RulePackage;
import yabdd.rules.RuleType;

import java.util.ArrayList;
import java.util.List;

/**
 * The rule store contains all the rules defined by the users and helps in matching the rules given a
 * pattern. It presents a convenient interface to work with rules and package matching
 *
 * Created by Marco Cosentino on 21/03/15.
 */
public class RulesStore {
    PackageTreeNode rootNode = new PackageTreeNode("");

    public void registerRule(Rule rule) {
        PackageTreeNode currentNode = rootNode;
        for(String part : rule.getRulePackage().getParts()) {
            PackageTreeNode child = currentNode.getChildForPart(part);
            if(child != null) {
                currentNode = child;
            } else {
                child = new PackageTreeNode(part);
                currentNode.addChild(child);
                currentNode = child;

            }
        }
        currentNode.addRule(rule);
    }

    public RuleMatch matchRuleBy(RulePackage targetPackage, RuleType type, String text) {
        List<PackageTreeNode> treePath = new ArrayList<PackageTreeNode>();
        PackageTreeNode currentNode = rootNode;
        for(String part : targetPackage.getParts()) {
            PackageTreeNode child = currentNode.getChildForPart(part);
            if(child == null) {
                break;
            }
            treePath.add(child);
            currentNode = child;
        }
        List<PackageTreeNode> revTreePath = Lists.reverse(treePath);
        for(PackageTreeNode node : revTreePath) {
            for(Rule rule : node.getRulesInPackage()) {
                if(rule.getType() == type) {
                    ImmutableList<String> matchResult = rule.match(text);
                    if(matchResult != null) {
                        return new RuleMatch(rule, new RuleContext(text, matchResult));
                    }
                }
            }
        }
        return null;
    }
}
