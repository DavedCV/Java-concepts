package org.example.main;

import org.example.config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context and make Spring use the configuration class
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        // parrot 1 (use the bean identifier and the class type)
        Parrot p1 = context.getBean("parrot1", Parrot.class);
        System.out.println(p1.getName());

        // get bean with custom identifier
        Parrot riki = context.getBean("Riki", Parrot.class);
        System.out.println(riki.getName());

        // get primary or default bean of the same type
        Parrot primary = context.getBean(Parrot.class);
        System.out.println(primary.getName());

        String s = context.getBean(String.class);
        System.out.println(s);

        Integer n = context.getBean(Integer.class);
        System.out.println(n);

    }
}