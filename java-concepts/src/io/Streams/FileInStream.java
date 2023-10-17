package io.Streams;

import java.io.FileInputStream;
import java.io.IOException;

public class FileInStream {
    public static void main(String[] args) {
        try (FileInputStream in = new FileInputStream("src/io/Streams/fileOutStreamTest.txt")) {
            int firstLetter = in.read();
            System.out.println("First char: " + (char) firstLetter);

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
