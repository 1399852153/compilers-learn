package chapter2.v1;

/**
 * @author xiongyx
 * @date 2022/1/25
 */
public class Infix2SuffixV1 {

    private static final char[] INPUT_EXPR = "9+5-2".toCharArray();
    private static int lookAheadIndex = 0;

    private static final char[] OUTPUT_EXPR = new char[INPUT_EXPR.length];
    private static int outputIndex = 0;

    public static void main(String[] args) {
        expr();
        System.out.println(INPUT_EXPR);
        System.out.println(OUTPUT_EXPR);
    }

    private static void expr(){
        term();
        rest();
    }

    private static void rest(){
        if(lookAheadIndex >= INPUT_EXPR.length){
            return;
        }

        int lookAheadSnapshot = lookAheadIndex;

        char ch = INPUT_EXPR[lookAheadSnapshot];

        switch (ch){
            case '+':
                match(lookAheadSnapshot,'+');
                term();
                outputAdd(ch);
                rest();
                return;
            case '-':
                match(lookAheadSnapshot,'-');
                term();
                outputAdd(ch);
                rest();
                return;
            default:
                throw new RuntimeException("not a oper lookAhead=" + lookAheadIndex + " ch=" + ch);
        }
    }

    private static void term(){
        int lookAheadSnapshot = lookAheadIndex;
        char ch = INPUT_EXPR[lookAheadSnapshot];
        if(Character.isDigit(ch)){
            Infix2SuffixV1.lookAheadIndex++;
            outputAdd(ch);
        }else{
            throw new RuntimeException("not a digit lookAhead=" + lookAheadIndex + " ch=" + ch);
        }
    }

    private static void match(int index, char expectChar){
        if(INPUT_EXPR[index] == expectChar){
            Infix2SuffixV1.lookAheadIndex++;
        }else{
            throw new RuntimeException("not match index=" + index + " expectChar=" + expectChar);
        }
    }

    private static void outputAdd(char ch){
        OUTPUT_EXPR[outputIndex] = ch;
        outputIndex++;
    }
}
