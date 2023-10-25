# The Spring context: Wiring beans

In this chapter, we discuss how to access the beans, which we’ve added to the Spring context.

In apps, we need to refer from one bean to another in a straightforward fashion—by telling Spring to provide a reference to an instance from its context where we need it. This way, we establish relationships among the beans (one bean will have a reference to another to delegate calls when it needs).

As you probably already know, often in any object-oriented programming language, an object needs to delegate specific responsibilities to others when implementing their behavior, so you need to know how to establish such relationships among objects when you use Spring as a framework as well.

## 3.1 - Implementing relationships among beans defined in the configuration file

In this section, you will learn to implement the relationship between two beans defined in the configuration class annotating methods with the @Bean annotation.
You’ll often encounter this approach for establishing the relationships among beans using the Spring configuration.

Say we have two instances in the Spring context: a parrot and a person. We’ll create and add these instances to the context. We want to make the person own the parrot. In other words, we need to link the two instances.

So, for each of the two approaches (wiring and auto-wiring), we have two steps:

1. Add the person and parrot beans to the Spring context.
2. Establish a relationship between the person and the parrot.

![](/beansRelationship.png)
![](/beansRelationship2.png)

```java
public class Parrot {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Parrot: " + name;
    }
}
```

```java
public class Person {
    private String name;
    private Parrot parrot;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parrot getParrot() {
        return parrot;
    }

    public void setParrot(Parrot parrot) {
        this.parrot = parrot;
    }
}
```

The following listing shows you how to define the two beans using the @Bean annotation in the configuration class.

```java
@Configuration
public class ProjectConfig {

    @Bean
    public Parrot parrot() {
        Parrot p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    public Person person() {
        Person p = new Person();
        p.setName("Ella");
        return p;
    }
}
```

You can now write a Main class, as presented in the following listing, and check that the two instances aren’t yet linked to one another.

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        // Gets a reference to the Person bean from the Spring context
        Person person = context.getBean(Person.class);

        // Gets a reference to the Parrot bean from the Spring context
        Parrot parrot = context.getBean(Parrot.class);

        System.out.println("Person's name: " + person.getName());
        System.out.println("Parrot's name: " + parrot.getName());

        // Prints the person’s parrot to prove that there’s not yet a relationship between the instances
        System.out.println("Person's parrot: " + person.getParrot());
    }
}
```

Both the person and the parrot instances are in the context, however. This output is null, which means there’s not yet a relationship between the instances.  

### Wiring the beans using a direct method call between the @Bean methods

In this section, we establish the relationship between the two instances of Person and Parrot. The first way (wiring) to achieve this is to call one method from another in the configuration class.

You’ll find this often used because it’s a straightforward
approach.

Making a link between the beans with a direct method call:

```java
@Configuration
public class ProjectConfig {
    @Bean
    public Parrot parrot() {
        Parrot p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    public Person person() {
        Person p = new Person();
        p.setName("Ella");

        // Setting the reference of the parrot
        // bean to the person’s parrot attribute
        p.setParrot(parrot());

        return p;
    }
}
```

![](/wiringBeanMethodCalls.png)

Doesn’t this mean that we create two instances of Parrot—one instance Spring creates and adds into its context and another one when the person() method makes the
direct call to the parrot() method? **No, we actually have only one parrot instance in this application overall.**

It might look strange at first, but Spring is smart enough to understand that by calling the parrot() method, you want to refer to the parrot bean in its context. When we use the @Bean annotation to define beans into the Spring context, Spring controls how the methods are called and can apply logic above the method call.

![](/wiringBeansSpringLogic.png)

It’s actually quite easy to test this behavior. Just add a no-args constructor to the Parrot class and print a message into the console from it. How many times will the message be printed in the console? If the behavior is correct, you’ll see the message only once.

### Wiring the beans using the @Bean annotated method’s parameters

Instead of directly calling the method that defines the bean we wish to refer to, we add a parameter to the method of the corresponding type of object, and we rely on Spring to provide us a value through that parameter.

This approach is a bit more flexible than the one we discussed. With this approach, it doesn’t matter if the bean we want to refer to is defined with a method annotated with @Bean or using a stereotype annotation like @Component

Take a look at the person() method. It now receives a parameter of type Parrot, and I set the reference of that parameter to the returned person’s attribute. When calling the method, Spring knows it has to find a parrot bean in its context and inject its value into the parameter of the person() method.

```java
@Configuration
public class ProjectConfig {

