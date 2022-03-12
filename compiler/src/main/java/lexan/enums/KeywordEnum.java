package lexan.enums;

public enum KeywordEnum {

    PUBLIC("public"),
    PRIVATE("private"),
    STATIC("static"),
    VOID("void"),
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

    /**
     * 基础类型声明关键字
     * */
    INT("int",true),
    CHAR("char",true),
    BOOLEAN("boolean",true),
    ;


    private final String code;
    private final boolean isTypeDefine;

    KeywordEnum(String code) {
        this.code = code;
        this.isTypeDefine = false;
    }

    KeywordEnum(String code, boolean isTypeDefine) {
        this.code = code;
        this.isTypeDefine = isTypeDefine;
    }

    public String getCode() {
        return code;
    }

    public boolean getTypeDefine() {
        return isTypeDefine;
    }

    public static KeywordEnum getByCode(String code){
        for(KeywordEnum keyWordEnum : KeywordEnum.values()){
            if(keyWordEnum.code.equals(code)){
                return keyWordEnum;
            }
        }
        throw new RuntimeException("not match keyword=" + code);
    }
}
