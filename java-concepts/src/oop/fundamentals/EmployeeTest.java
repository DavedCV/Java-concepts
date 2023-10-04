package oop.fundamentals;

import java.time.LocalDate;

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

class Employee {
    private final LocalDate hireDay;
    private String name;
    private double salary;

    public Employee(String name, Double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.hireDay = LocalDate.of(year, month, day);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getHireDay() {
        return hireDay;
    }

    public void raiseSalary(double byPercent)
    {
        double raise = salary * byPercent / 100;
        salary += raise;
    }
}
