package lexan;

import lexan.model.Token;

import java.util.List;

public class LexicalAnalyzerDemo {

    public static void main(String[] args) {
        String sourceCode = "public static void main(String[] args)  \n" +
                "{int abc=131+(22 -   3) \t  /45; abc++; int _cc = 21;  int _ab_c_ = 23211; abc >= a}";

        System.out.println(sourceCode);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();
        System.out.println(tokenList);
    }

}
