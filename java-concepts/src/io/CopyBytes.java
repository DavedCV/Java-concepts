package io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*
* Byte streams should only be used for the most primitive I/O.
* */
public class CopyBytes {
    public static void main(String[] args) throws IOException {
        try (
                FileInputStream in = new FileInputStream("src/io/xanadu.txt");
                FileOutputStream out = new FileOutputStream("src/io/outagain.txt");
        ) {
            int c;
            while ((c = in.read()) != -1) {
                // System.out.println(Integer.toHexString(c));
                out.write(c);
            }
        }
    }
}
