package util;

import lexan.enums.TokenTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenTypeUtil {

    private static final Map<Integer, TokenTypeEnum> tokenTypeMap;

    static {
        tokenTypeMap = new HashMap<>();
        for(TokenTypeEnum tokenTypeEnum : TokenTypeEnum.values()){
            List<Integer> matchedStates = tokenTypeEnum.getMatchedStates();

            matchedStates.forEach(item -> {
                TokenTypeEnum oldValue = tokenTypeMap.put(item, tokenTypeEnum);
                if (oldValue != null) {
                    throw new RuntimeException("tokenType not unique tokenTypeEnum=" + tokenTypeEnum);
                }
            });

        }
    }

    public static TokenTypeEnum getByState(Integer state){
        TokenTypeEnum tokenTypeEnum = tokenTypeMap.get(state);
        if(tokenTypeEnum == null){
            throw new RuntimeException("no matched tokenType state=" + state);
        }
        return tokenTypeEnum;
    }
}
