package io;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee extends SerialCloneable implements Serializable {
    private String name;
    private double salary;
    private LocalDate birth;

    public Employee(String name, double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.birth = LocalDate.of(year, month, day);
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getBirth() {
        return birth;
    }

    /**
     * Raises the salary of this employee.
     *
     * @byPercent the percentage of the raise
     */
    public void raiseSalary(double byPercent)
    {
        double raise = salary * byPercent / 100;
        salary += raise;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                ", birth=" + birth +
                '}';
    }
}
