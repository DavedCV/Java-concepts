package functional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static functional.Basics.Gender.*;

public class Basics {
    public static void main(String[] args) {
        List<Person> poeple = List.of(
                new Person("Jhon", MALE),
                new Person("Maria", FEMALE),
                new Person("Aisha", FEMALE),
                new Person("Alex", MALE),
                new Person("Alice", FEMALE)
        );

        // Imperative approach to filter all females -------------------------------------------------------------------
        List<Person> females = new ArrayList<>();
        for (Person person : poeple) {
            if (person.gender == FEMALE) {
                females.add(person);
            }
        }

        // Print the females
        System.out.println("Imperative approach: ");
        for (Person female : females) {
            System.out.println(female);
        }

        // Declarative approach ----------------------------------------------------------------------------------------
        System.out.println("Declarative approach: ");
        poeple.stream()
              .filter(person -> FEMALE == person.gender)
              .collect(Collectors.toList())
              .forEach(System.out::println);
    }

    enum Gender {
        MALE,
        FEMALE
    }

    static class Person {
        private final String name;
        private final Gender gender;

        public Person(String name, Gender gender) {
            this.name = name;
            this.gender = gender;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", gender=" + gender +
                    '}';
        }
    }

}