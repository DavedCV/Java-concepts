package io;

import java.io.*;

public class CopyLines {
    public static void main(String[] args) throws IOException {
        try (
                BufferedReader in = new BufferedReader(new FileReader("src/io/xanadu.txt"));
                PrintWriter out = new PrintWriter(new FileWriter("src/io/outagain.txt"));
        ) {
            String l;
            while ((l = in.readLine()) != null) {
                System.out.println(l);
                out.println(l);
            }
        }
    }
}
