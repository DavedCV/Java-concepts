# The Spring context: Bean scopes and life cycle

Thus far we have discussed several essential things about object instances managed by Spring (beans). We covered the important syntaxes we need to know to create beans, and we discussed establishing relationships among beans (including the necessity of using abstractions).

But we didn’t focus on how and when Spring creates the beans. From this perspective, we’ve only relied on the framework’s default approaches. Production apps’ scenarios are complex, and sometimes relying on the framework’s default behavior is not enough.

Spring has multiple different approaches for creating beans and managing their life cycle, and in the Spring world we name these approaches **scopes**. In this chapter, we discuss two scopes you’ll often find in Spring apps: **singleton** and **prototype**.

Singleton is the default scope of a bean in Spring, and it’s what we’ve been using up to now. 

## 5.1 - Using the singleton bean scope

The singleton bean scope defines Spring’s default approach for managing the beans in its context. It is also the bean scope you’ll most encounter in production apps.

### How singleton beans work

Spring creates a singleton bean when it loads the context and assigns the bean a name (sometimes also referred to as bean ID). **We name this scope singleton because you always get the same instance when you refer to a specific bean**. But be careful! You can have more instances of the same type in the Spring context if they have different names.

#### DECLARING SINGLETON-SCOPED BEANS WITH @BEAN

Let’s demonstrate a singleton bean’s behavior with an example using the @Bean annotation to add an instance to the Spring context and then simply refer to it multiple times in a main class. We do this to prove we get the same instance every time we refer to the bean.

![](/images/singletonScopeBeansWith@Bean.png)

The coffee bean in the visual represents the instance that Spring adds to its context.Observe that the context contains only one instance (coffee bean) with an associated name. As we discussed, when using the @Bean annotation approach to add a bean to the context, the name of the method annotated with @Bean becomes the bean’s name.

Is the same if we use Stereotype Annotations to create the bean. Singleton bean is not dependant of using the @Bean annotation definition approach.

Also, in the case of this example, we can get the bean without the name parameter. In this case is only to be more robust. 

Example using bean annotations to get the same behavior:

```java
public class CommentService {
}
```

```java
@Configuration
public class ProjectConfiguration {

    @Bean
    public CommentService commentService() {
        return new CommentService();
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        var cs1 = context.getBean("commentService", CommentService.class);
        var cs2 = context.getBean("commentService", CommentService.class);

        // The singleton bean scope always return the same instance
        boolean b1 = cs1 == cs2;

        System.out.println(b1);
    }
}
```

#### DECLARING SINGLETON BEANS USING STEREOTYPE ANNOTATIONS

As mentioned earlier, Spring’s behavior for singleton beans isn’t any different when using stereotype annotations than when you declared them with the @Bean annotation.

Consider a class design scenario where two service classes depend on a repository. Say we have both CommentService and UserService depending on a repository named CommentRepository.

![](/images/singletonBeansWithStereotypeScenario.png)

Let’s prove this behavior by creating the three classes and comparing the references Spring injects in the service beans. Spring injects the same reference in both service beans.

![](/images/singletonScopeBeanWithStereotypeAnnotations.png)

```java
@Repository
public class CommentRepository {
}
```

```java
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentRepository getCommentRepository() {
        return commentRepository;
    }
}
```

```java
@Service
public class UserService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentRepository getCommentRepository() {
        return commentRepository;
    }
}
```

Unlike the @Bean example, the configuration class remains empty in this project. We only need to tell Spring where to find the classes annotated with stereo- type annotations.

```java
@Configuration
@ComponentScan(basePackages = {"org.example.repositories", "org.example.services"})
public class ProjectConfiguration {
}
```

