package io;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
* In CopyCharacters, the int variable holds a character value in its last 16 bits; in CopyBytes, the int variable
* holds a byte value in its last 8 bits.
*
* */
public class CopyCharacters {
    public static void main(String[] args) throws IOException {
        try (
                FileReader in = new FileReader("src/io/xanadu.txt");
                FileWriter out = new FileWriter("src/io/outagain.txt");
        ) {
            int c;
            while ((c = in.read()) != -1) {
                System.out.println(Integer.toHexString(c));
                out.write(c);
            }
        }
    }
}
