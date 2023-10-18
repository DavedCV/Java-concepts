package io;

import java.time.LocalDate;

public class Employee {
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

   @Override
   public String toString() {
      return "Employee{" +
              "name='" + name + '\'' +
              ", salary=" + salary +
              ", birth=" + birth +
              '}';
   }
}
