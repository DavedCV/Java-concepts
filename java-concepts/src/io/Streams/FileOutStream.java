package io.Streams;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileOutStream {
    public static void main(String[] args) {
        String goal = "Learn java in depth";
        try {
            FileOutputStream fout = new FileOutputStream("src/io/Streams/fileOutStreamTest.txt", true);

            byte[] goalBytes = goal.getBytes();
            System.out.println(Arrays.toString(goalBytes));

            fout.write(goalBytes);
            fout.write("\n".getBytes());

            fout.close();
            System.out.println("Success...");
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
