package parser;


import lexan.enums.TokenTypeEnum;
import lexan.model.Token;
import parser.enums.ASTNodeTypeEnum;
import parser.model.ASTNode;
import parser.util.ParserUtil;

public class Parser {

    private TokenReader tokenReader;

    public Parser(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }

    /**
     * 按照语法规则解析token列表，返回抽象语法树AST的根节点
     * */
    public ASTNode parse(){
        blockStatement();

        return null;
    }

    private void blockStatement(){
        ASTNode blockASTNode = new ASTNode(ASTNodeTypeEnum.Block,"block");
        Token nextToken = tokenReader.peekToken();
        if(nextToken.getTokenTypeEnum() == TokenTypeEnum.LEFT_BRACE){
            tokenReader.readToken();
            expressionStatementList();
        }else{
            throw new RuntimeException("blockStatement parse error: not start with {");
        }


    }

    private ASTNode expressionStatementList(){
        Token nextToken = tokenReader.peekToken();

        if(ParserUtil.isKeywordTypeDefineToken(nextToken)){
            // 基础类型定义语句（例如 int a = 123;）


        }

        return null;
    }
}
