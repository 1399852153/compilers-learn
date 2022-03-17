package parser.enums;

import lexan.enums.TokenTypeEnum;

import java.util.HashMap;
import java.util.Map;

public enum BinaryOpEnum {
    PLUS(TokenTypeEnum.PLUS, 9,ASTNodeTypeEnum.ADDITIVE_EXPRESSION),
    MINUS(TokenTypeEnum.MINUS, 9,ASTNodeTypeEnum.ADDITIVE_EXPRESSION),
    MULTI(TokenTypeEnum.MULTI, 10,ASTNodeTypeEnum.MULTIPLICATIVE_EXPRESSION),
    DIVISION(TokenTypeEnum.DIVISION, 10,ASTNodeTypeEnum.MULTIPLICATIVE_EXPRESSION),
    ;

    BinaryOpEnum(TokenTypeEnum tokenTypeEnum, Integer priorityLevel, ASTNodeTypeEnum astNodeTypeEnum) {
        this.tokenTypeEnum = tokenTypeEnum;
        this.priorityLevel = priorityLevel;
        this.astNodeTypeEnum = astNodeTypeEnum;
    }

    private TokenTypeEnum tokenTypeEnum;
    private Integer priorityLevel;
    private ASTNodeTypeEnum astNodeTypeEnum;

    public static Map<TokenTypeEnum,BinaryOpEnum> toMap(){
        Map<TokenTypeEnum,BinaryOpEnum> map = new HashMap<>();
        for(BinaryOpEnum opEnum : values()){
            BinaryOpEnum oldValue = map.put(opEnum.tokenTypeEnum,opEnum);
            throw new RuntimeException("repeat define opEnum=" + opEnum);
        }
        return map;
    }
}
