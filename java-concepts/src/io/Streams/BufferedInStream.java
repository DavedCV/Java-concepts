package io.Streams;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class BufferedInStream {
    public static void main(String[] args) {
        try (BufferedInputStream in =
                     new BufferedInputStream( new FileInputStream("src/io/Streams/BufferedOutStreamTest.txt"))) {
            int firstLetter = in.read();
            System.out.println("First char: ");
            System.out.println((char) firstLetter);

            int character = 0;
            System.out.println("Rest of chars: ");
            while((character = in.read()) != -1){
                System.out.println((char) character);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
