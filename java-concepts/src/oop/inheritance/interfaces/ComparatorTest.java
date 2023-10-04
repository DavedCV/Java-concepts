package oop.inheritance.interfaces;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public class ComparatorTest {
    public static void main(String[] args) {
        String[] friends = {"Peter", "Paul", "Mary", "a very long name", "x"};
        Comparator<String> comp = new LengthComparator();

        System.out.println("Unsorted array: " + Arrays.toString(friends));
        Arrays.sort(friends, comp);
        System.out.println("Sorted array: " + Arrays.toString(friends));
    }
}

class LengthComparator implements Comparator<String> {
    @Override
    public int compare(String first, String second) {
        return first.length() - second.length();
    }
}
