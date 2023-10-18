package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class TextFileTest {
    public static void main(String[] args) throws IOException {
        Employee[] staff = new Employee[3];

        staff[0] = new Employee("Carl Cracker", 75000.0, 1987, 12, 15);
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);

        // Save all employee records to the file employee.dat
        try (PrintWriter out = new PrintWriter("src/io/employee.dat", "UTF-8")) {
            writeData(staff, out);
        }

        // Retrieve new records into a new array
        try (Scanner in = new Scanner(new FileInputStream("src/io/employee.dat"), "UTF-8")) {
            Employee[] newStaff = readData(in);

            // print retrieved records
            for (Employee e : newStaff)
                System.out.println(e);
        }
    }

    private static void writeData(Employee[] employees, PrintWriter out) {
        // write number of employees
        out.println(employees.length);

        for (Employee e : employees)
            writeEmployee(e, out);
    }

    private static void writeEmployee(Employee e, PrintWriter out) {
        out.println(e.getName() + "|" + e.getSalary() + "|" + e.getBirth());
    }

    private static Employee[] readData(Scanner in) {
        // retrieve the array size
        int size = Integer.parseInt(in.nextLine());

        Employee[] newStaff = new Employee[size];

        System.out.println("Retrieved employee records: ");
        for (int i = 0; i < size; i++) {
            newStaff[i] = readEmployee(in);
        }

        return newStaff;
    }

    private static Employee readEmployee(Scanner in) {
        String[] employeeData = in.nextLine().split("\\|");

        String name = employeeData[0];
        double salary = Double.parseDouble(employeeData[1]);

        LocalDate birth = LocalDate.parse(employeeData[2]);
        int year = birth.getYear();
        int month = birth.getMonthValue();
        int day = birth.getDayOfMonth();

        return new Employee(name, salary, year, month, day);
    }
}