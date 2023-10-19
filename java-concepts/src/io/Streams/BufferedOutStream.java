package io.Streams;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BufferedOutStream {
    public static void main(String[] args) throws IOException {

        try (
                FileOutputStream fout = new FileOutputStream("src/io/Streams/BufferedOutStreamTest.txt");
                BufferedOutputStream bout = new BufferedOutputStream(fout);
                DataOutputStream dout = new DataOutputStream(bout);
        ) {
            String goal = "Learn java in depth\n";
            byte[] goalBytes = goal.getBytes();
            dout.write(goalBytes);

            for (int i = 0; i < 255; i++) {
                dout.writeUTF("Cadena " + i + "\n");
            }

            dout.flush();
        }

        System.out.println("Success...");
    }
}
