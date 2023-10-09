package functional.streams;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static functional.streams._Stream.Gender.MALE;
import static functional.streams._Stream.Gender.FEMALE;

public class _Stream {
    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("Jhon", MALE),
                new Person("Maria", FEMALE),
                new Person("Aisha", FEMALE),
                new Person("Alex", MALE),
                new Person("Alice", FEMALE)
        );

        // stream is basically an abstract mode

        System.out.println("Get different genders with stream: ");
        people.stream()
              .map(person -> person.gender)
              .collect(Collectors.toSet())
              .forEach(System.out::println);

        System.out.println("Get the length of the names with streams: ");

        Function<Person, String> personStringFunction = person -> person.name;
        ToIntFunction<String> length = String::length;
        IntConsumer println = System.out::println;

        people.stream()
              .map(personStringFunction)
              .mapToInt(length)
              .forEach(println);


        System.out.println("Stream all match: ");
        boolean containsOnlyFemales = people.stream()
                                            .allMatch(person -> person.gender == FEMALE);
        System.out.println(containsOnlyFemales);

        System.out.println("Stream any match: ");
        boolean anyMatchFemale = people.stream()
                                            .anyMatch(person -> person.gender == FEMALE);
        System.out.println(anyMatchFemale);

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
