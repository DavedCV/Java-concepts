package com.davidcv.spring28minuteslearn.launcher.a0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/*
* Initialization of beans
* */

@Component
class ClassA {
}

@Component
@Lazy // Initialize the bean when the object is required, not at startup of spring context
class ClassB {
    private ClassA classA;

    @Autowired
    public ClassB(ClassA classA) {
        System.out.println("Some initialization logic in Class B");
        this.classA = classA;
    }
}

@Configuration
@ComponentScan
public class LazyInitializationLauncherApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(LazyInitializationLauncherApplication.class);

        System.out.println("Initialization of context is completed");

        // Now we require the bean of ClassB - Then is initialized
        System.out.println(context.getBean(ClassB.class));
    }
}
