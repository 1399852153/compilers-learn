package chapter2.v3.component;

/**
 * @author xiongyx
 * @date 2022/2/7
 */
public class CompileContext {

    private final char[] INPUT_EXPR;
    public int lookAheadIndex = 0;

    private final char[] OUTPUT_EXPR;
    public int outputIndex = 0;

    public CompileContext(char[] INPUT_EXPR) {
        this.INPUT_EXPR = INPUT_EXPR;
        this.OUTPUT_EXPR = new char[INPUT_EXPR.length];
    }

    public int lineNum = 1;

    public char getNextInputChar(){
        int currentIndex = lookAheadIndex;
        lookAheadIndex++;
        return INPUT_EXPR[currentIndex];
    }

    public char getCurrentInputChar(){
        return INPUT_EXPR[lookAheadIndex];
    }

    public boolean canLexan(){
        return lookAheadIndex < INPUT_EXPR.length;
    }
}
