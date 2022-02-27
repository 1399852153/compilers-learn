package lexan;

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

    private static char[] sourceCode = "".toCharArray();
    private static int currentIndex = 0;

    private static int currentState = 0;

    public String getToken(){
        StringBuilder tokenValue = new StringBuilder();

        while(true){
            char ch = getNextChar();
            ACTION_ENUM action = getAction(ch);

            currentState = STATE_TABLE[action.getCode()][currentState];
            if(currentState == ERROR){
                // todo 识别换行符，增加报错行数的信息
                throw new RuntimeException("un expect char index=" + currentIndex);
            }

            currentIndex++;
            tokenValue.append(ch);

            if(currentState == ACCEPT){
                // todo 类型、关键字过滤
                return tokenValue.toString();
            }
        }

    }

    private static char getNextChar(){
        return sourceCode[currentIndex];
    }

    private static ACTION_ENUM getAction(char ch) {
        // todo 根据字符决定动作
        return ACTION_ENUM.BLANK;
    }
}
