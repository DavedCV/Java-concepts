package oop.inheritance.object;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {
    private final LocalDate hireDay;
    private String name;
    private double salary;

    public Employee(String name, Double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.hireDay = LocalDate.of(year, month, day);
    }

    public static void main(String[] args) {
        Employee e = new Employee("David", 5000.0, 2023, 6, 4);
        System.out.println(e);
    }

    public LocalDate getHireDay() {
        return hireDay;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public void raiseSalary(double byPercent) {
        double raise = salary * byPercent / 100;
        salary += raise;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object otherObject) {
        // a quick test to see if the objects are equal
        if (this == otherObject) return true;

        // must return false if the explicit parameter is null
        if (otherObject == null) return false;

        // if the classes don't match they can't be equal
        if (getClass() != otherObject.getClass()) return false;

        // now we know otherObject is a non-null Employee
        Employee other = (Employee) otherObject;

        // test whether the fields have identical values.
        // The call Objects.equals(a, b) returns true if both arguments
        //are null, false if only one is null, and calls a.equals(b) otherwise.
        return Objects.equals(name, other.name)
                && salary == other.salary
                && Objects.equals(hireDay, other.hireDay);
    }

    // Your definition of equals and hashCode must be compatible
    @Override
    public int hashCode() {
        return Objects.hash(name, salary, hireDay);
    }

    public String toString() {
        return getClass().getName()
                + "[name=" + name
                + ", salary=" + salary
                + ", hireDay=" + hireDay
                + "]";
    }
}
