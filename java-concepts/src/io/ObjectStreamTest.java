package io;

import java.io.*;

public class ObjectStreamTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Employee harry = new Employee("Harry Hacker", 50000, 1989, 10, 1);

        Manager carl = new Manager("Carl Cracker", 80000, 1987, 12, 15);
        carl.setSecretary(harry);

        Manager tony = new Manager("Tony Tester", 40000, 1990, 3, 15);
        carl.setSecretary(harry);

        Employee[] staff = new Employee[3];
        staff[0] = harry;
        staff[1] = carl;
        staff[2] = tony;

        // save all employee records to the file ObjectStreamTest.dat
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/io/ObjectStreamTest.dat"))) {
            out.writeObject(staff);
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/io/ObjectStreamTest.dat"))) {
            // retrieve all records into a new array
            Employee[] newStaff = (Employee[]) in.readObject();

            // raise secretary's salary
            newStaff[0].raiseSalary(10);

            // print the newly read employee records
            for (Employee e : newStaff)
                System.out.println(e);
        }
    }
}
