package lexan;

import lexan.enums.ActionEnum;
import lexan.enums.TokenTypeEnum;
import util.TokenTypeUtil;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {

    private static final int OTHER = -1;
    private static final int ERROR = -2;

    /**
     * 状态表
     * */
    private static final Integer[][] STATE_TABLE =
        {
            {1,1,3,3,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // 数字
            {2,ERROR,3,3,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // 字母
            {4,OTHER,OTHER,OTHER,5,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "+"
            {6,OTHER,OTHER,OTHER,OTHER,OTHER,7,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "-"
            {8,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "*"
            {9,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "/"
            {10,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,11,OTHER,13,OTHER,15,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "="
            {12,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // ">"
            {14,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "<"
            {16,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "{"
            {17,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "}"
            {18,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "["
            {19,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "]"
            {20,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "("
            {21,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // ")"
            {22,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // ","
            {23,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // ";"
            {24,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "."
            {25,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,25}, // "空白符"
        };

    private char[] sourceCode;
    private int currentIndex = -1;

    private int currentState = 0;

    public LexicalAnalyzer(String sourceCode) {
        this.sourceCode = sourceCode.toCharArray();
    }

    public static void main(String[] args) {
        String sourceCode = "public static void main(String[] args)  \n" +
                "{int abc=131+(22 -   3) \t  /45; abc++;}";
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<Token> tokenList = lexicalAnalyzer.parseToken();
        System.out.println(tokenList);
    }

    public List<Token> parseToken(){
        List<Token> tokenList = new ArrayList<>();
        while(canParseNext()){
            Token token = getToken();
            if(!token.isWhiteSpaceToken()){
                System.out.println(token);
                tokenList.add(token);
            }
        }

        return tokenList;
    }

    public Token getToken(){
        StringBuilder tokenValue = new StringBuilder();

        while(canParseNext()){
            char nextCh = getNextChar();
            ActionEnum nextAction = getAction(nextCh);

            int nextState = STATE_TABLE[nextAction.getCode()][currentState];
            if(nextState == ERROR){
                // todo 识别换行符，增加报错行数的信息
                throw new RuntimeException("un expect char index=" + currentIndex);
            }

            if(nextState == OTHER){
                TokenTypeEnum tokenTypeEnum = TokenTypeUtil.getByState(currentState);
                // 接收一个token后，当前状态机state重置
                currentState = 0;
                // todo 关键字过滤
                return new Token(tokenTypeEnum,tokenValue.toString());
            }else{
                currentIndex++;
                tokenValue.append(nextCh);
                currentState = nextState;
            }
        }

        TokenTypeEnum tokenTypeEnum = TokenTypeUtil.getByState(currentState);
        return new Token(tokenTypeEnum,tokenValue.toString());
    }

    private boolean canParseNext(){
        return currentIndex < (sourceCode.length-1);
    }

    private char getCurrentChar(){
        return sourceCode[currentIndex];
    }

    private char getNextChar(){
        return sourceCode[currentIndex+1];
    }

    private static ActionEnum getAction(char ch) {
        if(Character.isDigit(ch)){
            return ActionEnum.DIGITAL;
        }

        if(Character.isLetter(ch)){
            return ActionEnum.LETTER;
        }

        if(Character.isWhitespace(ch)){
            return ActionEnum.WHITE_SPACE;
        }

        switch (ch){
            case '+': return ActionEnum.PLUS;
            case '-': return ActionEnum.MINUS;
            case '*': return ActionEnum.MULTI;
            case '/': return ActionEnum.DIVISION;
            case '=': return ActionEnum.EQUALS;

            case '>': return ActionEnum.GREATER_THAN;
            case '<': return ActionEnum.LITTLE_THAN;

            case '{': return ActionEnum.LEFT_BRACE;
            case '}': return ActionEnum.RIGHT_BRACE;

            case '[': return ActionEnum.LEFT_BRACKETS;
            case ']': return ActionEnum.RIGHT_BRACKETS;

            case '(': return ActionEnum.LEFT_PARENTHESES;
            case ')': return ActionEnum.RIGHT_PARENTHESES;

            case ',': return ActionEnum.COMMA;
            case ';': return ActionEnum.SEMICOLON;
            case '.': return ActionEnum.DOT;

            default:
                throw new RuntimeException("unmatched char=" + ch);

        }
    }
}
