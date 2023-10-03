package basics;

import java.util.Arrays;
import java.util.Scanner;

public class arrays {

    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        // array definition
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println(Arrays.toString(array));

        // redefinition with anonymous array creation
        array = new int[]{1, 2, 3};
        System.out.println(Arrays.toString(array));

        // iterate using for each
        System.out.println("Using for each: ");
        for (int num : array) {
            System.out.println(num);
        }

        // make a copy of an array
        int[] arrayCopy = Arrays.copyOf(array, array.length);

        // sort and array
        int[] testSort = {4, 45, 61, 3, 4, 8, 3, 18, 7, 8, 5};
        System.out.println("Unsorted array: " + Arrays.toString(testSort));
        Arrays.sort(testSort);
        System.out.println("Sorted array: " + Arrays.toString(testSort));

        // using binary search
        // System.out.print("What target number?: ");
        // int target = in.nextInt();
        int index = Arrays.binarySearch(testSort, 45);
        if (index < 0) {
            System.out.println("To maintain order number should be added at position: " + (-1 * (index + 1)));
        }
        else {
            System.out.println("Number is in position: " + index);
        }

        // compare arrays
        boolean eq = Arrays.equals(array, testSort);
        System.out.println(eq);

        // ragged arrays
        int[][] raggedArray = new int[10][];

        for (int i = 0; i < raggedArray.length; i++) {
            raggedArray[i] = new int[i+1];
            Arrays.fill(raggedArray[i], i+1);
        }
        System.out.println(Arrays.deepToString(raggedArray));
    }
}
