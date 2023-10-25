# The Spring context: Using abstractions

This topic is essential because in real-world projects, we often use abstractions to decouple implementations. We ensure our application is comfortable to maintain and test by decoupling implementations.

We’ll start with a refresher on how to use interfaces to define contracts in section. To approach this subject, we begin by discussing objects’ responsibilities and find out how they fit in a standard class design of an app. We’ll use our coding skills to implement a small scenario in which we don’t use Spring, but we focus on implementing a requirement and using abstractions to decouple the app’s dependent objects.

We then discuss Spring’s behavior when using DI with abstractions. We’ll start from the implementation we worked on and add Spring to the app’s dependencies. We then use the Spring context to implement dependency injection. With this example, we get closer to what you expect to find in production-ready implementations: objects with typical responsibilities for real-world scenarios and abstraction used with DI and the Spring context.

## Using interfaces to define contracts

In Java, the interface is an abstract structure you use to declare a specific responsibility. An object implementing the interface has to define this responsibility.

We can say that the interface specifies the “what needs to happen,” while every object implementing the interface specifies the “how it should happen.” Interfaces are the way objects ask for what they need.

### Using interfaces for decoupling implementations

An implementation example: Say you implement an object that needs to print packages’ details to be delivered for a shipping app. The printed details must be sorted by their destination address. The object dealing with printing the details needs to delegate to some other object the responsibility of sorting the packages by their delivery addresses.

![](/images/responsabilityDelegation.png)

the DeliveryDetailsPrinter directly delegates the sorting responsibility to the SorterByAddress object. If we keep this class design, we may face difficulties later if we need to change this functionality. Let’s imagine you need to change the printed details order later, and the new order is by the sender’s name. You’d need to replace the SorterByAddress object with another one implementing the new responsibility, but you’d also need to change the DeliveryDetailsPrinter object that uses the sorting responsibility

![](/images/responsabilityDelegationStronglyCoupling.png)

How can we improve this design? When changing an object’s responsibility, we want to avoid the need to change other objects using the changed responsibility. This design’s problem occurs because the DeliveryDetailsPrinter object specifies both what it needs and how it needs. As discussed earlier, an object only needs to specify what it needs and stay completely unaware of how the what is implemented.

Instead of declaring a SorterByAddress, the DeliveryDetailsPrinter object only specifies it needs a Sorter (interface). You can now have as many objects as you’d like to solve the what requested by the DeliveryDetailsPrinter. Any object implementing the Sorter interface can satisfy the dependency of the DeliveryDetailsPrinter object at any time.

![](/images/responsabilityDelegationLooselyCoupling.png)

```java
public interface Sorter {
  void sortDetails();
}
```

Because the DeliveryDetailsPrinter object depends on the interface instead of the implementation directly, you don’t need to change it further if you change the way the delivery details are sorted.

![](/images/responsabilityDelegationLooselyCoupling2.png)


With this theoretical introduction, you are now aware of why we use interfaces to decouple the objects that depend on each other in the class design.

### The requirement of the scenario

Say you are implementing an app a team uses to manage their tasks. One of the app’s features is allowing the users to leave comments for the tasks. When a user publishes a comment, it is stored somewhere (e.g., in a database), and the app sends an email to a specific address configured in the app.

We need to design the objects and find the right responsibilities and abstractions for implementing this feature.

### Implementing the requirement without using a framework

First, we need to identify the objects (responsibilities) to implement.

In standard real-world applications, we usually refer to the objects implementing uses cases as services, and that’s what we’ll do here. We’ll need a service that implements the “publish comment” use case. Let’s name this object CommentService. I prefer to give the service classes a name that ends with “service” so that their role in the project stands out.

When analyzing the requirement again, we observe that the use case consists of two actions: storing the comment and sending the comment by mail. As they are quite different from one another, we consider these actions to be two different responsibilities, and thus we need to implement two different objects.

When we have an object working directly with a database, we generally name such an object repository. Sometimes you also find such objects referred to as data access objects (DAO). Let’s name the object that implements the storing comment responsibility CommentRepository.

Finally, in a real-world app, when implementing objects whose responsibility is to establish communication with something outside the app, we name these objects proxies, so let’s name the object whose responsibility is sending the email CommentNotificationProxy.

![](/images/commentService.png)

