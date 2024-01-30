package com.davidcv.spring28minuteslearn.game;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.davidcv.spring28minuteslearn.game")
public class App04GamingSpring {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App04GamingSpring.class);
        System.out.println(context.getBean("gameRunner"));
        ((GameRunner) context.getBean("gameRunner")).run();
    }
}
