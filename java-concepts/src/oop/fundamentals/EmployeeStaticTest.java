package oop.fundamentals;

public class EmployeeStaticTest {
    public static void main(String[] args) {
        // fill the staff array with three Employee objects
        var staff = new EmployeeStatic[3];
        staff[0] = new EmployeeStatic("Tom", 40000);
        staff[1] = new EmployeeStatic("Dick", 60000);
        staff[2] = new EmployeeStatic("Harry", 65000);

        // print out information about all Employee objects
        for (EmployeeStatic e : staff) {
            e.setId();
            System.out.println("name=" + e.getName() + ",id=" + e.getId() + ",salary="
                                       + e.getSalary());
        }

        int n = EmployeeStatic.getNextId(); // calls static method
        System.out.println("Next available id=" + n);
    }
}

class EmployeeStatic {

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

}
