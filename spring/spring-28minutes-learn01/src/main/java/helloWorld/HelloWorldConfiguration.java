package helloWorld;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

record Person(String name, int age) {
};

record Address(String street, String place) {
};

record PersonWAddress(String name, int age, Address address) {
};


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

    @Bean
    public PersonWAddress personWAddressQualifier(
            String name,
            int age,
            @Qualifier("address2qualifier") Address address
    )
    {
        return new PersonWAddress(name, age, address);
    }

    @Bean(name = "customAddressName")
    @Primary
    public Address address() {
        return new Address("Baker Street", "London");
    }

    @Bean
    @Qualifier("address2qualifier")
    public Address address2() {
        return new Address("Baker Street", "Manchester");
    }

}
