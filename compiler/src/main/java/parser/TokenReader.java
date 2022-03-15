package parser;

import lexan.model.Token;

import java.util.List;

public class TokenReader {

    private final List<Token> tokenList;
    private int currentIndex = 0;

    public TokenReader(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public Token peekToken(){
        // 预读，下标不变
        return tokenList.get(currentIndex);
    }

    public Token peekToken(int index){
        // 预读，下标不变
        return tokenList.get(currentIndex+index);
    }

    public boolean canRead(){
        return currentIndex < tokenList.size()-1;
    }

    public Token readToken(){
        Token token = tokenList.get(currentIndex);
        // 普通读取，下标递增
        currentIndex++;
        return token;
    }
}
