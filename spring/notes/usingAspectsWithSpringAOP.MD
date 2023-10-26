# Using aspects with Spring AOP

Thus far, we have discussed the Spring context, and the only Spring capability we have used is DI, which is supported by the IoC principle. With DI, the framework manages objects you define, and you can request to use these objects where you need them. 

As we discussed, to request a bean’s reference, in most cases, you use the @Autowired annotation. When you request such an object from the Spring context, we say that Spring “injects” the object where you requested it. In this chapter, you’ll learn how to use another powerful technique supported by the IoC principle: **aspects**.

Aspects are a way the framework intercepts method calls and possibly alters the execution of methods. You can affect the execution of specific method calls you select. This technique helps you extract part of the logic belonging to the executing method.

In certain scenarios, decoupling a part of the code helps make that method easier to understand. It allows the developer to focus only on the relevant details discussed when reading the method logic.

This approach is called **aspect-oriented programming (AOP)**.

![](/images/AspectsVisualExample.png)

Another important reason for learning aspects is that Spring uses them in implementing a lot of the crucial capabilities it offers. Understanding how the framework works can save you many hours of debugging later when you face a specific problem.

## 6.1 - How aspects work in Spring

An aspect is simply a piece of logic the framework executes when you call specific methods of your choice. When designing an aspect, you define the following:

- *What* code you want Spring to execute when you call specific methods. This is named an **aspect**.
- *When* the app should execute this logic of the aspect (e.g., before or after the method call, instead of the method call). This is named the **advice**.
- *Which* methods the framework needs to intercept and execute the aspect for them. This is named a **pointcut**.

With aspects terminology, you’ll also find the concept of a join point, which defines the event that triggers the execution of an aspect. But with Spring, this event is always a method call.

As in the case of the dependency injection, to use aspects you need the framework to manage the objects for which you want to apply aspects. You’ll use the approaches learned to add beans to the Spring context to enable the framework to control them and apply aspects you define. The bean that declares the method intercepted by an aspect is named the **target object**.

![](/images/aspectsTerminology.png)

But how does Spring intercept each method call and apply the aspect logic? As discussed earlier in this section, the object needs to be a bean in the Spring context. But because you made the object an aspect target, Spring won’t directly give you an instance reference for the bean when you request it from the context. Instead, Spring gives you an object that calls the aspect logic instead of the actual method. We say that Spring gives you a *proxy* object instead of the real bean. This approach is named **weaving**.

Calling an aspected method assumes you call the method through the proxy object provided by Spring. The proxy applies the aspect logic and delegates the call to the actual method.

## 6.2 - Implementing aspects with Spring AOP

We’ll consider a scenario and implement its requirements with aspects.

Suppose you have an application that implements multiple use cases in its service classes. Some new regulations require your app to store the time it started and ended for each use case execution. In your team, you decided to take responsibility for implementing a functionality to log all the events where a use case begins and ends.

### Implementing a simple aspect

In this section, we discuss implementing a simple aspect to solve our scenario. We’ll create a new project and define a service class containing a method that we’ll use to test our implementation and prove the aspect we define works as desired in the end.

To use aspects we need the **spring-aspects** dependency:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>6.0.13</version>
</dependency>
```

we’ll only consider one service object named CommentService and a use case it defines named publishComment(Comment comment). This method, defined in the CommentService class, receives a parameter of type Comment.

```java
public class Comment {
    private String author;
    private String text;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
```

When declaring a logger object, you need to give it a name as a parameter. This name then appears in the logs and makes it easy for you to observe the log message source. Often, we use the class name.

```java
@Service
public class CommentService {

    // To log a message in the app’s console every time
    // someone calls the use case, we use a logger object.
    private Logger logger = Logger.getLogger(CommentService.class.getName());

    //  This method defines
    //  the use case for our
    //  demonstration.
    public void publishComment(Comment comment) {
        logger.info("Publishing comment: " + comment.getText());
    }
}
```

```java
@Configuration
@ComponentScan(basePackages = "org.example.services")
public class ProjectConfiguration {
}
```

Let’s write the Main class that calls the publishComment() method in the service class and observe the current behavior:

```java
public class Main {
    public static void main(String[] args) {
        var c = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        var service = c.getBean(CommentService.class);

        //  Creates a Comment instance
        //  to give as a parameter to the
        //  publishComment() method
        Comment comment = new Comment();
        comment.setText("Demo comment");
        comment.setAuthor("Natasha");

        // Calls the publishComment() method
        service.publishComment(comment);
    }
}
```

This is how the app looks before we solve the example we discussed.  Remember, we need to print messages in the console before and after the service method call. 

Let’s now enhance the project with an aspect class that intercepts the method call and adds an output before and after the call. To create an aspect, you follow these steps:

1. Enable the aspect mechanism in your Spring app by annotating the configuration class with the @EnableAspectJAutoProxy annotation.
2.  Create a new class, and annotate it with the @Aspect annotation. Using either @Bean or stereotype annotations, add a bean for this class in the Spring context.
3. Define a method that will implement the aspect logic and tell Spring when and which methods to intercept using an advice annotation.
4. Implement the aspect logic.

![](/images/createAspectSteps.png)
