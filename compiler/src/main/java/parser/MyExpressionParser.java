package parser;

import lexan.enums.TokenTypeEnum;
import lexan.model.Token;
import parser.enums.BinaryOpEnum;
import parser.model.ASTNode;

import java.util.Map;
import java.util.Stack;

public class MyExpressionParser {

    private TokenReader tokenReader;
    private MyParserMatchOneToken myParserMatchOneToken;
    private MyParser myParser;

    private Map<TokenTypeEnum,BinaryOpEnum> binaryOpEnumMap = BinaryOpEnum.toMap();

    public MyExpressionParser(TokenReader tokenReader,MyParser myParser) {
        this.tokenReader = tokenReader;
        this.myParserMatchOneToken = new MyParserMatchOneToken(tokenReader);
        this.myParser = new MyParser(tokenReader);
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
                return null;
            }

            BinaryOpEnum currentBinaryOpEnum = matchBinaryOp();
            boolean isHighPriorityLevel = comparePriorityLevel(binaryOpStack,currentBinaryOpEnum);

            if(isHighPriorityLevel){
                binaryOpStack.push(currentBinaryOpEnum);
            }else{

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
