package parser;

import parser.enums.ASTNodeTypeEnum;
import parser.model.ASTNode;

public class LispSimpleInterpreter {

    private final ASTNode rootTreeNode;

    public LispSimpleInterpreter(ASTNode rootTreeNode) {
        this.rootTreeNode = rootTreeNode;
    }

    public Object interpret(){
        return interpret(rootTreeNode);
    }

    public Object interpret(ASTNode astNode){
        if(astNode.getNodeType() == ASTNodeTypeEnum.PRIMARY){
            // -> (primary)
            return astNode.getText();
        }

        if(astNode.getNodeType() == ASTNodeTypeEnum.S_EXPRESSION){
            ASTNode firstChild = astNode.getChildren().get(0);
            if(firstChild.getNodeType() == ASTNodeTypeEnum.CALCULATE_OP){
                // -> (op exp1 exp2)
                String op = firstChild.getText();
                Object exp1Result = interpret(astNode.getChildren().get(1));
                Object exp2Result = interpret(astNode.getChildren().get(2));

                // 根据操作符和操作数进行运算
                return calculateExpInterpret(op,exp1Result,exp2Result);
            }

            if(firstChild.getNodeType() == ASTNodeTypeEnum.PRIMARY){
                // -> (exp)
                return interpret(firstChild);
            }
        }

        throw new RuntimeException("ERROR astNode=" + astNode);
    }

    /**
     * 目前仅支持二元算数操作符
     * 简单起见，只支持整数计算
     * */
    private Integer calculateExpInterpret(String op, Object operand1, Object operand2){
        Integer int1 = transferToInt(operand1);
        Integer int2 = transferToInt(operand2);

        switch (op){
            case "+":
                return int1 + int2;
            case "-":
                return int1 - int2;
            case "*":
                return int1 * int2;
            case "/":
                return int1 / int2;
            default:
                throw new RuntimeException("calculate un support op =" + op);
        }
    }

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
