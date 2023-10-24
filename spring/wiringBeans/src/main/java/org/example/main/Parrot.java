package org.example.main;

public class Parrot {
    private String name;

    public Parrot() {
        System.out.println("Parrot created (this should be printed only 1)");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Parrot: " + name;
    }
}
