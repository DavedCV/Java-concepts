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
    public Person person() {
        Person p = new Person();
        p.setName("Ella");

        // Setting the reference of the parrot
        // bean to the person’s parrot attribute
        p.setParrot(parrot());

        return p;
    }
}
