package lexan.model;

import lexan.enums.TokenTypeEnum;

public class Token {

    private TokenTypeEnum tokenTypeEnum;
    private String value;

    public Token(TokenTypeEnum tokenTypeEnum, String value) {
        this.tokenTypeEnum = tokenTypeEnum;
        this.value = value;
    }

    public TokenTypeEnum getTokenTypeEnum() {
        return tokenTypeEnum;
    }

    public void setTokenTypeEnum(TokenTypeEnum tokenTypeEnum) {
        this.tokenTypeEnum = tokenTypeEnum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isWhiteSpaceToken(){
        return this.tokenTypeEnum == TokenTypeEnum.WHITE_SPACE;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenTypeEnum=" + tokenTypeEnum +
                ", value='" + value + '\'' +
                '}';
    }
}
