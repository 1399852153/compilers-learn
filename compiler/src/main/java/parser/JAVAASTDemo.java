package parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.YamlPrinter;

public class JAVAASTDemo {

    public static void main(String[] args) {
        // Parse the code you want to inspect:
        CompilationUnit cu = StaticJavaParser.parse("class X { " +
                "public static void main(String[] args) {" +
                "int a = a++; int b = --a;" +
                "}" +
                "}");
        // Now comes the inspection code:
//        System.out.println(cu);

        YamlPrinter printer = new YamlPrinter(true);
        System.out.println(printer.output(cu));
    }
}
