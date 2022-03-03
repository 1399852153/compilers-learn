package lexan.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TokenTypeEnum {
    KEY_WORD("关键字",new ArrayList<>()),
    PURE_DIGITAL("纯数字", Arrays.asList(1)),
    IDENTIFIER("标识符", Arrays.asList(2,3)),
    PLUS("+",Arrays.asList(4)),
    DOUBLE_PLUS("++",Arrays.asList(5)),
    MINUS("-",Arrays.asList(6)),
    DOUBLE_MINUS("--",Arrays.asList(7)),
    MULTI("*",Arrays.asList(8)),
    DIVISION("/",Arrays.asList(9)),
    EQUALS("=",Arrays.asList(10)),
    DOUBLE_EQUALS("==",Arrays.asList(11)),
    GREATER_THAN(">",Arrays.asList(12)),
    GREATER_THAN_OR_EQUALS(">=",Arrays.asList(13)),
    LITTLE_THAN("<",Arrays.asList(14)),
    LITTLE_THAN_OR_EQUALS("<=",Arrays.asList(15)),
    LEFT_BRACE("{",Arrays.asList(16)),
    RIGHT_BRACE("}",Arrays.asList(17)),
    LEFT_BRACKETS("[",Arrays.asList(18)),
    RIGHT_BRACKETS("]",Arrays.asList(19)),
    LEFT_PARENTHESES("(",Arrays.asList(20)),
    RIGHT_PARENTHESES(")",Arrays.asList(21)),
    COMMA(",",Arrays.asList(22)),
    SEMICOLON(";",Arrays.asList(23)),
    DOT(".",Arrays.asList(24)),
    ;


    TokenTypeEnum(String message, List<Integer> matchedStates) {
        this.message = message;
        this.matchedStates = matchedStates;
    }
    private final String message;
    private final List<Integer> matchedStates;

    public String getMessage() {
        return message;
    }

    public List<Integer> getMatchedStates() {
        return matchedStates;
    }
}
