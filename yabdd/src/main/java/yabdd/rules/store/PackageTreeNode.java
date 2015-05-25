package yabdd.rules.store;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
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
    private final List<PackageTreeNode> childNodes;

    @Getter
    private final List<Rule> rulesInPackage;

    @Getter
    private final String nodePart;

    @Getter
    private final PackageTreeNode parent;

    public PackageTreeNode(String nodePart, PackageTreeNode parent) {
        this.nodePart = nodePart;
        childNodes = new ArrayList<PackageTreeNode>();
        rulesInPackage = new ArrayList<Rule>();
        this.parent = parent;
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

    @Override
    public String toString() {
        List <String> parts = new ArrayList<String>();
        PackageTreeNode node = this;

        do {
            String part = node.getNodePart();
            if(part != null) {
                parts.add(part);
            }
            node = node.getParent();
        } while(node != null);

        List <String> revparts = Lists.reverse(parts);
        StringBuilder result = new StringBuilder();

        result.append(Joiner.on(".").join(revparts));
        result.append("(").append(this.getRulesInPackage().size()).append(" rules)");
        return result.toString();
    }
}
