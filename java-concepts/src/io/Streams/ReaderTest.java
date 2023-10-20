package io.Streams;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ReaderTest {
    public static void main(String[] args) throws IOException {
        try (Reader r = new FileReader("src/io/Streams/WriterTest.txt")) {
            int ch;
            while((ch = r.read()) != -1) {
                System.out.print((char) ch);
            }
        }
    }
}
