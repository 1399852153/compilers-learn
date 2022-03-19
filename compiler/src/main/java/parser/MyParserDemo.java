package parser;

import lexan.LexicalAnalyzer;
import lexan.model.Token;
import parser.model.ASTNode;

import java.util.List;

public class MyParserDemo {

    public static void main(String[] args) {
        // 注意：由于只支持整型，表达式内如果无法整除会有问题
        String sourceCode = "8+2*3+10/5 T";
        System.out.println(sourceCode);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();

        MyExpressionParser myParser = new MyExpressionParser(new TokenReader(tokenList));
        ASTNode treeNode = myParser.parseExpression();
        treeNode.printTree();

        MyExpressionInterpreter myExpressionInterpreter = new MyExpressionInterpreter(treeNode);
        Object result = myExpressionInterpreter.interpret();
        System.out.println(result);
    }
}
