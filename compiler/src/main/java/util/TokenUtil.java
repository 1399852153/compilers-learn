package util;

import lexan.enums.KeyWordEnum;
import lexan.enums.TokenTypeEnum;

import java.util.*;

public class TokenUtil {

    private static final Map<Integer, TokenTypeEnum> tokenTypeMap;
    private static final Set<String> keywordsSet;


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

        keywordsSet = new HashSet<>();
        for(KeyWordEnum keyWordEnum : KeyWordEnum.values()){
            boolean notAlreadyExists = keywordsSet.add(keyWordEnum.getCode());
            if (!notAlreadyExists) {
                throw new RuntimeException("keywords define repeat" + keyWordEnum);
            }
        }
    }

    public static TokenTypeEnum getByState(Integer state){
        TokenTypeEnum tokenTypeEnum = tokenTypeMap.get(state);
        if(tokenTypeEnum == null){
            throw new RuntimeException("no matched tokenType state=" + state);
        }
        return tokenTypeEnum;
    }

    public static boolean isKeyword(String tokenValue){
        return keywordsSet.contains(tokenValue);
    }
}
