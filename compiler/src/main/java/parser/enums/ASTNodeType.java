package parser.enums;

public enum ASTNodeType {

    Programm,           //程序入口，根节点

    IntDeclaration,     //整型变量声明
    AssignmentStmt,     //赋值语句

    Multiplicative,     //乘法表达式
    Additive,           //加法表达式

    Identifier,         //标识符
    IntLiteral          //整型字面量
}
