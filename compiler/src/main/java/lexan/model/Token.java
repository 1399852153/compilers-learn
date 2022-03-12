package lexan.model;

import lexan.enums.KeywordEnum;
import lexan.enums.TokenTypeEnum;

public class Token {

    private TokenTypeEnum tokenTypeEnum;
    private String value;
    private KeywordEnum keywordEnum;

    public Token(TokenTypeEnum tokenTypeEnum, String value) {
        this.tokenTypeEnum = tokenTypeEnum;
        this.value = value;

        if(tokenTypeEnum == TokenTypeEnum.KEY_WORD){
            this.keywordEnum = KeywordEnum.getByCode(value);
        }
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

    public KeywordEnum getKeywordEnum() {
        return keywordEnum;
    }

    public void setKeywordEnum(KeywordEnum keywordEnum) {
        this.keywordEnum = keywordEnum;
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
