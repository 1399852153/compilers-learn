package parser.enums;

public enum ASTNodeTypeEnum {

//    Programm,           //程序入口，根节点
//
//    Block,
//    IntDeclaration,     //整型变量声明
//    AssignmentStmt,     //赋值语句
//
//    Multiplicative,     //乘法表达式
//    Additive,           //加法表达式
//
//    Identifier,         //标识符
//    IntLiteral,          //整型字面量


    PRIMARY("基础类型"),
    S_EXPRESSION(" S表达式"),
    CALCULATE_OP("算数操作符"),
    ;

    private String message;

    ASTNodeTypeEnum(String message) {
        this.message = message;
    }
}
