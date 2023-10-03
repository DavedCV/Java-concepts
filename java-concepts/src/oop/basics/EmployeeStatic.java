package oop.basics;

public class EmployeeStatic {

    private static int nextId = 1;

    private String name;
    private double salary;
    private int id;

    public EmployeeStatic(String n, double s) {
        name = n;
        salary = s;
        id = 0;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public int getId() {
        return id;
    }

    public void setId() {
        id = nextId; // set id to next available id
        nextId++;
    }

    public static int getNextId() {
        return nextId; // returns static field
    }

    // unit test
    public static void main(String[] args) {
        var e = new EmployeeStatic("Harry", 50000);
        System.out.println(e.getName() + " " + e.getSalary());
    }
}