We certainly want to make sure we decouple the CommentService from the implementations of its dependencies so that when we need to change the dependencies, we don’t need to change the object using them as well.

![](/images/commentServiceDecoupled.png)

Now that we have a clear picture of what we want to implement, let’s start coding it.

We’ll also have to represent the comment somehow. We just need to write a small POJO class for defining the comment. The responsibility of this type of object is simply to model the data the app uses, and we call it model.

> A POJO is a simple object without dependencies, only described by its attributes and methods.

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

We can now define the responsibilities of repository and proxy.

In the next listing, you can see the definition of the CommentRepository interface. The contract defined by this interface declares the storeComment(Comment comment) method, which the CommentService object needs to implement the use case.

```java
public interface  CommentRepository {
    void storeComment(Comment comment);
}
```

The interface only gives the what the CommentService object needs for implementing the use case: store a comment. When you define an object that implements this contract, it needs to override the storeComment(Comment comment) method to define the how.

In the next listing, you find the definition of the DBCommentRepository class. We don’t know yet how to connect to a database, so we’ll only write a text in the console to simulate this action.

```java
public class DBCommentRepository implements CommentRepository {
    @Override
    public void storeComment(Comment comment) {
        System.out.println("Storing comment: " + comment.getText());
    }
}
```

Similarly, we define an interface for the second responsibility the CommentService object needs: CommentNotificationProxy. We define this interface and the class implementing it in the proxies package of the project. The following listing presents this interface.

```java
public interface CommentNotificationProxy {
    void sendComment(Comment comment);
}
```

```java
public class EmailCommentNotificationProxy implements CommentNotificationProxy {
    @Override
    public void sendComment(Comment comment) {
        System.out.println("Sending notification for comment: " + comment.getText());
    }
}
```

We can now implement the object itself with the two dependencies of the CommentService object (the CommentRepository and the CommentNotificationProxy). In the service package, we write the CommentService class as presented in the following listing.

```java
public class CommentService {

    // We define the two dependencies as attributes of the class.
    private final CommentRepository commentRepository;
    private final CommentNotificationProxy commentNotificationProxy;


    // We provide the dependencies when the object is built through the parameters of the constructor.
    public CommentService(CommentRepository commentRepository, CommentNotificationProxy commentNotificationProxy) {
        this.commentRepository = commentRepository;
        this.commentNotificationProxy = commentNotificationProxy;
    }

    // We implement the use case that delegates the “store comment” and “send notification” responsibilities to the
    // dependencies.
    public void publishComment(Comment comment) {
        commentRepository.storeComment(comment);
        commentNotificationProxy.sendComment(comment);
    }
}
```

Let’s now write a Main class, as presented in the next listing, and test the whole class design.

```java
public class Main {
    public static void main(String[] args) {

        // Creates the instance for the dependencies
        CommentRepository commentRepository = new DBCommentRepository();
        CommentNotificationProxy commentNotificationProxy = new EmailCommentNotificationProxy();

        // Creates the instance of the service and providing the dependencies
        CommentService commentService = new CommentService(commentRepository, commentNotificationProxy);

        // Creates an instance of comment to send as a
        //parameter to the publish comment use case
        Comment comment = new Comment();
        comment.setAuthor("David");
        comment.setText("Demo comment");

        // Calls the use case
        commentService.publishComment(comment);
    }
}
```

## Using dependency injection with abstractions

In this section, we apply the Spring framework over the class design we implemented. Using this example, we can discuss how Spring manages dependency injection when using abstractions. This subject is essential because in most projects, you’ll implement dependencies between objects using abstractions.

We discussed dependency injection, and we used concrete classes to declare the variables where we wanted Spring to set the values of beans from its context. But as you’ll learn in this chapter, Spring also understands abstractions.

We’ll start by adding the Spring dependency to our project, and then we’ll decide which of the objects of this application need to be managed by Spring. You’ll learn to decide which objects you need to make Spring aware of.

We’ll focus on discussing various situations that can appear when using dependency injection with abstractions.

### Deciding which objects should be part of the Spring Context

Based on our discussion, you might think you need to add all the app objects in the Spring context, but this is not the case.

Remember, you learned that the main reason to add an object to the Spring context is to allow Spring to control it and further augment it with functionalities the framework provides. So the decision should be easy and based on the question, “Does this object need to be managed by the framework?”

