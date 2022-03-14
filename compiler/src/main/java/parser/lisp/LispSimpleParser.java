package parser.lisp;


import lexan.enums.TokenTypeEnum;
import lexan.model.Token;
import parser.TokenReader;
import parser.enums.ASTNodeTypeEnum;
import parser.model.ASTNode;
import parser.util.ParserUtil;

import java.util.Arrays;
import java.util.Collections;

public class LispSimpleParser {

    private TokenReader tokenReader;

    public LispSimpleParser(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }

    /**
     * 按照语法规则解析token列表，返回抽象语法树AST的根节点
     * */
    public ASTNode parse(){
        return SExpression();
    }

    /**
     * expression
     * -> (op exp1 exp2)
     * -> (exp)
     * -> (primary)
     * */
    private ASTNode SExpression(){
        Token peekToken = tokenReader.peekToken();
        if(peekToken.getTokenTypeEnum() != TokenTypeEnum.LEFT_PARENTHESES){
            // 最基本的S表达式：单独的primary
            return primary();
        }

        // 左边括号匹配
        matchLeftParentheses();

        ASTNode currentExpNode = new ASTNode(ASTNodeTypeEnum.S_EXPRESSION);
        Token token = tokenReader.peekToken();
        if(ParserUtil.isCalculateOp(token)){
            // -> (op exp1 exp2)
            ASTNode calculateOpNode = calculateOp();
            ASTNode exp1Node = SExpression();
            ASTNode exp2Node = SExpression();
            currentExpNode
                    .appendChildren(calculateOpNode)
                    .appendChildren(exp1Node)
                    .appendChildren(exp2Node);
        }else if(ParserUtil.isPrimary(token)){
            // -> (primary)
            ASTNode primaryNode = primary();
            currentExpNode.appendChildren(primaryNode);
        }else{
            // -> (exp)
            ASTNode expNode = SExpression();
            expNode.appendChildren(expNode);
        }

        // 右边括号匹配
        matchRightParentheses();

        return currentExpNode;
    }

    /**
     * primary
     * -> IDENTIFIER
     * -> LITERAL
     * */
    private ASTNode primary(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() == TokenTypeEnum.IDENTIFIER
                || token.getTokenTypeEnum() == TokenTypeEnum.LITERAL){
            return new ASTNode(ASTNodeTypeEnum.PRIMARY,token.getValue());
        }else{
            throw new RuntimeException("不符合语法规则");
        }
    }

    /**
     * primary
     * -> IDENTIFIER
     * -> LITERAL
     * */
    private ASTNode calculateOp(){
        Token token = tokenReader.readToken();
        if(ParserUtil.isCalculateOp(token)){
            return new ASTNode(ASTNodeTypeEnum.CALCULATE_OP,token.getValue());
        }else{
            throw new RuntimeException("不符合语法规则");
        }
    }

    private void matchLeftParentheses(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.LEFT_PARENTHESES){
            throw new RuntimeException("not match '(' :" + token);
        }
    }

    private void matchRightParentheses(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.RIGHT_PARENTHESES){
            throw new RuntimeException("not match ') :" + token);
        }
    }
}
