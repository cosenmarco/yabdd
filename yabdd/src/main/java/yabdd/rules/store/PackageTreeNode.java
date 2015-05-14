package yabdd.rules.store;

import lombok.Getter;
import yabdd.rules.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for creating a tree of RulePackage and rules to be found in the pacakge
 * Created by Marco Cosentino on 03/04/15.
 */
public class PackageTreeNode {
    @Getter
    List<PackageTreeNode> childNodes;

    @Getter
    List<Rule> rulesInPackage;

    @Getter
    String nodePart;

    public PackageTreeNode(String nodePart) {
        this.nodePart = nodePart;
        childNodes = new ArrayList<PackageTreeNode>();
        rulesInPackage = new ArrayList<Rule>();
    }

    public void addChild(PackageTreeNode child) {
        childNodes.add(child);
    }

    public void addRule(Rule rule) {
        rulesInPackage.add(rule);
    }

    public PackageTreeNode getChildForPart(String childPart) {
        for (PackageTreeNode child : childNodes) {
            if (child.getNodePart().equals(childPart)) {
                return child;
            }
        }
        return null;
    }
}
