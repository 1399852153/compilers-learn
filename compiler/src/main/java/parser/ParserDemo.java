package parser;

import lexan.LexicalAnalyzer;
import lexan.model.Token;
import simple.ASTNode;

import java.util.List;

public class ParserDemo {

    public static void main(String[] args) {
        String sourceCode = "int a = 10; int b = 20; int c = a+b;";

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();

        Parser parser = new Parser();
        ASTNode ASTTreeRoot = parser.parse(tokenList);
    }

}
