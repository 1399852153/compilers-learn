package lexan;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {

    private static final int ACCEPT = -1;
    private static final int ERROR = -2;

    /**
     * 状态表
     * */
    private static final Integer[][] STATE_TABLE =
        {
            {2,3,2,3,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // 数字
            {1,3,ERROR,3,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // 字母
            {4,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // "+"
            {5,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // "-"
            {6,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // ">"
            {7,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // "<"
            {10,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,8,9,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // "="
            {11,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // "{"
            {12,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // "}"
            {ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT,ACCEPT}, // "空白符（制表符、空格、换行"
        };

    private char[] sourceCode;
    private int currentIndex = 0;

    private int currentState = 0;

    public LexicalAnalyzer(String sourceCode) {
        this.sourceCode = sourceCode.toCharArray();
    }

    public static void main(String[] args) {
        String sourceCode = "pu";
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
            char ch = getNextChar();
            ACTION_ENUM action = getAction(ch);

            currentState = STATE_TABLE[action.getCode()][currentState];
            if(currentState == ERROR){
                // todo 识别换行符，增加报错行数的信息
                throw new RuntimeException("un expect char index=" + currentIndex);
            }

            currentIndex++;

            if(currentState == ACCEPT){
                // 接收一个token后，当前状态机state重置
                currentState = 0;
                // todo 类型、关键字过滤
                return tokenValue.toString();
            }

            tokenValue.append(ch);
        }

        throw new RuntimeException("not in here");
    }

    private boolean canParseNext(){
        return currentIndex < sourceCode.length;
    }

    private char getNextChar(){
        return sourceCode[currentIndex];
    }

    private static ACTION_ENUM getAction(char ch) {
        if(Character.isDigit(ch)){
            return ACTION_ENUM.DIGITAL;
        }

        if(Character.isLetter(ch)){
            return ACTION_ENUM.LETTER;
        }

        if(ch == '+'){
            return ACTION_ENUM.PLUS;
        }

        if(ch == '-'){
            return ACTION_ENUM.MINUS;
        }

        if(ch == '>'){
            return ACTION_ENUM.GREATER_THAN;
        }

        if(ch == '<'){
            return ACTION_ENUM.LITTLE_THAN;
        }

        if(ch == '='){
            return ACTION_ENUM.EQUALS;
        }

        if(ch == '{'){
            return ACTION_ENUM.LEFT_BRACE;
        }

        if(ch == '}'){
            return ACTION_ENUM.RIGHT_BRACE;
        }

        if(Character.isWhitespace(ch)){
            return ACTION_ENUM.WHITE_SPACE;
        }

        throw new RuntimeException("unmatched char=" + ch);
    }
}
