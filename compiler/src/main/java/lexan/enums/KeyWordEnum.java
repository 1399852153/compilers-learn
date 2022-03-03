package lexan.enums;

public enum KeyWordEnum {

    PUBLIC("public"),
    PRIVATE("private"),
    STATIC("static"),
    VOID("void"),
    INT("int"),
    CHAR("char"),
    BOOLEAN("boolean"),
    FINAL("final"),
    ENUM("enum"),
    THIS("this"),
    WHILE("while"),
    FOR("for"),
    RETURN("return"),
    IMPORT("import"),
    IF("if"),
    ELSE("else"),
    SWITCH("switch"),
    CASE("case"),
    NULL("null"),
    NEW("new"),
    CLASS("class"),

    ;


    private final String code;


    KeyWordEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