    @Bean
    public Parrot parrot() {
        Parrot p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    // We instruct Spring to provide a bean
    //from its context by defining a parameter
    //for the method.
    public Person person(Parrot parrot) {
        Person p = new Person();
        p.setName("Ella");
        
        //We set the value of the person’s
        //attribute with the reference
        //Spring provided.
        p.setParrot(parrot);

        return p;
    }
}
```

DI is a technique involving the framework setting a value into a specific field or parameter. In our case, Spring sets a particular value into the parameter of the person() method when calling it and resolves a dependency of this method. DI is an application of the IoC principle, and IoC implies that the framework controls the application at execution.

![](/DIfigure.png)

You’ll often use DI (and not only in Spring) because it’s a very comfortable way to manage object instances that are created and help us minimize the code we write when developing our apps.

## 3.2 - Using the @Autowired annotation to inject beans

You’ll often encounter this technique, which refers to an annotation named @Autowired, when you can change the class for which you define the bean (when this class is not part of a dependency).

Using the @Autowired annotation, we mark an object’s property where we want Spring to inject a value from the context, and we mark this intention directly in the class that defines the object that needs the dependency. This approach makes it easier to see the relationship between the two objects than the alternatives we discussed.

There are three ways we can use the @Autowired annotation:
- Injecting the value in the field of the class, which you usually find in examples and proofs of concept
- Injecting the value through the constructor parameters of the class approach that you’ll use most often in real-world scenarios
- Injecting the value through the setter, which you’ll rarely use in production-ready code

### Using @Autowired to inject the values through the class fields

Even if this approach is very straightforward, it has its sins, which is why we avoid using it when writing production code.

Let’s develop a project in which we annotate the parrot field of the Person class with the @Autowired annotation to tell Spring we want to inject a value there from its context.

```java
@Component
public class Parrot {
    private String name = "Koko";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Parrot: " + name;
    }
}
```

```java
@Component
public class Person {
    private String name;

    //  Annotating the field with @Autowired, we
    //  instruct Spring to inject an appropriate
    //  value from its context
    @Autowired
    private Parrot parrot = "Ella";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parrot getParrot() {
        return parrot;
    }

