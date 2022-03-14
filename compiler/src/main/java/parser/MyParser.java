package parser;

import lexan.enums.TokenTypeEnum;
import lexan.model.Token;
import parser.enums.ASTNodeTypeEnum;
import parser.model.ASTNode;

import java.util.Arrays;

public class MyParser {

    private TokenReader tokenReader;

    public MyParser(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }



    /**
     * primary
     * -> IDENTIFIER
     * -> LITERAL
     * -> '( expression )'
     * */
    public ASTNode primary(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() == TokenTypeEnum.IDENTIFIER){
            // -> IDENTIFIER
            return new ASTNode(ASTNodeTypeEnum.Identifier,token.getValue());
        }

        if(token.getTokenTypeEnum() == TokenTypeEnum.LITERAL){
            // -> LITERAL（目前只支持整型字面量）
            return new ASTNode(ASTNodeTypeEnum.IntLiteral,token.getValue());
        }

        if(token.getTokenTypeEnum() == TokenTypeEnum.LEFT_PARENTHESES){
            // '( expression )'
            ASTNode expressionNode = expression();
            matchRightParentheses();
            return expressionNode;
        }

        throw new RuntimeException("primary 语法分析错误：" + token);
    }

    /**
     *
     * */
    public ASTNode expression(){
        return null;
    }

    /**
     *
     * */
    public ASTNode blockExpression(){
        return null;
    }

    /**
     * variableDeclarator (目前只支持整型变量定义)
     * -> variableDeclaratorId ('=' variableInitializer)
     * */
    public ASTNode variableDeclarator(){
        ASTNode variableDeclaratorIdNode = variableDeclaratorId();
        matchEqualsChar();
        ASTNode variableInitializerNode = variableInitializer();

        // -> variableDeclaratorId ('=' variableInitializer)
        ASTNode variableDeclaratorNode = new ASTNode(ASTNodeTypeEnum.IntDeclaration);
        variableDeclaratorNode.setChildren(Arrays.asList(variableDeclaratorIdNode,variableInitializerNode));
        return variableDeclaratorNode;
    }

    /**
     * variableInitializer
     * -> expression
     * */
    public ASTNode variableInitializer(){
        return expression();
    }

    /**
     * variableDeclaratorId
     * -> Identifier;
     * */
    public ASTNode variableDeclaratorId(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() == TokenTypeEnum.IDENTIFIER){
            // Identifier;
            ASTNode variableDeclaratorIdNode = new ASTNode(ASTNodeTypeEnum.Identifier,token.getValue());
            matchSemicolon();
            return variableDeclaratorIdNode;
        }

        throw new RuntimeException("variableDeclaratorId need IDENTIFIER 语法分析错误:" + token);
    }

    // ===========================================单token解析=================================================

    private void matchEqualsChar(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.ASSIGNMENT && token.getValue().equals("=")){
            throw new RuntimeException("语法分析错误 not match '=' :" + token);
        }
    }

    private void matchLeftParentheses(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.LEFT_PARENTHESES){
            throw new RuntimeException("语法分析错误 not match '(' :" + token);
        }
    }

    private void matchRightParentheses(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.RIGHT_PARENTHESES){
            throw new RuntimeException("语法分析错误 not match ')' :" + token);
        }
    }

    /**
     * 分号;
     * */
    private void matchSemicolon(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.SEMICOLON){
            throw new RuntimeException("语法分析错误 not match ';' :" + token);
        }
    }
}
