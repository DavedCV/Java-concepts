package com.davidcv.spring28minuteslearn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

record Person (String name, int age) {};

record Address (String street, String place) {};

@Configuration
public class HelloWorldConfiguration {

    @Bean
    public String name() {
        return "David";
    }

    @Bean
    public int age() {
        return 21;
    }

    @Bean
    public Person person() {
        return new Person("David", 21);
    }

    @Bean
    public Person person2MethodCall() {
        return new Person(name(), age());
    }

    @Bean
    public Person person3Parameters(String name, int age) {
        return new Person(name, age);
    }

    @Bean(name = "customAddressName")
    public Address address() {
        return new Address("Baker Street", "London");
    }

}