    public void setParrot(Parrot parrot) {
        this.parrot = parrot;
    }
}
```

![](/autowiredClassFields.png)

We use the stereotype annotation as an alternative to creating the bean using the configuration class. When annotating a class with @Component, Spring knows it has to create an instance of that class and add it to its context.

> In this case were used stereotype annotations to add the beans in the Spring context for this example. We could have defined the beans using @Bean, but most often, in real-world scenarios, you’ll encounter @Autowired used together with stereotype annotations.

```java
@Configuration
@ComponentScan (basePackages = "org.example.main")
public class ProjectConfig {
}
```

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        // Gets a reference to the Person bean from the Spring context
        Person person = context.getBean(Person.class);

        // Gets a reference to the Parrot bean from the Spring context
        Parrot parrot = context.getBean(Parrot.class);

        System.out.println("Person's name: " + person.getName());
        System.out.println("Parrot's name: " + parrot.getName());

        System.out.println("Person's parrot: " + person.getParrot());
    }
}
```

**Why is this approach not desired in production code?** It’s not totally wrong to use it, but you want to make sure you make your app maintainable and testable in produc- tion code. By injecting the value directly in the field:

- You don’t have the option to make the field final, and this way, make sure no one can change its value after initialization.
- It’s more difficult to manage the value yourself at initialization.

### Using @Autowired to inject the values through the constructor

The second option you have for injecting values into the object’s attributes when Spring creates a bean is using the class’s constructor defining the instance.

This approach is the one used most often in production code. The most recommended.

It enables you to define the fields as final, ensuring no one can change their value after Spring initializes them. The possibility to set the values when calling the constructor also helps you when writing specific unit tests where you don’t want to rely on Spring making the field injection for you.

You only need to change the Person class, as presented in the following listing. You need to define a constructor for the class and annotate it with @Autowired. Now we can also make the parrot field final.

```java
@Component
public class Person {
    private final Parrot parrot;

    // We can now make the field final to ensure its
    // value cannot be changed after initialization.
    private String name = "Ella";

    // We use the @Autowired annotation
    // over the constructor.
    @Autowired
    public Person(Parrot parrot) {
        this.parrot = parrot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parrot getParrot() {
        return parrot;
    }
}
```

> Starting with Spring version 4.3, when you only have one constructor in the class, you can omit writing the @Autowired annotation.

### Using dependency injection through the setter

You won’t often find developers applying the approach of using the setter for dependency injection. This approach has more disadvantages than advantages: it’s more challenging to read, it doesn’t allow you to make the field final, and it doesn’t help you in making the testing easier.

```java
@Component
public class Person {
  private String name = "Ella";
  private Parrot parrot;
  
  // Omitted getters and setters

  @Autowired
  public void setParrot(Parrot parrot) {
    this.parrot = parrot;
  }
}
```

## 3.3 - Dealing with circular dependencies

It’s comfortable to let Spring build and set the dependencies to the objects of your application. Letting Spring do this job for you saves you from writing a bunch of lines of code and makes the app easier to read and understand. But Spring can also get confused in some cases. A scenario often encountered in practice is generating a circular dependency by mistake.

A circular dependency is a situation in which, to create a bean (let’s name it Bean A), Spring needs to inject another bean that doesn’t exist yet (Bean B). But Bean B also requests a dependency to Bean A. So, to create Bean B, Spring needs first to have Bean A. Spring is now in a deadlock. It cannot create Bean A because it needs Bean B, and it cannot create Bean B because it needs Bean A.

![](/circularDependencies.png)

A circular dependency is easy to avoid. You just need to make sure you don’t define objects whose creation depends on the other. Having dependencies from one object to another like this is a bad design of classes. In such a case, you need to rewrite your code.

## 3.4 - Choosing from multiple beans in the Spring context

In this section, we discuss the scenario in which Spring needs to inject a value into a parameter or class field but has multiple beans of the same type to choose from. Say you have three Parrot beans in the Spring context. You configure Spring to inject a value of type Parrot into a parameter. How will Spring behave? Which of the beans of the same type would the framework choose to inject in such} a scenario?

Depending on your implementation, you have the following cases:

1. The identifier of the parameter matches the name of one of the beans from the context (which, remember, is the same as the name of the method annotated with @Bean that returns its value). In this case, Spring will choose the bean for which the name is the same as the parameter
2. The identifier of the parameter doesn’t match any of the bean names from the context. Then you have the following options:
  - You marked one of the beans as primary. In this case, Spring will select the primary bean for injection.
  - You can explicitly select a specific bean using the @Qualifier annotation.
  - If none of the beans is primary and you don’t use @Qualifier, the app will fail with an exception, complaining that the context contains more beans of the same type and Spring doesn’t know which one to choose.

The next listing shows you a configuration class that defines two Parrot instances and uses injection through the method parameters.

### Using parameter injection for more than one bean

```java
@Configuration
public class ProjectConfig {

    @Bean
    public Parrot parrot1() {
        Parrot p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    @Primary
    public Parrot parrot2() {
        Parrot p = new Parrot();
        p.setName("Miki");
        return p;
    }

    @Bean
    // The name of the parameter
    //matches the name of the bean
    //representing parrot Miki.
    public Person person(Parrot parrot2) {
        Person p = new Person();
        p.setName("Ella");
        p.setParrot(parrot2);
        return p;
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        // Gets a reference to the Person bean from the Spring context
        Person person = context.getBean(Person.class);

        // Gets a reference to the Parrot bean from the Spring context
        Parrot parrot = context.getBean(Parrot.class);

        System.out.println("Person's name: " + person.getName());
        System.out.println("Parrot's name: " + parrot.getName());

        System.out.println("Person's parrot: " + person.getParrot());
    }
}
```

