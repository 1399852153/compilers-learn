package parser;

import com.sun.org.apache.regexp.internal.RE;
import lexan.LexicalAnalyzer;
import lexan.enums.TokenTypeEnum;
import lexan.model.Token;
import parser.enums.ASTNodeTypeEnum;
import parser.enums.BinaryOpEnum;
import parser.model.ASTNode;
import sun.dc.pr.PRError;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MyExpressionParser {

    private TokenReader tokenReader;
    private MyParserMatchOneToken myParserMatchOneToken;
    private MyParser myParser;

    private Map<TokenTypeEnum,BinaryOpEnum> binaryOpEnumMap = BinaryOpEnum.toMap();

    public MyExpressionParser(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
        this.myParserMatchOneToken = new MyParserMatchOneToken(tokenReader);
        this.myParser = new MyParser(tokenReader);
    }

    /**
     * 二元表达式解析（参考java的实现，LR(1)解析）
     * */
    public ASTNode parseBinaryExpression(){
        Stack<ASTNode> opNumStack = new Stack<>();
        Stack<BinaryOpEnum> binaryOpStack = new Stack<>();

        ASTNode opNumNode = myParser.primary();
        opNumStack.push(opNumNode);

        while(true) {
            Token token = tokenReader.peekToken();
            if (!binaryOpEnumMap.containsKey(token.getTokenTypeEnum())) {
                // 发现不是二元操作符后退出
                return mergeRemainingNode(opNumStack,binaryOpStack);
            }

            BinaryOpEnum currentBinaryOpEnum = matchBinaryOp();
            boolean isHighPriorityLevel = comparePriorityLevel(binaryOpStack,currentBinaryOpEnum);

            if(isHighPriorityLevel){
                // 当前二元符号优先级高于栈顶符号
                // 将当前符号和下一个操作数都压入栈
                binaryOpStack.push(currentBinaryOpEnum);
                ASTNode opNumNode2 = myParser.primary();
                opNumStack.push(opNumNode2);
            }else{
                do{
                    // 当前二元符号优先级低于栈顶符号
                    // 弹出当前栈顶头两个操作数，将其与操作符号共同组成一个mergeNode
                    mergeBinaryExpNode(opNumStack, binaryOpStack, currentBinaryOpEnum);
                    // merge后，若当前操作符的优先级依然低于栈顶操作符，则再进行规约，循环往复
                }while(!comparePriorityLevel(binaryOpStack,currentBinaryOpEnum));

                // 随后将当前低优先级的操作符压入操作符栈中,下一个操作数压入操作数栈中
                ASTNode nextOpNum = myParser.primary();
                opNumStack.push(nextOpNum);
                binaryOpStack.push(currentBinaryOpEnum);
            }
        }
    }

    /**
     * 前二元符号优先级低于栈顶符号
     * 弹出当前栈顶头两个操作数，将其与操作符号共同组成一个mergeNode
     * */
    private void mergeBinaryExpNode(Stack<ASTNode> opNumStack, Stack<BinaryOpEnum> binaryOpStack, BinaryOpEnum currentBinaryOpEnum){
        ASTNode opNumNode1 = opNumStack.pop();
        ASTNode opNumNode2 = opNumStack.pop();
        // 先弹出栈顶高优先级的操作符，用于生成mergeNode
        BinaryOpEnum topOp = binaryOpStack.pop();

        ASTNode opNode = new ASTNode(ASTNodeTypeEnum.CALCULATE_OP,topOp.getTokenTypeEnum().getMessage());
        ASTNode mergeNode = new ASTNode(topOp.getAstNodeTypeEnum());
        // 由于入栈是先进后出；因此后出栈的是左孩子节点；先出栈的是右孩子节点
        mergeNode.appendChildren(opNumNode2)
                .appendChildren(opNode)
                .appendChildren(opNumNode1);

        opNumStack.push(mergeNode);
    }

    private BinaryOpEnum matchBinaryOp(){
        Token token = tokenReader.readToken();
        BinaryOpEnum binaryOpEnum = binaryOpEnumMap.get(token.getTokenTypeEnum());
        if(binaryOpEnum == null){
            throw new RuntimeException("not binary op token=" + token);
        }

        return binaryOpEnum;
    }

    /**
     * 比较栈顶操作符与当前操作符的优先级
     * @return true 当前操作符 > 栈顶操作符
     *         false 当前操作符 < 当前操作符
     * */
    private boolean comparePriorityLevel(Stack<BinaryOpEnum> binaryOpStack, BinaryOpEnum binaryOpEnum){
        if(binaryOpStack.isEmpty()){
            return true;
        }

        BinaryOpEnum topOp = binaryOpStack.peek();
        if(topOp.getPriorityLevel() == binaryOpEnum.getPriorityLevel()){
            // 优先级相等时，取决于结合性（左结合性时，栈顶优先级更高/右结合性，当前运算符优先级更低）
            return !topOp.isLeftAssociativity();
        }
        return topOp.getPriorityLevel() < binaryOpEnum.getPriorityLevel();
    }

    /**
     * 将整个栈中遗留的所有ASTNode按照顺序弹出，组装成一个AST返回
     * */
    private ASTNode mergeRemainingNode(Stack<ASTNode> opNumStack,Stack<BinaryOpEnum> binaryOpStack){
        if(binaryOpStack.isEmpty()){
            // 特殊情况，操作符栈一进来就是空的，则操作数栈必须只存在一个opNumNode，此时直接弹出返回即可（可能存在吗？）
            ASTNode fastReturn = opNumStack.pop();
            if(!opNumStack.isEmpty()){
                throw new RuntimeException("opNumStack must empty！！！");
            }
            return fastReturn;
        }

        ASTNode mergeNode = null;
        while(!binaryOpStack.isEmpty()){
            if(mergeNode == null){
                // 先出栈的属于二维表达式的右操作数
                ASTNode opNum2 = opNumStack.pop();
                // 后出栈的属于二维表达式的左操作数
                ASTNode opNum1 = opNumStack.pop();
                BinaryOpEnum topOp = binaryOpStack.pop();
                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.CALCULATE_OP,topOp.getTokenTypeEnum().getMessage());
                mergeNode = new ASTNode(topOp.getAstNodeTypeEnum());
                mergeNode.appendChildren(opNum1)
                        .appendChildren(opNode)
                        .appendChildren(opNum2);
            }else{
                ASTNode opNum1 = opNumStack.pop();
                BinaryOpEnum topOp = binaryOpStack.pop();
                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.CALCULATE_OP,topOp.getTokenTypeEnum().getMessage());
                ASTNode newMergeNode = new ASTNode(topOp.getAstNodeTypeEnum());
                newMergeNode.appendChildren(opNum1)
                        .appendChildren(opNode)
                        .appendChildren(mergeNode);
                mergeNode = newMergeNode;
            }
        }

        if(!opNumStack.isEmpty()){
            throw new RuntimeException("opNumStack must empty！！！");
        }
        return mergeNode;
    }


    public static void main(String[] args) {
        // 注意：由于只支持整型，表达式内如果无法整除会有问题
        String sourceCode = "-++a T";
        System.out.println(sourceCode);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();

        MyExpressionParser myParser = new MyExpressionParser(new TokenReader(tokenList));
        ASTNode treeNode = myParser.parseUnaryExpression();
        treeNode.printTree();

        int a=2;
        int b=-++a;
        System.out.println(b);
    }

    /**
     * 一元表达式解析
     * unaryExpression
     * -> '++' unaryExpression
     * -> '--' unaryExpression
     * -> '+' unaryExpression
     * -> '-' unaryExpression
     * -> postfixExpression
     * */
    public ASTNode parseUnaryExpression(){
        Token token = tokenReader.peekToken();
        switch (token.getTokenTypeEnum()){
            case DOUBLE_PLUS: {
                // '++' unaryExpression(preIncrementExpression)
                ASTNode pNode = new ASTNode(ASTNodeTypeEnum.UNARY_EXPRESSION);
                Token plusToken = myParserMatchOneToken.matchDoublePlus();
                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.UNARY_EXP_OP, plusToken.getValue());
                ASTNode unaryExpNode = parseUnaryExpression();
                pNode.appendChildren(opNode).appendChildren(unaryExpNode);
                return pNode;
            }
            case DOUBLE_MINUS: {
                // '--' unaryExpression(preDecrementExpression)
                ASTNode pNode = new ASTNode(ASTNodeTypeEnum.UNARY_EXPRESSION);
                Token plusToken = myParserMatchOneToken.matchDoubleMinus();
                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.UNARY_EXP_OP, plusToken.getValue());
                ASTNode unaryExpNode = parseUnaryExpression();
                pNode.appendChildren(opNode).appendChildren(unaryExpNode);
                return pNode;
            }
            case PLUS: {
                // '+' unaryExpression
                ASTNode pNode = new ASTNode(ASTNodeTypeEnum.UNARY_EXPRESSION);
                Token plusToken = myParserMatchOneToken.matchPlus();
                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.UNARY_EXP_OP, plusToken.getValue());
                ASTNode unaryExpNode = parseUnaryExpression();
                pNode.appendChildren(opNode).appendChildren(unaryExpNode);
                return pNode;
            }
            case MINUS: {
                // '-' unaryExpression
                ASTNode pNode = new ASTNode(ASTNodeTypeEnum.UNARY_EXPRESSION);
                Token plusToken = myParserMatchOneToken.matchMinus();
                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.UNARY_EXP_OP, plusToken.getValue());
                ASTNode unaryExpNode = parseUnaryExpression();
                pNode.appendChildren(opNode).appendChildren(unaryExpNode);
                return pNode;
            }
            case IDENTIFIER:
            case LITERAL: {
                return postfixExpression();
            }
            default:
                throw new RuntimeException("parseUnaryExpression not match");
        }
    }

    /**
     * postfixExpression
     * -> primary ('++' | '--')*
     * */
    private ASTNode postfixExpression(){
        ASTNode primaryNode = myParser.primary();

        ASTNode postfixExpNode = new ASTNode(ASTNodeTypeEnum.POSTFIX_EXPRESSION);
        postfixExpNode.appendChildren(primaryNode);
        // LL1预测（如果是’++‘或’--‘则获取，否则结束当前节点的解析）
        while(tokenReader.peekToken().getTokenTypeEnum() == TokenTypeEnum.DOUBLE_PLUS ||
                tokenReader.peekToken().getTokenTypeEnum() == TokenTypeEnum.DOUBLE_PLUS){
            Token opToken = tokenReader.readToken();
            ASTNode opNode = new ASTNode(ASTNodeTypeEnum.UNARY_EXP_OP, opToken.getValue());
            postfixExpNode.appendChildren(opNode);
        }

        return postfixExpNode;
    }
}
