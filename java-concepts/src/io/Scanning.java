package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/*
 * Objects of type Scanner are useful for breaking down formatted input into tokens and translating individual
 * tokens according to their data type.
 * */
public class Scanning {
    public static void main(String[] args) throws IOException {

        try (Scanner s = new Scanner(new BufferedReader(new FileReader("src/io/xanadu.txt")))) {
            while (s.hasNext()) {
                System.out.println(s.next());
            }
        }
    }
}
