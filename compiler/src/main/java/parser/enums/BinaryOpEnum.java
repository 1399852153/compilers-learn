package parser.enums;

import lexan.enums.TokenTypeEnum;

import java.util.HashMap;
import java.util.Map;

public enum BinaryOpEnum {
    PLUS(TokenTypeEnum.PLUS, 9,ASTNodeTypeEnum.ADDITIVE_EXPRESSION,true),
    MINUS(TokenTypeEnum.MINUS, 9,ASTNodeTypeEnum.ADDITIVE_EXPRESSION,true),
    MULTI(TokenTypeEnum.MULTI, 10,ASTNodeTypeEnum.MULTIPLICATIVE_EXPRESSION,true),
    DIVISION(TokenTypeEnum.DIVISION, 10,ASTNodeTypeEnum.MULTIPLICATIVE_EXPRESSION,true),
    ;

    BinaryOpEnum(TokenTypeEnum tokenTypeEnum, Integer priorityLevel, ASTNodeTypeEnum astNodeTypeEnum,boolean leftAssociativity) {
        this.tokenTypeEnum = tokenTypeEnum;
        this.priorityLevel = priorityLevel;
        this.astNodeTypeEnum = astNodeTypeEnum;
        this.leftAssociativity = leftAssociativity;
    }

    private final TokenTypeEnum tokenTypeEnum;
    private final int priorityLevel;
    private final ASTNodeTypeEnum astNodeTypeEnum;
    private final boolean leftAssociativity;


    public static Map<TokenTypeEnum,BinaryOpEnum> toMap(){
        Map<TokenTypeEnum,BinaryOpEnum> map = new HashMap<>();
        for(BinaryOpEnum opEnum : values()){
            BinaryOpEnum oldValue = map.put(opEnum.tokenTypeEnum,opEnum);
            throw new RuntimeException("repeat define opEnum=" + opEnum);
        }
        return map;
    }

    public TokenTypeEnum getTokenTypeEnum() {
        return tokenTypeEnum;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public ASTNodeTypeEnum getAstNodeTypeEnum() {
        return astNodeTypeEnum;
    }

    public boolean isLeftAssociativity() {
        return leftAssociativity;
    }
}
