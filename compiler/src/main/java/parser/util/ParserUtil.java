package parser.util;

import lexan.enums.TokenTypeEnum;
import lexan.model.Token;

public class ParserUtil {

    public static boolean isKeywordTypeDefineToken(Token token){
        return token.getKeywordEnum() != null && token.getKeywordEnum().getTypeDefine();
    }

    /**
     * 是否是加减乘除运算符
     * */
    public static boolean isCalculateOp(Token token){
        TokenTypeEnum tokenTypeEnum = token.getTokenTypeEnum();
        if(tokenTypeEnum == TokenTypeEnum.PLUS || tokenTypeEnum == TokenTypeEnum.MINUS ||
                tokenTypeEnum == TokenTypeEnum.MULTI || tokenTypeEnum == TokenTypeEnum.DIVISION){
            return true;
        }else {
            return false;

        }
    }

    /**
     * 是否是基础元素
     * */
    public static boolean isPrimary(Token token){
        TokenTypeEnum tokenTypeEnum = token.getTokenTypeEnum();
        if(tokenTypeEnum == TokenTypeEnum.LITERAL || tokenTypeEnum == TokenTypeEnum.IDENTIFIER){
            return true;
        }else {
            return false;

        }
    }
}
