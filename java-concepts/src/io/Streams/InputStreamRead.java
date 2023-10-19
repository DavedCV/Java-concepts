package io.Streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

public class InputStreamRead {

    public static void main(String[] args) throws IOException {
        Reader r = new InputStreamReader(System.in);

        System.out.print("Read 4 chars: ");
        char[] fourChars = new char[4];
        r.read(fourChars);
        System.out.println(Arrays.toString(fourChars));
        r.read(); //read the \n

        System.out.println();

        BufferedReader b = new BufferedReader(r);
        String line;
        do {
            // press ctrl + d to add EOF
            line = b.readLine();
            if (line != null) {
                System.out.println("Entrada: " + line + " " + line.length());
            }
        } while(line != null);

        r.close();
        b.close();
    }
}
