package org.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context
        var context = new AnnotationConfigApplicationContext();
        Parrot p = new Parrot();
    }
}