In the Main class, we get the references for the two services, and we compare their dependencies to prove that Spring injected the same instance in both.

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        var s1 = context.getBean(CommentService.class);
        var s2 = context.getBean(UserService.class);

        //  Compares the references for the repository dependency injected by Spring
        boolean b1 = s1.getCommentRepository() == s2.getCommentRepository();

        //  Because the dependency (CommentRepository)
        //  is singleton, both services contain the same
        //  reference, so this line always prints “true.”
        System.out.println(b1);
    }
}
```

### Singleton beans in real-world scenarios

Let’s start by considering some scenarios where you should or shouldn’t use singleton beans.

- Because the singleton bean scope assumes that multiple components of the app can share an object instance, the most important thing to consider is that these beans must be immutable. Most often, a real-world app executes actions on multiple threads (e.g., any web app). In such a scenario, multiple threads share the same object instance. If these threads change the instance, you encounter a race-condition scenario.

  A race condition is a situation that can happen in multithreaded architectures when multiple threads try to change a shared resource. In case of a race condition, the developer needs to properly synchronize the threads to avoid unexpected execution results or errors.

- Remember that constructor DI is a good practice and preferred over field injection. One of the advantages of constructor injection is that it allows you to make the instance immutable (define the bean’s fields as final). In our previous example, we can enhance the CommentService class’s definition by replacing the field injection with constructor injection.

```java
@Service
public class CommentService {
    private final CommentRepository commentRepository;
  
    // Making the field final
    // highlights that this field was
    // intended not to be changed.
    public CommentService(
      CommentRepository commentRepository
    ) {
        this.commentRepository = commentRepository;
    }
  
    public CommentRepository getCommentRepository() {
        return commentRepository;
    }
}
```

### Using eager and lazy instantiation

In most cases, Spring creates all singleton beans when it initializes the context; this is Spring’s default behavior. We’ve used only this default behavior, which is also called **eager instantiation**.

We discuss a different approach of the framework, **lazy instantiation**. With lazy instantiation, Spring doesn’t create the singleton instances when it creates the context. Instead, it creates each instance the first time someone refers to the bean.

Let’s take an example to observe the difference between the approaches and then discuss the advantages and disadvantages of using them in production apps.

Eager instantiation works the same as we'been working, we need to keep in mind that the beans are created at the same time of the context creation.

The lazy approach is achieved as follows:

```java
@Service
//  The @Lazy annotation tells Spring that it
//  needs to create the bean only when someone
//  refers to the bean for the first time.
@Lazy
public class CommentService {
    public CommentService() {
        System.out.println("CommentService instance created!");
    }
}
```

```java
@Configuration
@ComponentScan(basePackages = "org.example.services")
public class ProjectConfiguration {
}
```

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        System.out.println("Before retrieving the CommentService");
        //  At this line, where Spring needs to provide a
        //  reference to the CommentService bean,
        //  Spring also creates the instance
        var service = context.getBean(CommentService.class);
        System.out.println("After retrieving the CommentService");
    }
}
```

When should you use eager instantiation and when should you use lazy?

- In most cases, it’s more comfortable to let the framework create all the instances at the beginning when the context is instantiated (eager); this way, when one instance delegates to another, the second bean already exists in any situation.

  In a lazy instantiation, the framework has to first check if the instance exists and eventually create it if it doesn’t, so from the performance point of view, it’s better to have the instances in the context already (eager) because it spares some checks the framework needs to do when one bean delegates to another.
- Another advantage of eager instantiation is when something is wrong and the framework cannot create a bean; we can observe this issue when starting the app. With lazy instantiation, someone would observe the issue only when the app is already executing and it reaches the point that the bean needs to be created.

The advice is to go with the default, which is an eager instantiation. This approach generally brings more benefits. Often, the need for using lazy instantiation is a sign something might be wrong with the app’s design.

## 5.2 - Using the prototype bean scope

In this section, we discuss the second bean scope Spring offers: **prototype**.

### How prototype beans work

Every time you request a reference to a prototype-scoped bean, Spring creates a new object instance. For prototype beans, Spring doesn’t create and manage an object instance directly. The framework manages the object’s type and creates a new instance every time someone requests a reference to the bean.

![](/images/prototypeScopeBeans.png)

As you can see, we need to use a new annotation named @Scope to change the bean’s scope. When you create the bean using the @Bean annotation approach, @Scope goes together with @Bean over the method that declares the bean. When declaring the bean with stereotype annotations, you use the @Scope annotation and the stereotype annotation over the class that declares the bean.

With prototype beans, we no longer have concurrency problems because each thread that requests the bean gets a different instance, so defining mutable prototype beans is not a problem.

#### DECLARING PROTOTYPE-SCOPED BEANS WITH @BEAN
