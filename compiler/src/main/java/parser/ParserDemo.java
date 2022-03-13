package parser;

import lexan.LexicalAnalyzer;
import lexan.model.Token;
import parser.model.ASTNode;

import java.util.List;

public class ParserDemo {

    public static void main(String[] args) {
//        String sourceCode = "1";
//        String sourceCode = "(1)";

//        String sourceCode = "(* 12 (+ 21 31))";

        String sourceCode = "(* (+ 1 2) (+ 3 4))";


        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();

        LispSimpleParser lispSimpleParser = new LispSimpleParser(new TokenReader(tokenList));
        ASTNode ASTTreeRoot = lispSimpleParser.parse();
        ASTTreeRoot.printTree();

        LispSimpleInterpreter lispSimpleInterpreter = new LispSimpleInterpreter(ASTTreeRoot);
        Object result = lispSimpleInterpreter.interpret();
        System.out.println(result);
    }

}
