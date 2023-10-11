package generics;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.function.*;

public class GenericArrayCreation {
    public static void main(String[] args) {
        String[] arr = lambdaCreation(String[]::new, "Hola", "Como", "Estas");
        System.out.println(Arrays.toString(arr));
    }

    public static <T> T[] lambdaCreation(Function<Integer, T[]> arrCons, T... args) {
        T[] array = arrCons.apply(args.length);

        IntStream.range(0, args.length)
                 .forEach(i -> array[i] = args[i]);

        return array;
    }
}
