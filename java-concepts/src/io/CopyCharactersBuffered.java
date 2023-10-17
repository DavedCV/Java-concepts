package io;


import java.io.*;

/*
* In CopyCharacters, the int variable holds a character value in its last 16 bits; in CopyBytes, the int variable
* holds a byte value in its last 8 bits.
*
* */
public class CopyCharactersBuffered {
    public static void main(String[] args) throws IOException {
        try (
                BufferedReader in = new BufferedReader(new FileReader("src/io/xanadu.txt"));
                BufferedWriter out = new BufferedWriter(new FileWriter("src/io/outagain.txt"));
        ) {
            int c;
            while ((c = in.read()) != -1) {
                System.out.println(Integer.toHexString(c));
                out.write(c);
            }
        }
    }
}
