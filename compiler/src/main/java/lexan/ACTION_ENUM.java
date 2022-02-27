package lexan;

public enum ACTION_ENUM {
    DIGITAL(0,"数字"),
    LETTER(1,"字母"),
    PLUS(2,"+"),
    MINUS(3,"-"),
    GREATER_THAN(4,">"),
    LITTLE_THAN(5,"<"),
    EQUALS(6,"="),
    LEFT_BRACE(7,"{"),
    RIGHT_BRACE(8,"}"),
    BLANK(9,"空白符(制表符、空格、换行)")
    ;

    private final int code;
    private final String message;

    ACTION_ENUM(int code, String message) {
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
