package io;

import java.io.*;
import java.time.LocalDate;

public class RandomAccessTest {

    final static int NAME_SIZE = 40; // 80 bytes = 40 ch
    final static int RECORD_SIZE = 100; // 80 bytes name, 8 bytes salary, 12 bytes date

    public static void main(String[] args) {
        Employee[] staff = new Employee[3];

        staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("src/io/RandomAccessTest.dat"))) {
            // save all employees to the data file
            for (Employee e : staff) {
                writeData(out, e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (RandomAccessFile in = new RandomAccessFile("src/io/RandomAccessTest.dat", "r")) {
            // retrieve all records into a new array

            // compute the array size
            int n = (int) (in.length() / RECORD_SIZE);
            Employee[] newStaff = new Employee[n];

            // read employees in reverse order
            for (int i = n - 1; i >= 0; i--) {
                // move the cursor
                in.seek((long) i * RECORD_SIZE);
                newStaff[i] = readData(in);
            }

            // print the retrieved employee records
            for (Employee e : newStaff)
                System.out.println(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Writes employee data to a data output
     * */
    private static void writeData(DataOutput out, Employee e) throws IOException {
        DataIO.writeFixedString(e.getName(), NAME_SIZE, out);

        out.writeDouble(e.getSalary());

        LocalDate birth = e.getBirth();
        out.writeInt(birth.getYear());
        out.writeInt(birth.getMonthValue());
        out.writeInt(birth.getDayOfMonth());
    }

    /*
    * Reads employee data from a data input
    * */
    private static Employee readData(DataInput in) throws IOException {
        String name = DataIO.readFixedString(NAME_SIZE, in);

        double salary = in.readDouble();
        int y = in.readInt();
        int m = in.readInt();
        int d = in.readInt();

        return new Employee(name, salary, y, m, d);
    }
}

class DataIO {

    /*
     * The writeFixedString writes the specified number of code units, starting at the
     * beginning of the string. If there are too few code units, the method pads the string,
     * using zero values.
     * */
    public static void writeFixedString(String s, int size, DataOutput out) throws IOException {
        for (int i = 0; i < size; i++) {
            char ch = 0;
            if (i < s.length()) ch = s.charAt(i);
            out.writeChar(ch);
        }
    }

    /*
     * The readFixedString method reads characters from the input stream until it has
     * consumed size code units or until it encounters a character with a zero value. Then,
     * it skips past the remaining zero values in the input field.
     * */
    public static String readFixedString(int size, DataInput in) throws IOException {
        StringBuilder b = new StringBuilder(size);
        int i = 0;
        boolean more = true;

        while (more && i < size) {
            char ch = in.readChar();
            i++;

            if (ch == 0) more = false;
            else b.append(ch);
        }

        // skip the remaining 0 values
        in.skipBytes(2 * (size - i));

        return b.toString();
    }
}