It’s not difficult to answer this question for our scenario, as the only Spring feature we use is the DI. In our case, we need to add the object to the Spring context if it either has a dependency we need to inject from the context or if it’s a dependency itself.

Looking at our implementation, you’ll observe that the only object that doesn’t have a dependency and is also not a dependency itself is Comment. The other objects in our class design are as follows:
- CommentService—Has two dependencies, the CommentRepository and the CommentNotificationProxy
- DBCommentRepository—Implements the CommentRepository interface and is a dependency of the CommentService
- EmailCommentNotificationProxy—Implements the CommentNotificationProxy interface and is a dependency of the CommentService

Adding objects to the Spring context without needing the framework to manage them adds unnecessary complexity to your app, making the app both more challenging to maintain and less performant. When you add an object to the Spring context, you allow the framework to manage it with some specific functionality the framework provides. If you add the object to be managed by Spring without getting any benefit from the framework, you just over-engineer your implementation.

We discussed that using stereotype annotations (@Component) is the most comfortable way to add beans to the Spring context when the classes belong to your project, and you can change them. We’ll use this approach here as well.

We use stereotype annotations for the classes that Spring needs to create instances and add these instances to its context. It doesn’t make sense to add stereotype annotations on interfaces or abstract classes because these cannot be instantiated. Syntactically, you can do this, but it is not useful.

Let’s change the code and add the @Component annotation to these classes.

In the following listing, you find the change for the DBCommentRepository class:

```java
// Marking the class with @Component instructs Spring to
// instantiate the class and add an instance as a bean in
// its context.
@Component
public class DBCommentRepository implements CommentRepository {
    @Override
    public void storeComment(Comment comment) {
        System.out.println("Storing comment: " + comment.getText());
    }
}
```

In the next listing, you find the changes for the EmailCommentNotificationProxy class.

```java
@Component
public class EmailCommentNotificationProxy implements CommentNotificationProxy {
    @Override
    public void sendComment(Comment comment) {
        System.out.println("Sending notification for comment: " + comment.getText());
    }
}
```

In the next listing, we change the CommentService class as well by annotating it with @Component. The CommentService class declares the dependencies to the other two components through the interfaces CommentRepository and CommentNotificationProxy. Spring sees the attributes are defined with interface types and is smart enough to search in its context for beans created with classes that implement these interfaces.

```java

@Component
public class CommentService {

    // We define the two dependencies as attributes of the class.
    private final CommentRepository commentRepository;
    private final CommentNotificationProxy commentNotificationProxy;

    @Autowired
    // Spring uses this constructor to create the bean and injects
    // references from its context in the parameters when creating the
    // instance.
    public CommentService(CommentRepository commentRepository, CommentNotificationProxy commentNotificationProxy) {
        this.commentRepository = commentRepository;
        this.commentNotificationProxy = commentNotificationProxy;
    }

    // We implement the use case that delegates the “store comment” and “send notification” responsibilities to the
    // dependencies.
    public void publishComment(Comment comment) {
        commentRepository.storeComment(comment);
        commentNotificationProxy.sendComment(comment);
    }
}
```

We only need to tell Spring where to find the classes annotated with stereotype annotations and test the app. The next listing presents the project’s configuration class where we use the @ComponentScan annotation to tell Spring where to find the classes annotated with @Component.

```java
@Configuration
@ComponentScan(basePackages = {"org.example.proxies", "org.example.repositories", "org.example.services"})
public class ProjectConfiguration {
}
```

To test our setup, let’s create a new main method, as presented in the following listing. We’ll spin the Spring context, grab the bean of type CommentService out of it, and call the publishComment(Comment comment) method.

```java
public class Main {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        Comment comment = new Comment();
        comment.setAuthor("David");
        comment.setText("Demo comment");

        CommentService commentService = context.getBean(CommentService.class);
        commentService.publishComment(comment);
    }
}
```

It’s a small example, and it might not look like Spring improves a lot the experience, but look again. By using the DI feature, we don’t create the instance of the CommentService object and its dependencies ourselves, and we don’t need to explicitly make the relationship between them.

In a real-world scenario, where you have more than three classes, letting Spring manage the objects and dependencies among them really makes a difference.
