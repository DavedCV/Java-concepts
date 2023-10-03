package oop.basics;

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
