package org.example.config;

import org.example.main.Parrot;
import org.example.main.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public Parrot parrot() {
        Parrot p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    // We instruct Spring to provide a bean
    //from its context by defining a parameter
    //for the method.
    public Person person(Parrot parrot) {
        Person p = new Person();
        p.setName("Ella");

        //We set the value of the person’s
        //attribute with the reference
        //Spring provided.
        p.setParrot(parrot);

        return p;
    }
}
