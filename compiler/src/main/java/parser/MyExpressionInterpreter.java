package parser;

import parser.enums.ASTNodeTypeEnum;
import parser.model.ASTNode;

public class MyExpressionInterpreter {

    private final ASTNode rootTreeNode;

    public MyExpressionInterpreter(ASTNode rootTreeNode) {
        this.rootTreeNode = rootTreeNode;
    }

    public Object interpret() {
        return interpret(rootTreeNode);
    }

    private Object interpret(ASTNode astNode) {
        ASTNodeTypeEnum astNodeTypeEnum = astNode.getNodeType();
        switch (astNodeTypeEnum){
            case ADDITIVE_EXPRESSION:
                return evalAdditiveExpression(astNode);
            case MULTIPLICATIVE_EXPRESSION:
                return evalMultiplicativeExpression(astNode);
            case INT_LITERAL:
                return evalIntLiteral(astNode);
            default:
                throw new RuntimeException("un expected astNodeType=" + astNodeTypeEnum);
        }
    }

    private Object evalAdditiveExpression(ASTNode astNode){
        ASTNode opNum1Node = astNode.getChildren().get(0);
        ASTNode opNode = astNode.getChildren().get(1);
        ASTNode opNum2Node = astNode.getChildren().get(2);

        Integer opNum1 = transferToInt(interpret(opNum1Node));
        Integer opNum2 = transferToInt(interpret(opNum2Node));

        String opName = opNode.getText();
        switch (opName){
            case "+":
                return opNum1 + opNum2;
            case "-":
                return opNum1 - opNum2;
            default:
                throw new RuntimeException("evalAdditiveExpression error op=" + opName);
        }
    }

    private Object evalMultiplicativeExpression(ASTNode astNode){
        ASTNode opNum1Node = astNode.getChildren().get(0);
        ASTNode opNode = astNode.getChildren().get(1);
        ASTNode opNum2Node = astNode.getChildren().get(2);

        Integer opNum1 = transferToInt(interpret(opNum1Node));
        Integer opNum2 = transferToInt(interpret(opNum2Node));

        String opName = opNode.getText();
        switch (opName){
            case "*":
                return opNum1 * opNum2;
            case "/":
                return opNum1 / opNum2;
            default:
                throw new RuntimeException("evalAdditiveExpression error op=" + opName);
        }
    }

    private Integer evalIntLiteral(ASTNode astNode){
        return Integer.parseInt(astNode.getText());
    }

    /**
     * 暂时只支持整型
     * */
    private Integer transferToInt(Object operand){
        if(operand instanceof Integer){
            return (Integer) operand;
        }

        if(operand instanceof String){
            return Integer.parseInt((String)operand);
        }

        throw new RuntimeException("un support operand type：" + operand);
    }
}
