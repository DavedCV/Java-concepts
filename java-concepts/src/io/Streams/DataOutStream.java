package io.Streams;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataOutStream {
    public static void main(String[] args) throws IOException {
        try (
                DataOutputStream out =
                        new DataOutputStream(new FileOutputStream("src/io/Streams/DataOutStreamTest.txt", false))
        ) {
            out.writeInt(65);
            out.flush();
        }

        System.out.println("Success...");
    }
}
