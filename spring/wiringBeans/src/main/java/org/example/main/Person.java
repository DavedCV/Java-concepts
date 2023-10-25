package org.example.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Person {
    private final Parrot parrot;

    // We can now make the field final to ensure its
    // value cannot be changed after initialization.
    private String name = "Ella";

    // We use the @Autowired annotation
    // over the constructor.
    @Autowired
    public Person(Parrot parrot) {
        this.parrot = parrot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parrot getParrot() {
        return parrot;
    }
}
