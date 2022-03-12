package parser.model;


import parser.enums.ASTNodeTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {

    private ASTNode parent;
    private List<ASTNode> children = new ArrayList<>();
    private ASTNodeTypeEnum nodeType;
    private String text = "";

    public ASTNode(ASTNodeTypeEnum nodeType) {
        this.nodeType = nodeType;
    }

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
        // 建立双向关联
        this.children = children;
        for(ASTNode child : children){
            child.parent = this;
        }
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

    public void printTree(){
        System.out.println(this.getNodeType() + " " + this.getText());
        for (ASTNode child : this.getChildren()) {
            printTree(child, "\t");
        }
    }

    private void printTree(ASTNode astNode,String indent){
        System.out.println(indent + astNode.getNodeType() + " " + astNode.getText());
        for (ASTNode child : astNode.getChildren()) {
            printTree(child, indent + "\t");
        }
    }
}