Observe that Spring linked the person bean to the parrot named Miki because the bean representing this parrot has the name parrot2.

### Using the @Qualifier annotation

In a real-world scenario, is best to avoid relying on the name of the parameter, which could be easily refactored and changed by mistake by another developer. To feel more comfortable, is better to choose a more visible approach to express the intention to inject a specific bean: using the @Qualifier annotation.

The following listing provides an example using the @Qualifier annotation. Observe that instead of having a specific identifier of the parameter, now we specify the bean we want to inject using the value attribute of the @Qualifier annotation.

```java
@Configuration
public class ProjectConfig {

    @Bean
    public Parrot parrot1() {
        Parrot p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    @Primary
    public Parrot parrot2() {
        Parrot p = new Parrot();
        p.setName("Miki");
        return p;
    }

    @Bean
    //  Using the @Qualifier annotation,
    //  you clearly mark your intention
    //  to inject a specific bean from the
    //  context.
    public Person person(@Qualifier("parrot2") Parrot parrot) {
        Person p = new Person();
        p.setName("Ella");
        p.setParrot(parrot);
        return p;
    }
}
```

### Using parameter injection for more than one bean with @Autowire

A similar situation can also happen when using the @Autowired annotation.

In this project, we define two beans of type Parrot (using the @Bean annotation) and an instance of Person (using stereotype annotations). We’ll configure Spring to inject one of the two parrot beans in bean of type Person.

```java
public class Parrot {
  private String name;
  // Omitted getters, setters, and toString()
}
```

We define a bean of type Person using the @Component stereotype annotation. Observe the identifier we gave to the parameter of the constructor in the next code snippet. The reason we gave the identifier “parrot2” is this is the name we’ll also configure for the bean in the context we want Spring to inject into that parameter:

```java
@Component
public class Person {
    private final Parrot parrot;

    private String name = "Ella";

    @Autowired
    public Person(Parrot parrot2) {
        this.parrot = parrot2;
    }

    // getters and setters
}
```

I define two beans of type Parrot using the @Bean annotation in the configuration class. Don’t forget we still have to add @ComponentScan to tell Spring where to find the classes annotated with stereotype annotations.

```java
@Configuration
@ComponentScan(basePackages = "org.example.main")
public class ProjectConfig {

    @Bean
    public Parrot parrot1() {
        Parrot p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    @Primary
    //  With the current setup, the bean named
    //  parrot2 is the one that Spring injects into
    //  the Person bean.
    public Parrot parrot2() {
        Parrot p = new Parrot();
        p.setName("Miki");
        return p;
    }
}
```

What happens if you run a main method as the one presented in the next code snippet? Our person owns which parrot? Because the name of the constructor’s parameter matches one of the bean’s names in the Spring context (parrot2), Spring injects that bean.

### Using the @Qualifier annotation with @Autowire

As we discussed for the @Bean annotated method parameter, is better to not rely on the name of the variable. Instead use the @Qualifier annotation to express the intention clearly: inject a specific bean from the context.

This way, we minimize the chance that someone would refactor the name of the variable and thus affect how the app works.

Using the @Qualifier annotation, we specify the name of the bean we want Spring to inject from the context, and we don’t rely on the identifier of the constructor’s parameter

```java
@Component
public class Person {
    private final Parrot parrot;

    private String name = "Ella";

    @Autowired
    public Person(@Qualifier("parrot2") Parrot parrot) {
        this.parrot = parrot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parrot getParrot() {
        return parrot;
    }
}
```

The behavior of the app doesn’t change, and the output remains the same. This approach makes your code less subject to mistakes.
