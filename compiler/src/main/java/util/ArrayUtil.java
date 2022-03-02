package util;

public class ArrayUtil {

    public static <T> void print2DArray(T[][] array){
        for(int i=0; i<array.length; i++){
            T[] lineArray = array[i];
            for(int j=0; j<lineArray.length; j++){
               System.out.print(array[i][j] + ",");
            }
            System.out.println();
        }
    }
}
