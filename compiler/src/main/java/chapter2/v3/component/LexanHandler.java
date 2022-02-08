package chapter2.v3.component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongyx
 * @date 2022/2/7
 */
public class LexanHandler {

    public static void main(String[] args) {
        CompileContext compileContext = new CompileContext("91 + 52- 12 + 1321+ 13- 13- 6666 -1".toCharArray());
        List<LexanTuple> lexanTupleList = new ArrayList<>();
        while(compileContext.canLexan()){
            LexanTuple lexanTuple = lexan(compileContext);
            System.out.println(lexanTuple);
            lexanTupleList.add(lexanTuple);
        }

        System.out.println(lexanTupleList);
    }

    public static LexanTuple lexan(CompileContext compileContext){
        while(true){
            char c = compileContext.getNextInputChar();
            if(c == ' ' || c == '\t'){
                // 空格或者制表符 => 忽略
            }else if(c == '\n'){
                // 换行符 => 扫描行号+1
                compileContext.lineNum++;
            }else if(Character.isDigit(c)){
                String tokenValue = parseOneNum(c,compileContext);

                LexanTuple lexanTuple = new LexanTuple();
                lexanTuple.tupleType = TupleType.NUM;
                lexanTuple.tupleValue = tokenValue;
                return lexanTuple;
            }else if(c == '+'){
                // 加号
                LexanTuple lexanTuple = new LexanTuple();
                lexanTuple.tupleType = TupleType.PLUS;
                return lexanTuple;
            }else if(c == '-'){
                // 减号
                LexanTuple lexanTuple = new LexanTuple();
                lexanTuple.tupleType = TupleType.MINUS;
                return lexanTuple;
            }else{
                // 非法符号
                throw new RuntimeException("un support char=" + c + " lineNum=" + compileContext.lineNum);
            }
        }
    }

    private static String parseOneNum(char c,CompileContext compileContext){
        StringBuilder tokenValue = new StringBuilder();
        // 数字类型
        tokenValue.append(c);

        while(compileContext.canLexan() && Character.isDigit(compileContext.getCurrentInputChar())){
            tokenValue.append(compileContext.getCurrentInputChar());
            compileContext.lookAheadIndex++;
        }

        return tokenValue.toString();
    }
}
