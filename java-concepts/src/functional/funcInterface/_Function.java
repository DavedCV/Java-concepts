package functional.funcInterface;

import java.util.function.BiFunction;
import java.util.function.Function;

/*
 * See how we can pass from normal functions definitions
 * to the functional interface "Function"
 * */
public class _Function {

    // This is the same as the defined function but in functional paradigm
    static Function<Integer, Integer> incrementByOneFunction = number -> number + 1;
    static Function<Integer, Integer> multiplyBy10Function = number -> number * 10;
    static BiFunction<Integer, Integer, Integer> incrementByOneAndMultiplyFunction =
            (numberToIncrementByOne, numberToMultiplyBy)
                    -> (numberToIncrementByOne + 1) * numberToMultiplyBy;

    public static void main(String[] args) {

        // function takes 1 argument -----------------------------------------------------------------------------------

        // using normal function
        System.out.println("Normal function: " + incrementByOne(1));

        // using functional paradigm
        int increment = incrementByOneFunction.apply(1);
        System.out.println("Using functional paradigm: "
                                   + incrementByOneFunction.apply(1));

        // chaining functions
        System.out.println("Chaining function results: "
                                   + multiplyBy10Function.apply(increment));

        System.out.println("Chaining functions oneline: "
                                   + incrementByOneFunction.andThen(multiplyBy10Function).apply(4));

        // function takes 2 arguments ----------------------------------------------------------------------------------
        System.out.println("Increment and multiply: "
                                   + incrementByOneAndMultiply(4, 100));

        System.out.println("Increment and multiply with bifunction: "
                                   + incrementByOneAndMultiplyFunction.apply(4,
                                                                             100));
    }

    static int incrementByOne(int number) {
        return number + 1;
    }

    static int incrementByOneAndMultiply(int number, int numToMultiplyBy) {
        return (number + 1) * numToMultiplyBy;
    }
}
