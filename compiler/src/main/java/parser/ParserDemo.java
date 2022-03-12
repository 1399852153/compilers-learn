package parser;

import lexan.LexicalAnalyzer;
import lexan.model.Token;
import parser.model.ASTNode;

import java.util.List;

public class ParserDemo {

    public static void main(String[] args) {
        String sourceCode = "{int a = 10; int b = 20; int c = a+b;}";

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();

        Parser parser = new Parser(new TokenReader(tokenList));
        ASTNode ASTTreeRoot = parser.parse();
    }

}
