package lexan.enums;

public enum ActionEnum {
    DIGITAL(0,"数字"),
    LETTER(1,"字母"),
    PLUS(2,"+"),
    MINUS(3,"-"),
    MULTI(4,"*"),
    DIVISION(5,"/"),
    EQUALS(6,"="),
    GREATER_THAN(7,">"),
    LITTLE_THAN(8,"<"),
    LEFT_BRACE(9,"{"),
    RIGHT_BRACE(10,"}"),
    LEFT_BRACKETS(11,"["),
    RIGHT_BRACKETS(12,"]"),
    LEFT_PARENTHESES(13,"("),
    RIGHT_PARENTHESES(14,")"),
    COMMA(15,","),
    SEMICOLON(16,";"),
    DOT(17,"."),
    WHITE_SPACE(18,"空白符(制表符、空格、换行)")
    ;

    private final int code;
    private final String message;

    ActionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
