package io.Streams;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class BufferedInStream {
    public static void main(String[] args) {
        try (BufferedInputStream in =
                     new BufferedInputStream(new FileInputStream("src/io/Streams/BufferedOutStreamTest.txt"));
             DataInputStream din = new DataInputStream(in);
        )
        {
            int character = 0;
            while ((character = in.read()) != -1) {
                System.out.print((char) character);
                if ((char) character == '\n') System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
