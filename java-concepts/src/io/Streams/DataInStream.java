package io.Streams;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DataInStream {
    public static void main(String[] args) throws IOException {
        try (DataInputStream in =
                     new DataInputStream(new FileInputStream("src/io/Streams/BufferedOutStreamTest.txt"))
        )
        {
            int count = in.available();
            System.out.println("Available: " + count);

            byte[] ary = new byte[count];
            in.read(ary);

            for (byte b : ary) {
                System.out.print((char) b + "-");
            }

        }
    }
}
