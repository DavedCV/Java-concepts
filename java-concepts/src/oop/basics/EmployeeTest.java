package oop.basics;

public class EmployeeTest {

    public static void main(String[] args) {
        Employee[] staff = new Employee[3];
        staff[0] = new Employee("juan", 2000.0, 2023, 8, 1);
        staff[1] = new Employee("felipe", 2000.0, 2023, 8, 1);
        staff[2] = new Employee("juanjo", 2000.0, 2023, 8, 1);

        // raise everyone's salary by 5%
        for (Employee e : staff)
            e.raiseSalary(5);

        // print out information about all Employee objects
        for (Employee e : staff)
            System.out.println("name=" + e.getName()
                                       + ",salary=" + e.getSalary()
                                       + ",hireDay=" + e.getHireDay());

    }
}
