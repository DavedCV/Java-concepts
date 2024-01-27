package com.davidcv.spring28minuteslearn;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App02HelloWorldSpring {
    public static void main(String[] args) {
        // 1. Launch a Spring Context
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HelloWorldConfiguration.class);

        // 2. Configure the things that we want spring to manage -
        // HelloWorldConfiguration - @Configuration
        // name - Bean

        // 3. Retrieve beans managed by spring
        System.out.println(context.getBean("name"));
        System.out.println(context.getBean("age"));
        System.out.println(context.getBean("person"));
        System.out.println(context.getBean("person2MethodCall"));
        System.out.println(context.getBean("person3Parameters"));
        System.out.println(context.getBean("customAddressName"));
        System.out.println(context.getBean(Address.class));
    }
}
