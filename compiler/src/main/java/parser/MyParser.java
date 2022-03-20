package parser;

import lexan.LexicalAnalyzer;
import lexan.enums.TokenTypeEnum;
import lexan.model.Token;
import parser.enums.ASTNodeTypeEnum;
import parser.model.ASTNode;
import parser.util.ParserUtil;

import java.util.List;

public class MyParser {

    private TokenReader tokenReader;
    private MyParserMatchOneToken myParserMatchOneToken;

    public MyParser(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
        this.myParserMatchOneToken = new MyParserMatchOneToken(tokenReader);
    }

    public ASTNode parse(){
        return block();
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
            return new ASTNode(ASTNodeTypeEnum.INT_LITERAL,token.getValue());
        }

        if(token.getTokenTypeEnum() == TokenTypeEnum.LEFT_PARENTHESES){
            // '( expression )'
            ASTNode expressionNode = expression();
            this.myParserMatchOneToken.matchRightParentheses();
            return expressionNode;
        }

        throw new RuntimeException("primary 语法分析错误：" + token);
    }

    /**
     * expression
     * -> assignmentExpression
     * -> lambdaExpression(暂不支持)
     * */
    public ASTNode expression(){
        // 只支持assignmentExpression
        return assignmentExpression();
        // throw new RuntimeException("expression 语法分析错误：" + token);
    }

    /**
     * assignmentExpression
     * 	-> additiveExpression(暂时只支持+ - * / 算数表达式)
     * 	-> assignment
     * */
    public ASTNode assignmentExpression(){
        // 预读两个token
        Token next1Token = tokenReader.peekToken();
        Token next2Token = tokenReader.peekToken(1);

        // 预读两个token
        if(next1Token.getTokenTypeEnum() == TokenTypeEnum.IDENTIFIER
            && next2Token.getTokenTypeEnum() == TokenTypeEnum.ASSIGNMENT){
            // -> assignment( ID = EXP)
            return assignment();
        }else{
            // 	-> additiveExpression
            return additiveExpression();
        }
    }

    public static void main(String[] args) {
        String sourceCode = "2*3*5*7*9*10 A";
        System.out.println(sourceCode);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();

        MyParser myParser = new MyParser(new TokenReader(tokenList));
        ASTNode ASTTreeRoot = myParser.additiveExpression();
        ASTTreeRoot.printTree();
    }

    public ASTNode additiveExpression(){
        ASTNode op1Node = multiplicativeExpression();

        // 基于加法运算的左结合性，靠右的节点需要更加靠近树根，更加上层，才能保证下层靠左的节点先求值
        ASTNode upNode = op1Node;
        while(true){
            Token nextToken = tokenReader.peekToken();
            if(nextToken.getTokenTypeEnum() == TokenTypeEnum.PLUS ||
                nextToken.getTokenTypeEnum() == TokenTypeEnum.MINUS){
                Token opToken = tokenReader.readToken();
                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.CALCULATE_OP,opToken.getValue());
                ASTNode op2Node = multiplicativeExpression();
                ASTNode addNode = new ASTNode(ASTNodeTypeEnum.ADDITIVE_EXPRESSION)
                    .appendChildren(upNode).appendChildren(opNode).appendChildren(op2Node);
                upNode = addNode;
            }else{
                break;
            }
        }

        return upNode;
    }

    public ASTNode multiplicativeExpression(){
        ASTNode op1Node = primary();

        // 基于乘法运算的左结合性，靠右的节点需要更加靠近树根，更加上层，才能保证下层靠左的节点先求值
        ASTNode upNode = op1Node;
        while(true){
            Token nextToken = tokenReader.peekToken();
            if(nextToken.getTokenTypeEnum() == TokenTypeEnum.MULTI ||
                nextToken.getTokenTypeEnum() == TokenTypeEnum.DIVISION){
                Token opToken = tokenReader.readToken();
                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.CALCULATE_OP,opToken.getValue());
                ASTNode op2Node = primary();
                ASTNode addNode = new ASTNode(ASTNodeTypeEnum.MULTIPLICATIVE_EXPRESSION)
                    .appendChildren(upNode).appendChildren(opNode).appendChildren(op2Node);
                upNode = addNode;
            }else{
                break;
            }
        }

        return upNode;
    }

    /**
     * assignment
     * 	-> leftHandSide assignmentOperator expression
     * */
    public ASTNode assignment(){
        ASTNode leftHandSideNode = leftHandSide();
        ASTNode assignmentOperatorNode = assignmentOperator();
        ASTNode expressionNode = expression();

        ASTNode assignmentNode = new ASTNode(ASTNodeTypeEnum.ASSIGNMENT);
        assignmentNode.appendChildren(leftHandSideNode)
                .appendChildren(assignmentOperatorNode)
                .appendChildren(expressionNode);
        return assignmentNode;
    }

    /**
     * leftHandSide
     * 	:	expressionName
     * 	|	fieldAccess(暂不支持)
     * 	|	arrayAccess(暂不支持)
     * */
    public ASTNode leftHandSide(){
        return expressionName();
    }

    /**
     * expressionName
     * 	:	Identifier
     * 	|	ambiguousName '.' Identifier(暂不支持)
     * */
    public ASTNode expressionName(){
        Token token = tokenReader.readToken();

        if(token.getTokenTypeEnum() == TokenTypeEnum.IDENTIFIER){
            return new ASTNode(ASTNodeTypeEnum.Identifier,token.getValue());
        }

        throw new RuntimeException("expressionName match error: " + token);
    }

    public ASTNode assignmentOperator(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() == TokenTypeEnum.ASSIGNMENT){
            return new ASTNode(ASTNodeTypeEnum.ASSIGNMENT_OPERATOR,token.getValue());
        }

        throw new RuntimeException("assignmentOperator match error: " + token);
    }

    /**
     * block
     * -> '{' blockStatements '}'
     * */
    public ASTNode block(){
        // -> '{' blockStatements '}'
        this.myParserMatchOneToken.matchLeftBrace();
        ASTNode blockStatementsNode = blockStatements();
        this.myParserMatchOneToken.matchRightBrace();

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
            this.myParserMatchOneToken.matchSemicolon();

            return variableDeclaratorsNode;
        }

        // -> statement
        return statement();

    }

    /**
     * statement
     * -> SEMI(;)
     * */
    public ASTNode statement(){
        Token token = tokenReader.peekToken();
        if(token.getTokenTypeEnum() == TokenTypeEnum.SEMICOLON){
            // ; 空语句
            Token semicolonToken= this.myParserMatchOneToken.matchSemicolon();
            return new ASTNode(ASTNodeTypeEnum.EMPTY_STATEMENT,semicolonToken.getValue());
        }

        throw new RuntimeException("statement match error :" + token);
    }

    /**
     * primitiveType
     * -> INT
     * */
    public ASTNode primitiveType(){
        Token token = tokenReader.readToken();
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
        this.myParserMatchOneToken.matchEqualsChar();
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
            return new ASTNode(ASTNodeTypeEnum.VARIABLE_DECLARATOR_ID,token.getValue());
        }

        throw new RuntimeException("variableDeclaratorId need IDENTIFIER 语法分析错误:" + token);
    }
}
