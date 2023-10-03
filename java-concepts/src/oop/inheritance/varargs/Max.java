package oop.inheritance.varargs;

public class Max {

    public static double max(double... values) {
        double largest = Double.NEGATIVE_INFINITY;
        for (double v : values) {
            if (v > largest) largest = v;
        }
        return largest;
    }

    public static void main(String[] args) {
        double max = max(3.1, 40.4, -5.1);
        System.out.println(max);
    }
}
