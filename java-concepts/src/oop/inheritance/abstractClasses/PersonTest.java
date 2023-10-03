package oop.inheritance.abstractClasses;

public class PersonTest {
    public static void main(String[] args) {
        var people = new Person[2];

        people[0] = new Employee("Joan", 5000.0, 2023, 10, 2);
        people[1] = new Student("David", "Computer Science");

        for (Person p : people) {
            System.out.println(p.getName() + ", " + p.getDescription());
        }
    }
}
