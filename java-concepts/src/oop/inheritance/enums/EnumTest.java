package oop.inheritance.enums;

import java.util.Scanner;

public class EnumTest {
    public static void main(String[] args) {
        var in = new Scanner(System.in);

        System.out.println("Enter a size: (SMALL, MEDIUM, LARGE, EXTRA_LARGE)");
        String input = in.next().toUpperCase();

        Size size = Enum.valueOf(Size.class, input);

        System.out.println("Size=" + size);
        System.out.println("Abbreviaiton=" + size.getAbbreviation());
    }
}
