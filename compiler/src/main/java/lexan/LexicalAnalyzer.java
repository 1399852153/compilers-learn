package lexan;

import util.ArrayUtil;

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
            {1,1,3,3,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // 数字
            {2,ERROR,3,3,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // 字母
            {4,OTHER,OTHER,OTHER,5,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "+"
            {6,OTHER,OTHER,OTHER,OTHER,OTHER,7,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "-"
            {8,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "*"
            {9,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "/"
            {10,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,11,OTHER,13,OTHER,15,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "="
            {12,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // ">"
            {14,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "<"
            {16,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "{"
            {17,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "}"
            {18,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "["
            {19,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "]"
            {20,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "("
            {21,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // ")"
            {22,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // ","
            {23,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // ";"
            {24,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER,OTHER}, // "."
        };

    private char[] sourceCode;
    private int currentIndex = -1;

    private int currentState = 0;

    public LexicalAnalyzer(String sourceCode) {
        this.sourceCode = sourceCode.toCharArray();
    }

    public static void main(String[] args) {
        String sourceCode = "public static void main(String[] args) {int abc=131+(22 -3) /45;}";
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        List<String> tokenList = lexicalAnalyzer.parseToken();
        System.out.println(tokenList);
    }

    public List<String> parseToken(){
        List<String> tokenList = new ArrayList<>();
        while(canParseNext()){
            String token = getToken();
            System.out.println(token);
            tokenList.add(token);
        }

        return tokenList;
    }

    public String getToken(){
        StringBuilder tokenValue = new StringBuilder();

        while(canParseNext()){
            char nextCh = getNextChar();
            ACTION_ENUM nextAction = getAction(nextCh);

            int nextState = STATE_TABLE[nextAction.getCode()][currentState];
            if(nextState == ERROR){
                // todo 识别换行符，增加报错行数的信息
                throw new RuntimeException("un expect char index=" + currentIndex);
            }

            if(nextState == OTHER){
                // 接收一个token后，当前状态机state重置
                currentState = 0;
                // todo 类型、关键字过滤
                return tokenValue.toString();
            }else{
                currentIndex++;
                tokenValue.append(nextCh);
                currentState = nextState;
            }
        }

        return tokenValue.toString();
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

    private static ACTION_ENUM getAction(char ch) {
        if(Character.isDigit(ch)){
            return ACTION_ENUM.DIGITAL;
        }

        if(Character.isLetter(ch)){
            return ACTION_ENUM.LETTER;
        }

        if(Character.isWhitespace(ch)){
            return ACTION_ENUM.WHITE_SPACE;
        }

        switch (ch){
            case '+': return ACTION_ENUM.PLUS;
            case '-': return ACTION_ENUM.MINUS;
            case '*': return ACTION_ENUM.MULTI;
            case '/': return ACTION_ENUM.DIVISION;
            case '=': return ACTION_ENUM.EQUALS;

            case '>': return ACTION_ENUM.GREATER_THAN;
            case '<': return ACTION_ENUM.LITTLE_THAN;

            case '{': return ACTION_ENUM.LEFT_BRACE;
            case '}': return ACTION_ENUM.RIGHT_BRACE;

            case '[': return ACTION_ENUM.LEFT_BRACKETS;
            case ']': return ACTION_ENUM.RIGHT_BRACKETS;

            case '(': return ACTION_ENUM.LEFT_PARENTHESES;
            case ')': return ACTION_ENUM.RIGHT_PARENTHESES;

            case ',': return ACTION_ENUM.COMMA;
            case ';': return ACTION_ENUM.SEMICOLON;
            case '.': return ACTION_ENUM.DOT;

            default:
                throw new RuntimeException("unmatched char=" + ch);

        }
    }
}
