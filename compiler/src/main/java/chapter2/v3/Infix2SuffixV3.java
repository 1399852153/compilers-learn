package chapter2.v3;

import chapter2.v2.Infix2SuffixV2;
import chapter2.v3.component.CompileContext;
import chapter2.v3.component.LexanHandler;
import chapter2.v3.component.LexanTuple;
import chapter2.v3.component.TupleType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongyx
 * @date 2022/2/8
 */
public class Infix2SuffixV3 {

    private static List<LexanTuple> INPUT_TUPLE_LIST;
    private static int lookAheadIndex = 0;

    private static List<LexanTuple> OUTPUT_TUPLE_LIST = new ArrayList<>();
    private static int outputIndex = 0;

    public static void main(String[] args) {
        CompileContext compileContext = new CompileContext("91 + 52- 121".toCharArray());
        List<LexanTuple> lexanTupleList = new ArrayList<>();
        while(compileContext.canLexan()){
            LexanTuple lexanTuple = LexanHandler.lexan(compileContext);
            System.out.println(lexanTuple);
            lexanTupleList.add(lexanTuple);
        }

        INPUT_TUPLE_LIST = lexanTupleList;

        expr();
        System.out.println(INPUT_TUPLE_LIST);
        System.out.println(OUTPUT_TUPLE_LIST);
    }

    private static void expr(){
        term();
        rest();
    }

    private static void rest(){
        while(true) {
            if(lookAheadIndex >= INPUT_TUPLE_LIST.size()){
                return;
            }
            int lookAheadSnapshot = lookAheadIndex;

            LexanTuple lexanTuple = INPUT_TUPLE_LIST.get(lookAheadSnapshot);
            TupleType tupleType = lexanTuple.tupleType;
            switch (tupleType) {
                case PLUS:
                    match(lookAheadSnapshot, TupleType.PLUS);
                    term();
                    OUTPUT_TUPLE_LIST.add(lexanTuple);
                    break;
                case MINUS:
                    match(lookAheadSnapshot, TupleType.MINUS);
                    term();
                    OUTPUT_TUPLE_LIST.add(lexanTuple);
                    break;
                default:
                    throw new RuntimeException("not a oper lookAhead=" + lookAheadIndex + " lexanTuple=" + lexanTuple);
            }
        }
    }

    private static void term(){
        int lookAheadSnapshot = lookAheadIndex;
        LexanTuple lexanTuple = INPUT_TUPLE_LIST.get(lookAheadSnapshot);
        if(lexanTuple.tupleType == TupleType.NUM){
            Infix2SuffixV3.lookAheadIndex++;
            OUTPUT_TUPLE_LIST.add(lexanTuple);
        }else{
            throw new RuntimeException("not a digit lookAhead=" + lookAheadIndex + " lexanTuple=" + lexanTuple);
        }
    }

    private static void match(int index, TupleType expectTupleType){
        if(INPUT_TUPLE_LIST.get(index).tupleType == expectTupleType){
            Infix2SuffixV3.lookAheadIndex++;
        }else{
            throw new RuntimeException("not match index=" + index + " expectTupleType=" + expectTupleType);
        }
    }

}
