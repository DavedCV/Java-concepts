package org.example.main;

import org.example.config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context and make Spring use the configuration class
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        Parrot p = context.getBean(Parrot.class);

        // Prints the default String representation of the instance taken from the Spring context
        System.out.println(p);

        // Prints null because we did
        //not assign any name to the
        //parrot instance added by
        //Spring in its context
        System.out.println(p.getName());

    }
}