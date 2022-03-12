package parser.util;

import lexan.model.Token;

public class ParserUtil {

    public static boolean isKeywordTypeDefineToken(Token token){
        return token.getKeywordEnum() != null && token.getKeywordEnum().getTypeDefine();
    }
}
