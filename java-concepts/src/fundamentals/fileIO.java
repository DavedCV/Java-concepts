package fundamentals;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class fileIO {
    public static void main(String[] args) {
        try (Scanner in =
                     new Scanner(Path.of("src/basics/testFile.txt"), StandardCharsets.US_ASCII))
        {
            while (in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (PrintWriter out =
                     new PrintWriter("src/basics/testFile.txt", StandardCharsets.US_ASCII))
        {
            out.print("Printed line from java!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

