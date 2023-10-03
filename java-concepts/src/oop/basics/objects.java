package oop.basics;

import java.time.LocalDate;

public class objects {
    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        System.out.println("Current date: " + date);

        int today = date.getDayOfMonth();
        System.out.println("Start of month: "  +date.minusDays(today - 1));
    }
}
