package parser;

import lexan.enums.TokenTypeEnum;
import lexan.model.Token;

/**
 * @author xiongyx
 * @date 2022/3/17
 *
 * 单token解析,把MyParser类按功能拆解一下
 */
public class MyParserMatchOneToken {

    private TokenReader tokenReader;

    public MyParserMatchOneToken(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }


    public Token matchEqualsChar(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.ASSIGNMENT && token.getValue().equals("=")){
            throw new RuntimeException("语法分析错误 not match '=' :" + token);
        }
        return token;
    }

    public Token matchLeftBrace(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.LEFT_BRACE){
            throw new RuntimeException("语法分析错误 not match '{' :" + token);
        }
        return token;
    }

    public Token matchRightBrace(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.RIGHT_BRACE){
            throw new RuntimeException("语法分析错误 not match '}' :" + token);
        }
        return token;
    }

    public Token matchLeftParentheses(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.LEFT_PARENTHESES){
            throw new RuntimeException("语法分析错误 not match '(' :" + token);
        }
        return token;
    }

    public Token matchRightParentheses(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.RIGHT_PARENTHESES){
            throw new RuntimeException("语法分析错误 not match ')' :" + token);
        }
        return token;
    }

    /**
     * 分号;
     * */
    public Token matchSemicolon(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.SEMICOLON){
            throw new RuntimeException("语法分析错误 not match ';' :" + token);
        }

        return token;
    }

    /**
     * 加号
     * */
    public Token matchPlus(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.PLUS){
            throw new RuntimeException("语法分析错误 not match '+' :" + token);
        }

        return token;
    }

    /**
     * 减号
     * */
    public Token matchMinus(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.MINUS){
            throw new RuntimeException("语法分析错误 not match '-' :" + token);
        }

        return token;
    }

    /**
     * 双加号
     * */
    public Token matchDoublePlus(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.DOUBLE_PLUS){
            throw new RuntimeException("语法分析错误 not match '++' :" + token);
        }

        return token;
    }

    /**
     * 双减号
     * */
    public Token matchDoubleMinus(){
        Token token = tokenReader.readToken();
        if(token.getTokenTypeEnum() != TokenTypeEnum.DOUBLE_MINUS){
            throw new RuntimeException("语法分析错误 not match '--' :" + token);
        }

        return token;
    }
}
