package parser;

import lexan.LexicalAnalyzer;
import lexan.model.Token;
import parser.lisp.LispSimpleParser;
import parser.model.ASTNode;

import java.util.List;

public class MyParserDemo {

    public static void main(String[] args) {
        String sourceCode = "{int a = 10; int b= 20; ;}";
        System.out.println(sourceCode);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();

        MyParser myParser = new MyParser(new TokenReader(tokenList));
        ASTNode ASTTreeRoot = myParser.parse();
        ASTTreeRoot.printTree();
    }
}
