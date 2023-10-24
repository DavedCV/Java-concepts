# The Spring context: Wiring beans

In this chapter, we discuss how to access the beans, which we’ve added to the Spring context.

In apps, we need to refer from one bean to another in a straightforward fashion—by telling Spring to provide a reference to an instance from its context where we need it. This way, we establish relationships among the beans (one bean will have a reference to another to delegate calls when it needs).

As you probably already know, often in any object-oriented programming language, an object needs to delegate specific responsibilities to others when implementing their behavior, so you need to know how to establish such relationships among objects when you use Spring as a framework as well.

## 3.1 Implementing relationships among beans defined in the configuration file

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
