package org.example.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class Person {
    private Parrot parrot;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parrot getParrot() {
        return parrot;
    }

    public void setParrot(Parrot parrot) {
        this.parrot = parrot;
    }
}
