package chapter2.v3.component;

/**
 * @author xiongyx
 * @date 2022/2/7
 */
public class LexanTuple{

    public TupleType tupleType;
    public String tupleValue;

    @Override
    public String toString() {
        return "LexanTuple{" +
            "tupleType=" + tupleType +
            ", tupleValue='" + tupleValue + '\'' +
            '}';
    }
}
