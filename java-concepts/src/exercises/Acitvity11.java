package exercises;

import java.util.Scanner;

public class Acitvity11 {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int currentNumberPeaches = 0;
        int numberOfBoxesShipped = 0;

        while (true) {
            System.out.print("Enter incoming number of peaches: ");
            int incomingNumberPeaches = sc.nextInt();

            if (incomingNumberPeaches == 0) break;

            currentNumberPeaches += incomingNumberPeaches;

            while (currentNumberPeaches >= 20) {
                numberOfBoxesShipped++;
                currentNumberPeaches -= 20;
                System.out.println(numberOfBoxesShipped + " Boxes shipped, " + currentNumberPeaches + " peaches " +
                                           "remaining");
            }
        }


    }
}
