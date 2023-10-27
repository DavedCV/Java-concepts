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

### Altering the intercepted method’s parameters and the returned value

Aspects are really powerful. Not only can they intercept a method and alter its execution, but they can also intercept the parameters used to call the method and possibly alter them or the value the intercepted method returns.

Knowing how to do this gives you even more opportunities in what you can implement using aspects.

Suppose you want to log the parameters used to call the service method and what the method returned.

Changing the return of the method:

```java
@Service
public class CommentService {

    // To log a message in the app’s console every time
    // someone calls the use case, we use a logger object.
    private Logger logger = Logger.getLogger(CommentService.class.getName());

    //  This method defines
    //  the use case for our
    //  demonstration.
    public String publishComment(Comment comment) {
        logger.info("Publishing comment: " + comment.getText());
        return "SUCESS";
    }
}
```

The aspect can easily find the name of the intercepted method and the method parameters. Remember that the **ProceedingJoinPoint** parameter of the  aspect method represents the intercepted method. You can use this parameter to  get any information related to the intercepted method (parameters, method name, target object, and so on).

Now we can change the aspect also to log the details we need:

```java
@Aspect
public class LoggingAspect {

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("execution(* org.example.services.*.*(..))") // Defines which are the intercepted methods
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        // Obtains the name and parameters of the intercepted method
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        // Logs the name and parameters of the intercepted method
        logger.info("Method " + methodName + " with parameters " + Arrays.asList(arguments) + " will execute");

        // Delegates to the actual intercepted method
        Object returnedByMethod = joinPoint.proceed();

        logger.info("Method executed and returned " + returnedByMethod);

        // Returns the value returned by the intercepted method
        return returnedByMethod;
    }
}
```

![](/images/aspectLOgsMethodInfo.png)

But aspects are even more powerful. They can alter the execution of the intercepted method by:

- Changing the value of the parameters sent to the method
- Changing the returned value received by the caller
- Throwing an exception to the caller or catching and treating an exception thrown by the intercepted method

You can be extremely flexible in altering the call of an intercepted method. You can even change its behavior completely. But be careful! When you alter the logic through an aspect, you make a part of the logic transparent.

The whole idea of decoupling a part of the logic is to avoid duplicating code and hide what’s irrelevant, so a developer can easily focus on the business logic code. When considering writing an aspect, put yourself in the developer’s shoes. Someone who needs to understand the code should easily realize what’s happening.

The following listing shows that when you call the proceed() method without sending any parameters, the aspect sends the original parameters to the intercepted method. But you can choose to provide a parameter when calling the proceed() method. This parameter is an array of objects that the aspect sends to the intercepted method instead of the original parameter values. The aspect logs the value returned by the intercepted method, but it returns to the caller a different value.

```java
@Aspect
public class LoggingAspect {

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("execution(* org.example.services.*.*(..))") // Defines which are the intercepted methods
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        // Obtains the name and parameters of the intercepted method
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        // Logs the name and parameters of the intercepted method
        logger.info("Method " + methodName + " with parameters " + Arrays.asList(arguments) + " will execute");

        Comment comment = new Comment();
        comment.setText("Some other text!");
        Object[] newArgs = {comment};

        // Delegates to the actual intercepted method with different parameters
        Object returnedByMethod = joinPoint.proceed(newArgs);

        logger.info("Method executed and returned " + returnedByMethod);

        // Returns a different value, not the actual one returned by the target method
        return "FAILED";
    }
}
```

> Be careful with using aspects! You should only use them to hide irrelevant lines of code that can easily be implied. Aspects are so powerful they can bring you to the “dark side” of hiding relevant code and make your app more difficult to maintain. Use aspects with caution!

### Intercepting annotated methods

You can also use annotations to mark the methods you want an aspect to intercept with a comfortable syntax that allows you also to avoid writing complex AspectJ pointcut expressions.

In the CommentService class, we’ll add three methods: publishComment(), deleteComment(), and editComment(). We want to define a custom annotation and log only the execution of the methods we mark using the custom annotation. To achieve this objective, you need to do the following:

1. Define a custom annotation, and make it accessible at runtime. We’ll call this annotation @ToLog.
2. Use a different AspectJ pointcut expression for the aspect method to tell the aspect to intercept the methods annotated with the custom annotation.

![](/images/interceptAnnotatedMethods.png)


In the next code snippet, you find the declaration of the custom annotation. The definition of the retention policy with @Retention(RetentionPolicy.RUNTIME) is critical. By default, in Java annotations cannot be intercepted at runtime. You need to explicitly specify that someone can intercept annotations by setting the retention policy to RUNTIME. The @Target annotation specifies which language elements we can use this annotation for. By default, you can annotate any language elements, but it’s always a good idea to restrict the annotation to only what you make it for—in our case, methods:

```java
```
