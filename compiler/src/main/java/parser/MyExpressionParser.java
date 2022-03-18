package parser;

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
        String sourceCode = "1*2+3 T";
        System.out.println(sourceCode);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();

        MyExpressionParser myParser = new MyExpressionParser(new TokenReader(tokenList));
        ASTNode treeNode = myParser.parseExpression();
        treeNode.printTree();
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
                // todo 此时需要将整个栈中的所有ASTNode按照顺序弹出，组装成一个局部AST返回
                return opNumStack.pop();
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
                // 弹出当前栈顶操作数，将其与操作符号和下一个操作数共同组成一个mergeNode后
                ASTNode opNumNode1 = opNumStack.pop();
                // 先弹出栈顶高优先级的操作符，用于生成mergeNode
                BinaryOpEnum topOp = binaryOpStack.pop();
                // 随后将当前低优先级的操作符压入操作符栈中
                binaryOpStack.push(currentBinaryOpEnum);

                ASTNode opNode = new ASTNode(ASTNodeTypeEnum.CALCULATE_OP,topOp.getTokenTypeEnum().getMessage());
                ASTNode opNumNode2 = myParser.primary();
                ASTNode mergeNode = new ASTNode(topOp.getAstNodeTypeEnum());
                mergeNode.appendChildren(opNumNode1)
                    .appendChildren(opNode)
                    .appendChildren(opNumNode2);

                opNumStack.push(mergeNode);
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
            // 优先级相等时，取决于结合性（左结合性时，栈顶优先级更高/右结合性，当前运算符优先级更高）
            return topOp.isLeftAssociativity();
        }
        return topOp.getPriorityLevel() < binaryOpEnum.getPriorityLevel();
    }
}
