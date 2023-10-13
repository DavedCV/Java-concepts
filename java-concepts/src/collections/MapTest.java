package collections;

import java.util.HashMap;

public class MapTest {

    public static void main(String[] args) {
        var staff = new HashMap<String, Employee>();
        staff.put("144-25-5464", new Employee("Amy Lee"));
        staff.put("567-24-2546", new Employee("Harry Hacker"));
        staff.put("157-62-7935", new Employee("Gary Cooper"));
        staff.put("456-62-5527", new Employee("Francesca Cruz"));

        // print all entries
        System.out.println(staff);

        // remove an entry
        staff.remove("567-24-2546");

        // replace an entry
        staff.put("456-62-5527", new Employee("Francesca Miller"));

        // lookup a value
        System.out.println(staff.get("157-62-7935"));

        // iterate through all entries
        staff.forEach((key, value) -> System.out.println("Key=" + key + ", Value=" + value));
    }
}

class Employee {
    private final String name;

    public Employee(String name) {
        this.name = name;
    }
}