package parser;


import lexan.model.Token;
import parser.model.ASTNode;
import parser.util.ParserUtil;

public class Parser {

    /**
     * 按照语法规则解析token列表，返回抽象语法树AST的根节点
     * */
    public ASTNode parse(TokenReader tokenReader){
        Token nextToken = tokenReader.peekToken();
        if(ParserUtil.isKeywordTypeDefineToken(nextToken)){
            // 基础类型定义语句（例如 int a = 123;）


        }

        return null;
    }


}
