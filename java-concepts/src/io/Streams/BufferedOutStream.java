package io.Streams;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BufferedOutStream {
    public static void main(String[] args) throws IOException {

        try (
                FileOutputStream fout = new FileOutputStream("src/io/Streams/BufferedOutStreamTest.txt");
                BufferedOutputStream bout = new BufferedOutputStream(fout);
        ) {
            String goal = "Learn java in depth";
            byte[] goalBytes = goal.getBytes();

            bout.write(goalBytes);
            bout.flush();
        }

        System.out.println("Success...");
    }
}
