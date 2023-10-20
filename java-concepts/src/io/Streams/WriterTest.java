package io.Streams;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriterTest {
    public static void main(String[] args) throws IOException {
        Writer w = new FileWriter("src/io/Streams/WriterTest.txt");
        String content = "I love my country";
        w.write(content);
        w.close();
        System.out.println("Done");
    }
}
