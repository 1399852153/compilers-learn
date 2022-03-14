package parser;

import com.sun.org.apache.regexp.internal.RE;
import lexan.enums.TokenTypeEnum;
import lexan.model.Token;
import parser.enums.ASTNodeTypeEnum;
import parser.model.ASTNode;
import parser.util.ParserUtil;

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
     * block
     * -> '{' blockStatements '}'
     * */
    public ASTNode block(){
        // -> '{' blockStatements '}'
        matchLeftBrace();
        ASTNode blockStatementsNode = blockStatements();
        matchRightBrace();

        return blockStatementsNode;
    }

    /**
     * blockStatements
     * -> blockStatement* (0-N个blockStatement)
     * */
    public ASTNode blockStatements(){
        Token token = tokenReader.peekToken();
        ASTNode blockStatementsNode = new ASTNode(ASTNodeTypeEnum.BLOCK_STATEMENTS);

        // LL1预测（基于block的语法推到规则，下一个token若不为’}‘，则认为需要以blockStatement进行解析）
        while(token.getTokenTypeEnum() != TokenTypeEnum.RIGHT_BRACE){
            ASTNode childBlockStmtNode = blockStatement();
            blockStatementsNode.appendChildren(childBlockStmtNode);
            token = tokenReader.peekToken();
        }

        return blockStatementsNode;
    }

    /**
     * blockStatement
     * -> variableDeclarators ';'
     * -> statement
     * */
    public ASTNode blockStatement(){
        Token token = tokenReader.peekToken();

        // LL1预测
        if(ParserUtil.isKeywordTypeDefineToken(token)){
            // -> variableDeclarators ';'
            ASTNode variableDeclaratorsNode = variableDeclarators();
            matchSemicolon();

            return variableDeclaratorsNode;
        }

        // -> statement


        return null;
    }

    /**
     *
     * */
    public ASTNode statement(){
        return null;
    }

    /**
     * primitiveType
     * -> INT
     * */
    public ASTNode primitiveType(){
        Token token = tokenReader.peekToken();
        if(ParserUtil.isKeywordTypeDefineToken(token)){
            return new ASTNode(ASTNodeTypeEnum.PRIMARY_TYPE,token.getValue());
        }

        throw new RuntimeException("primitiveType match error :" + token);
    }

    /**
     * typeType
     * -> primitiveType(目前只支持基础类型)
     * */
    public ASTNode typeType(){
        Token token = tokenReader.peekToken();

        if(ParserUtil.isKeywordTypeDefineToken(token)){
            // (目前只支持基础类型)
            return primitiveType();
        }

        throw new RuntimeException("typeType match error :" + token);
    }

    /**
     * 变量定义集合（类型 + 变量定义描述）
     * variableDeclarators
     * -> typeType variableDeclarator
     * */
    public ASTNode variableDeclarators(){
        ASTNode typeTypeNode = typeType();
        ASTNode variableDeclaratorNode = variableDeclarator();

        // 类型 + 变量定义描述
        ASTNode variableDeclaratorsNode = new ASTNode(ASTNodeTypeEnum.VARIABLE_DECLARATORS);
        variableDeclaratorsNode
                .appendChildren(typeTypeNode)
                .appendChildren(variableDeclaratorNode);
        return variableDeclaratorsNode;
    }

    /**
     * 变量定义描述 （ 变量名 = 变量初始化表达式）
     * variableDeclarator (目前只支持整型变量定义)
     * -> variableDeclaratorId ('=' variableInitializer)
     * */
    public ASTNode variableDeclarator(){
        ASTNode variableDeclaratorIdNode = variableDeclaratorId();
        matchEqualsChar();
        ASTNode variableInitializerNode = variableInitializer();

        // -> variableDeclaratorId ('=' variableInitializer)
        ASTNode variableDeclaratorNode = new ASTNode(ASTNodeTypeEnum.VARIABLE_DECLARATOR);
        variableDeclaratorNode
                .appendChildren(variableDeclaratorIdNode)
                .appendChildren(variableInitializerNode);

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
            // Identifier
            return new ASTNode(ASTNodeTypeEnum.Identifier,token.getValue());
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

    private void matchLeftBrace(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.LEFT_BRACE){
            throw new RuntimeException("语法分析错误 not match '{' :" + token);
        }
    }

    private void matchRightBrace(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.RIGHT_BRACE){
            throw new RuntimeException("语法分析错误 not match '}' :" + token);
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
