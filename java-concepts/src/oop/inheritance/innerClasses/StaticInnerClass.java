package oop.inheritance.innerClasses;

import java.util.Arrays;

public class StaticInnerClass {
    public static void main(String[] args) {
        var values = new double[20];

        for (int i = 0; i < values.length; i++) {
            values[i] = 100 * Math.random();
        }

        ArrayAlg.Pair p = ArrayAlg.minmax(values);

        System.out.println(Arrays.toString(values));
        System.out.println("Min = " + p.getFirst());
        System.out.println("Max = " + p.getSecond());
    }
}

class ArrayAlg {
    /**
     * A pair of floating-point numbers
     */
    public static class Pair {
        private double first;
        private double second;

        /**
         * Constructs a pair from two floating-point numbers
         *
         * @param first the first number
         * @param second the second number
         */
        public Pair(double first, double second) {
            this.first = first;
            this.second = second;
        }

        /**
         * Returns the first number of the pair
         *
         * @return the first number
         */
        public double getFirst() {
            return first;
        }

        /**
         * Returns the second number of the pair
         *
         * @return the second number
         */
        public double getSecond() {
            return second;
        }
    }

    /**
     * Computes both the minimum and the maximum of an array
     * @param values an array of floating-point numbers
     * @return a pair whose first element is the minimum and whose second element
     * is the maximum
     */
    public static Pair minmax(double[] values) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for (double v : values) {
            if (min > v) min = v;
            if (max < v) max = v;
        }

        return new Pair(min, max);
    }
}
