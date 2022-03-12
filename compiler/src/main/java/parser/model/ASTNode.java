package parser.model;


import parser.enums.ASTNodeTypeEnum;

import java.util.List;

public class ASTNode {

    private ASTNode parent;
    private List<ASTNode> children;
    private ASTNodeTypeEnum nodeType;
    private String text;

    public ASTNode(ASTNodeTypeEnum nodeType, String text) {
        this.nodeType = nodeType;
        this.text = text;
    }

    public ASTNode getParent() {
        return parent;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public void setChildren(List<ASTNode> children) {
        this.children = children;
    }

    public ASTNodeTypeEnum getNodeType() {
        return nodeType;
    }

    public void setNodeType(ASTNodeTypeEnum nodeType) {
        this.nodeType = nodeType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
