package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ScanningNums {
    public static void main(String[] args) throws FileNotFoundException {
        double sum = 0;

        try(Scanner s = new Scanner(new BufferedReader(new FileReader("src/io/usNumbers.txt")))) {
            while(s.hasNext()) {
                if (s.hasNextDouble())
                    sum += s.nextDouble();
                else
                    s.next();
            }
        }

        System.out.println(sum);
    }
}
