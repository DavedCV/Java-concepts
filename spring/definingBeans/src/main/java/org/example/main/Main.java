package org.example.main;

import org.example.config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context and make Spring use the configuration class
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        // we create the instance we want to add programmatically
        Parrot x = new Parrot();
        x.setName("Kiki");

        // We define a Supplier to return this instance.
        Supplier<Parrot> parrotSupplier = () -> x;

        // We call the registerBean() method to add the instance to the Spring context.
        context.registerBean("parrot1", Parrot.class, parrotSupplier);
        
        // To verify the bean is now in the
        //context, we refer to the parrot bean
        //and print its name in the console.
        Parrot p = context.getBean(Parrot.class);
        System.out.println(p.getName());
    }
}