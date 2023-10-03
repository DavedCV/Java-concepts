package basics;

public class formattingOutput {
    public static void main(String[] args) {
        double x = 10_000.0 / 3.0;
        System.out.println(x);
        System.out.printf("Number with fixed length and precision: %8.2f\n", x);
        System.out.printf("Number with fixed length, precision and format: %,8.2f\n", x);
        System.out.printf("Number with fixed length, precision and format: %0,14.2f\n", x);

        String name = "david";
        int age = 22;
        System.out.printf("Hello, %s. Next year you'll be %d\n", name, age);

    }
}
