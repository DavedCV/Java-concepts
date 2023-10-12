package lambdas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ConstructorReference {
    public static void main(String[] args) {
        ArrayList<String> names = new ArrayList<>(Arrays.asList("David", "Liz", "Mary", "Hernan"));
        Stream<Person> stream = names.stream().map(Person::new);
        List<Person> people = stream.toList();

        System.out.println(people);
    }
}

class Person {
    private final String name;
    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Object person: " + name;
    }
}
