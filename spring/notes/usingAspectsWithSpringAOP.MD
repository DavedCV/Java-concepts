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

#### STEP 1: ENABLING THE ASPECTS MECHANISM FOR YOUR APPLICATION

For the first step, you need to tell Spring you’ll use aspects in your app. Whenever you use a specific mechanism provided by Spring, you have to explicitly enable it by annotating your configuration class with a particular annotation. In most cases, the names of
these annotations start with “Enable.”

In this example, we need to use the @EnableAspectJAutoProxy annotation to enable the aspect capabilities.

```java
@Configuration
@ComponentScan(basePackages = "org.example.services")
// Enables the aspects mechanism in our Spring app
@EnableAspectJAutoProxy
public class ProjectConfiguration {
}
```

#### STEP 2: CREATE A CLASS THAT DEFINES THE ASPECT, AND ADD AN INSTANCE FOR THIS CLASS IN THE SPRING CONTEXT

We need to create a new bean in the Spring context that defines the aspect. This object holds the methods, which will intercept specific method calls and augment them with specific logic.

```java
@Aspect
public class LoggingAspect {
    public void log() {
        // To implement later
    }
}
```

You can use any of the approaches we learned to add an instance of this class to the Spring context.

```java
@Configuration
@ComponentScan(basePackages = "org.example.services")
// Enables the aspects mechanism in our Spring app
@EnableAspectJAutoProxy
public class ProjectConfiguration {
    @Bean
    // Adds an instance of the LoggingAspect class to the Spring context
    public LoggingAspect aspect() {
        return new LoggingAspect();
    }
}
```

Remember, you need to make this object a bean in the Spring context because Spring needs to know about any object it needs to manage. Also, the @Aspect annotation isn’t a stereotype annotation. Using @Aspect, you tell Spring that the class implements the definition of an aspect, but Spring won’t also create a bean for this class.

#### STEP 3: USE AN ADVICE ANNOTATION TO TELL SPRING WHEN AND WHICH METHOD CALLS TO INTERCEPT

Now that we have defined the aspect class, we choose the advice and annotate the method accordingly.

```java
@Aspect
public class LoggingAspect {

    @Around("execution(* org.example.services.*.*(..))") // Defines which are the intercepted methods
    public void log(ProceedingJoinPoint joinPoint) {
        // Delegates to the actual intercepted method
        joinPoint.proceed();
    }
}
```

Other than using the @Around annotation, you also observe I’ve written an unusual string expression as the value of the annotation, and I have added a parameter to the aspect method. What are these?

- The peculiar expression used as a parameter to the @Around annotation tells Spring which method calls to intercept. Don’t be intimidated by this expression! This expression language is called AspectJ pointcut language, and you won’t need to learn it by heart to use it. In practice, you don’t use complex expressions.

    Theoretically, you can write very complex AspectJ pointcut expressions to identify a particular set of method calls to be intercepted. This language is really powerful. But it’s always better to avoid writing complex expressions. In most cases, you can find simpler alternatives.

    ![](/images/adviceNotation.png)

    It means Spring intercepts any method defined in a class that is in the services package, regardless of the method’s return type, the class it belongs to, the name of the method, or the parameters the method receives.

- Now let’s look at the second element I’ve added to the method: the ProceedingJoinPoint parameter, which represents the intercepted method. The main thing you do with this parameter is tell the aspect when it should delegate further to the actual method.

#### STEP 4: IMPLEMENT THE ASPECT LOGIC

```java
@Aspect
public class LoggingAspect {

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("execution(* org.example.services.*.*(..))") // Defines which are the intercepted methods
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        // Prints a message in the console before the intercepted method’s execution
        logger.info("Method will execute");
        // Delegates to the actual intercepted method
        joinPoint.proceed();
        // Prints a message in the console after the intercepted method’s execution
        logger.info("Method executed");
    }
}
```

Now the aspect:

1. Intercepts the method
2. Displays something in the console before calling the intercepted method
3. Calls the intercepted method
4. Displays something in the console after calling the intercepted method

![](/images/aspectBehavior.png)

The method proceed() of the ProceedingJoinPoint parameter calls the intercepted method, publishComment(), of the CommentService bean. If you don’t call proceed(), the aspect never delegates further to the intercepted method.

You can even implement logic where the actual method isn’t called anymore. For example, an aspect that applies some authorization rules decides whether to delegate further to a method the app protects. If the authorization rules aren’t fulfilled, the aspect doesn’t delegate to the intercepted method it protects.

Also, observe that the proceed() method throws a Throwable. The method proceed() is designed to throw any exception coming from the intercepted method. In this example, I chose the easy way to propagate it further, but you can use a try-catch-finally block to treat this throwable if you need it.
