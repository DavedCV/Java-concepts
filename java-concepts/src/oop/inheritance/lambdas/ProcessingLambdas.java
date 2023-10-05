package oop.inheritance.lambdas;

import java.util.function.IntConsumer;

public class ProcessingLambdas {

    // Using the functional interface "Runnable"
    public static void repeat(int n, Runnable action) {
        for (int i = 0; i < n; i++) action.run();
    }

    public static void repeatPrintIndex(int n, IntConsumer action) {
        for (int i = 0; i < n; i++) action.accept(i);
    }

    public static void main(String[] args) {
        System.out.println("Using Runnable interface: ");
        repeat(10, () -> System.out.println("Hello, world!"));

        System.out.println("Using IntConsumer interface: ");
        repeatPrintIndex(10, (index) -> System.out.println(index + " - Hello, world!"));
    }
}
