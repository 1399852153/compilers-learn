package parser;

import com.sun.org.apache.regexp.internal.RE;
import lexan.LexicalAnalyzer;
import lexan.enums.TokenTypeEnum;
import lexan.model.Token;
import parser.enums.ASTNodeTypeEnum;
import parser.enums.BinaryOpEnum;
import parser.model.ASTNode;

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

    public static void main(String[] args) {
        String sourceCode = "1*2+3*4+5 T";
        System.out.println(sourceCode);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();

        MyExpressionParser myParser = new MyExpressionParser(new TokenReader(tokenList));
        ASTNode treeNode = myParser.parseExpression();
        treeNode.printTree();

//        String sourceCode2 = "1+2*3 T";
//        System.out.println(sourceCode2);
//        LexicalAnalyzer lexicalAnalyzer2 = new LexicalAnalyzer(sourceCode2);
//        List<Token> tokenList2 = lexicalAnalyzer2.parseToken();
//
//        MyExpressionParser myParser2 = new MyExpressionParser(new TokenReader(tokenList2));
//        ASTNode treeNode2 = myParser2.parseExpression();
//        treeNode2.printTree();

    }

    public ASTNode parseExpression(){
        Stack<ASTNode> opNumStack = new Stack<>();
        Stack<BinaryOpEnum> binaryOpStack = new Stack<>();

        ASTNode opNumNode = myParser.primary();
        opNumStack.push(opNumNode);

        while(true) {
            Token token = tokenReader.peekToken();
            if (!binaryOpEnumMap.containsKey(token.getTokenTypeEnum())) {
                // 发现不是二元操作符后退出
                // 将整个栈中遗留的所有ASTNode按照顺序弹出，组装成一个AST返回
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
                // 当前二元符号优先级低于栈顶符号
                // 弹出当前栈顶头两个操作数，将其与操作符号共同组成一个mergeNode
                ASTNode opNumNode1 = opNumStack.pop();
                ASTNode opNumNode2 = opNumStack.pop();
                // 先弹出栈顶高优先级的操作符，用于生成mergeNode
                BinaryOpEnum topOp = binaryOpStack.pop();
                // 随后将当前低优先级的操作符压入操作符栈中
                binaryOpStack.push(currentBinaryOpEnum);

                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.CALCULATE_OP,topOp.getTokenTypeEnum().getMessage());
                ASTNode mergeNode = new ASTNode(topOp.getAstNodeTypeEnum());
                // 由于入栈是先进后出；因此后出栈的是左孩子节点；先出栈的是右孩子节点
                mergeNode.appendChildren(opNumNode2)
                    .appendChildren(opNode)
                    .appendChildren(opNumNode1);

                opNumStack.push(mergeNode);
                ASTNode nextOpNum = myParser.primary();
                // 和下一个操作数
                opNumStack.push(nextOpNum);
            }
        }
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
        ASTNode mergeNode = opNumStack.pop();
        // 不断迭代，直到操作符栈为空
        // 注意：特殊情况，操作符栈一进来就是空的，则操作数栈必须只存在一个opNumNode，此时直接弹出返回即可
        while(!binaryOpStack.isEmpty()){
            ASTNode opNum1 = opNumStack.pop();
            BinaryOpEnum topOp = binaryOpStack.pop();
            ASTNode opNode = new ASTNode(ASTNodeTypeEnum.CALCULATE_OP,topOp.getTokenTypeEnum().getMessage());

            ASTNode newMergeNode = new ASTNode(topOp.getAstNodeTypeEnum());
            newMergeNode.appendChildren(opNum1)
                    .appendChildren(opNode)
                    .appendChildren(mergeNode);
            mergeNode = newMergeNode;
        }

        if(!opNumStack.isEmpty()){
            throw new RuntimeException("opNumStack must empty！！！");
        }
        return mergeNode;
    }
}
