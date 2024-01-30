package game;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App03GamingSpring {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GamingConfiguration.class);
        System.out.println(context.getBean("gameRunner"));
        ((GameRunner) context.getBean("gameRunner")).run();
    }
}
