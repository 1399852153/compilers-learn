package parser.enums;

public enum ASTNodeTypeEnum {

    Program("程序入口，根节点"),

    Block("块作用域"),
    IntDeclaration("整型变量声明"),
    AssignmentStmt("赋值语句"),


    EMPTY_STATEMENT("空语句"),
    Identifier("标识符"),
    INT_LITERAL("整型字面量"),
    PRIMARY_TYPE("基础类型"),
    VARIABLE_DECLARATOR_ID("变量定义(变量名)"),
    VARIABLE_DECLARATOR("变量定义"),
    VARIABLE_DECLARATORS("变量定义集合"),
    BLOCK_STATEMENTS("块语句集合"),
    ASSIGNMENT("赋值语句"),
    ASSIGNMENT_OPERATOR("赋值操作符"),
    ADDITIVE_EXPRESSION("加法表达式"),
    MULTIPLICATIVE_EXPRESSION("乘法表达式"),


    PRIMARY("基础类型"),
    S_EXPRESSION(" S表达式"),
    CALCULATE_OP("算数操作符"),
    ;

    private String message;

    ASTNodeTypeEnum(String message) {
        this.message = message;
    }
}
