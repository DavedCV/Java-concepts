package oop.fundamentals;

import java.util.Random;

public class EmployeeConstructorTest {
    public static void main(String[] args) {
        // fill the staff array with three Employee objects
        var staff = new EmployeeConstructor[3];

        staff[0] = new EmployeeConstructor("Harry", 40000);
        staff[1] = new EmployeeConstructor(60000);
        staff[2] = new EmployeeConstructor();

        // print out information about all Employee objects
        for (EmployeeConstructor e : staff)
            System.out.println("name=" + e.getName() + ",id=" + e.getId() + ",salary="
                                       + e.getSalary());
    }
}

class EmployeeConstructor {

    private static int nextId;

    // static initialization block
    static {
        var generator = new Random();
        nextId = generator.nextInt(10000);
    }

    private int id;
    private String name = ""; // instance field initialization
    private double salary;

    // object initializator block
    {
        id = nextId;
        nextId++;
    }

    // three overloaded constructors
    public EmployeeConstructor() {
        // name initialized to "" --see above
        // salary not explicitly set --initialized to 0
        // id initialized in initialization block
    }

    public EmployeeConstructor(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public EmployeeConstructor(double salary) {
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}

