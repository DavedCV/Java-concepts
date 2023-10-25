# The Spring Context: Defining Beans

Imagine the context as a place in the memory of your app in which we add all the object instances that we want the framework to manage. By default, Spring doesn’t know any of the objects you define in your application. To enable Spring to see your objects, you need to add them to the context.

Spring uses the instances in the context to connect your
app to various functionalities it provides.

The context is a complex mechanism that enables Spring to control instances you define. This way, it allows you to use the capabilities the framework offers.

**We’ll name these object instances “beans.”**

## 2.1 Creating a Maven Project

A build tool is software we use to build apps more easily. You configure a build tool to do the tasks that are part of building the app instead of manually doing them. Some examples of tasks that are often part of building the app are as follows:

- Downloading the dependencies needed by your app
- Running tests
- Validating that the syntax follows rules that you define
- Checking for security vulnerabilities
- Compiling the app
- Packaging the app in an executable archive

For a Maven project you specify the following:

- A group ID, which we use to group multiple related projects
- An artifact ID, which is the name of the current application
- A version, which is an identifier of the current implementation state

When you look first at your project, you observe two main things:

- The “src” folder (also known as the source folder), where you’ll put everything that belongs to the app.
- The pom.xml file, where you write the configurations of your Maven project, like adding new dependencies.

Maven organizes the “src” folder into the following folders:

- The “main” folder, where you store the application’s source code. This folder contains the Java code and the configurations separately into two different subfolders named “java” and “resources.”
- The “test” folder, where you store the unit tests’ source code. 

To add dependencies to your Maven projects, we need to change the content of the pom.xml file.

The default pom.xml file is the following:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
      http://maven.apache.org/xsd/maven-4.0.0.xsd">
  
  <modelVersion>4.0.0</modelVersion>
  
  <groupId>org.example</groupId>
  <artifactId>sq-ch2-ex1</artifactId>
  <version>1.0-SNAPSHOT</version>
</project>
```

With this pom.xml file, the project doesn’t use any external dependency.

You write all the dependencies between the **\<dependencies>\</dependencies>** tags. Each dependency is represented by a **\<dependency>\</dependency>** group of tags where you write the dependency’s attributes: the dependency’s group ID, artifact name, and version. Maven will search for the dependency by the values you provided for these three attributes and will download the dependencies from a repository.

Adding a dependency to the pom.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
      http://maven.apache.org/xsd/maven-4.0.0.xsd">
  
  <modelVersion>4.0.0</modelVersion>
  
  <groupId>org.example</groupId>
  <artifactId>sq-ch2-ex1</artifactId>
  <version>1.0-SNAPSHOT</version>

  <dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.2.6.RELEASE</version>
    </dependency>
  </dependencies>

</project>
```

## 2.2 Adding new beans to the Spring context

You’ll find out that you have multiple ways to add beans in the Spring context such that Spring can manage them and plug features it provides into your app.

Depending on the action, you’ll choose a specific way to add the bean; we’ll discuss when to select one or another. You can add beans in the context in the following ways:

- Using the @Bean annotation
- Using stereotype annotations
- Programmatically

---

To learn the concept we are going to follow progressive steps:

parrot class:

```java
public class Parrot {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

we are going to create an instance:

```java
public class Main {
    public static void main(String[] args) {
        Parrot p = new Parrot();
    }
}
```

Add the dependencies to the maven project:

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.2.6.RELEASE</version>
  </dependency>
</dependencies>
```

A critical thing to observe is that Spring is designed to be modular. By modular, I mean that you don’t need to add the whole Spring to your app when you use something out of the Spring ecosystem. You just need to add those parts that you use.

With the dependency added to our project, we can create an instance of the Spring context.

```java
public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context
        var context = new AnnotationConfigApplicationContext();
        Parrot p = new Parrot();
    }
}
```

By now you created an instance of Parrot, added the Spring context dependencies to your project, and created an instance of the Spring context. Your objective is to add the Parrot object to the context, which is the next step.

### Using the @Bean annotation to add beans into the Spring context

This makes it possible for you to add the instances of the classes
defined in your project (like Parrot in our case), as well as classes you didn’t create yourself but you use in your app.

Remember that the reason you learn to add beans to the Spring context is that Spring can manage only the objects that are part of it.

The steps you need to follow to add a bean to the Spring context using the @Bean annotation are as follows:

1. **Define a configuration class (annotated with @Configuration) for your project, which, as we’ll discuss later, we use to configure the context of Spring.**

The first step is to create a configuration class in the project. A Spring configuration class is characterized by the fact that it is annotated with the @Configuration annotation. We use the configuration classes to define various Spring-related configurations for the project.

2. **Add a method to the configuration class that returns the object instance you want to add to the context and annotate the method with the @Bean annotation.**

One of the things you can do with a configuration class is add beans to the Spring context. To do this, we need to define a method that returns the object instance we wish to add to the context and annotate that method with the @Bean annotation, which lets Spring know that it needs to call this method when it initializes its context and adds the returned value to the context.

3. **Make Spring use the configuration class defined in step 1. As you’ll learn later, we use configuration classes to write different configurations for the framework.**

We’ve implemented a configuration class in which we tell Spring the object instance that needs to become a bean. Now we need to make sure Spring uses this configuration class when initializing its context

![](/images/addingBeanToContext.png)

configuration class:
```java
@Configuration
public class ProjectConfig {

    @Bean
    Parrot parrot() {
        var p = new Parrot();
        p.setName("Koko");
        return p;
    }
}
```

main class:
```java
public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context and make Spring use the configuration class
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
    }
}
```

> A configuration class is a special class in Spring applications that we use to instruct Spring to do specific actions. For example, we can tell Spring to create beans or to enable certain functionalities

To verify the Parrot instance is indeed part of the context now, you can refer to the instance and print its name in the console:

```java
public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context and make Spring use the configuration class
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        Parrot p = context.getBean(Parrot.class);
        System.out.println(p.getName());

    }
}
```

You can add any kind of object to the Spring context:

```java
public class ProjectConfig {

    @Bean
    Parrot parrot() {
        var p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    String hello() {
        return "Hello";
    }

    @Bean
    Integer ten() {
        return 10;
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context and make Spring use the configuration class
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        Parrot p = context.getBean(Parrot.class);
        System.out.println(p.getName());

        String s = context.getBean(String.class);
        System.out.println(s);

        Integer n = context.getBean(Integer.class);
        System.out.println(n);

    }
}
```

Thus far we added one or more beans of different types to the Spring context. You can add more beans of the same type to the Spring context by using multiple methods annotated with @Bean. 

Of course, you can’t get the beans from the context anymore by only specifying the type. If you do, you’ll get an exception because Spring cannot guess which instance you’ve declared you refer to.

To solve this ambiguity problem, you need to refer precisely to one of the instances by using the bean’s name. By default, Spring uses the names of the methods annotated with @Bean as the beans’ names themselves.

If you’d like to give another name to the bean, you can use either one of the name or the value attributes of the @Bean annotation. Any of the following syntaxes will change the name of the bean in "miki":

- @Bean(name = "miki")
- @Bean(value = "miki")
- @Bean("miki")

When you have multiple beans of the same kind in the Spring context you can make one of them primary. You mark the bean you want to be primary using the @Primary annotation. A primary bean is the one Spring will choose if it has multiple options and you don’t specify a name

### Using stereotype annotations to add beans to the Spring context

Spring offers you more ways to add beans to its context. In different scenarios, you’ll find using one of these approaches is more comfortable than another. For example, with stereotype annotations, you’ll observe you write less code to instruct Spring to add a bean to its context.

With stereotype annotations, you add the annotation above the class for which you need to have an instance in the Spring context. When doing so, we say that you’ve marked the class as a component.

When the app creates the Spring context, Spring creates an instance of the class you marked as a component and adds that instance to its context.

We’ll still have a configuration class when we use this approach to tell
Spring where to look for the classes annotated with stereotype annotations. Moreover, you can use both the approaches (using @Bean ans Stereotype annotations together).

1. Using the @Component annotation, mark the classes for which you want Spring
to add an instance to its context (in our case Parrot).
2. Using @ComponentScan annotation over the configuration class, instruct Spring on where to find the classes you marked.

In the parrto class:

```java
@Component
public class Parrot {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

But wait! This code won’t work just yet. By default, Spring doesn’t search for classes annotated with stereotype annotations, so if we just leave the code as-is, Spring won’t add a bean of type Parrot in its context. To tell Spring it needs to search for classes annotated with stereotype annotations, we use the @ComponentScan annotation over the configuration class. Also, with the @ComponentScan annotation, we tell Spring where to look for these classes. We enumerate the packages where we defined the classes with stereotype annotations

```java
@Configuration
// Using the basePackages attribute of the annotation, we tell Spring where to look
//for classes annotated with stereotype annotations.
@ComponentScan(basePackages = "org.example.main")
public class ProjectConfig {
}
```

Now you told Spring the following:

1. Which classes to add an instance to its context (Parrot)
2. Where to find these classes (using @ComponentScan)


You can continue writing the main method as presented in the following listing to prove that Spring creates and adds the bean in its context.

```java
public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context and make Spring use the configuration class
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        Parrot p = context.getBean(Parrot.class);

        // Prints the default String representation of the instance taken from the Spring context
        System.out.println(p);

        // Prints null because we did
        //not assign any name to the
        //parrot instance added by
        //Spring in its context
        System.out.println(p.getName());
    }
}
```

Running this application, you’ll observe Spring added a Parrot instance to its context because the first value printed is the default String representation of this instance. However, the second value printed is null because we did not assign any name to this parrot. Spring just creates the instance of the class, but it’s still our duty if we want to change this instance in any way afterward (like assigning it a name).

#### Using @PostConstruct to manage the instance after its creation

As we’ve discussed in this section, using stereotype annotations you instruct Spring to create a bean and add it to its context. But, unlike using the @Bean annotation, you don’t have full control over the instance creation.

What if we want to execute some instructions right after Spring creates the bean? We can use the @PostConstruct annotation.

You just need to define a method in the component class and annotate that method with @PostConstruct, which instructs Spring to call that method after the constructor finishes its execution.

Let’s add to pom.xml the Maven dependency needed to use the @PostConstruct
annotation:

```xml
<dependency>
  <groupId>javax.annotation</groupId>
  <artifactId>javax.annotation-api</artifactId>
  <version>1.3.2</version>
</dependency>
```

Now you can define a method in the Parrot class:

```java
@Component
public class Parrot {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PostConstruct
    public void init() {
        this.name = "Kiki";
    }
}
```

Very similarly, but less encountered in real-world apps, you can use an annotation named @PreDestroy. With this annotation, you define a method that Spring calls immediately before closing and clearing the context.

### Comparison

In real-world scenarios you’ll use stereotype annotations as much as possible (because this approach implies writing less code), and you’ll only use the @Bean when you can’t add the bean otherwise (e.g., you create the bean for a class that is part of a library so you cannot modify that class to add the stereotype annotation).

![](/images/comparisonAddBeans.png)

### Programmatically adding beans to the Spring context

We’ve had the option of programmatically adding beans to the Spring context with Spring 5, which offers great flexibility because it enables you to add new instances in the context directly by calling a method of the context instance.

You’d use this approach when you want to implement a custom way of adding beans to the context and the @Bean or the stereotype annotations are not enough for your needs. Say you need to register specific beans in the Spring context depending on specific configurations of your application.

```java
if (condition) {
    // If the condition is true, add a
    // specific bean to the Spring context.
    registerBean(b1);
} else {
    // Otherwise, add another
    // bean to the Spring context.
    registerBean(b2);
}
```

To keep using our parrots example, the scenario is as follows: The app reads a collection of parrots. Some of them are green; others are orange. You want the app to add to the Spring context only the parrots that are green

**Using the registerBean() method you can write custom logic to add the desired instances to the Spring context.**

The **registerBean()** has four parameters:

1. **String beanName:** Use the first parameter beanName to define a name for the bean you add in the Spring context. If you don’t need to give a name to the bean you’re adding, you can use null as a value when you call the method.
2. **Class<T> beanClass:** The second parameter is the class that defines the bean you add to the context. Say you want to add an instance of the class Parrot; the value you give to this parameter is Parrot.class.
3. **Supplier<T> supplier:** The third parameter is an instance of Supplier. The implementation of this Supplier needs to return the value of the instance you add to the context. Remember, Supplier is a functional interface you find in the java.util.function package. The purpose of a supplier implementation is to return a value you define without taking parameters. 
4. **BeanDefinitionCustomizer... customizers:** The fourth and last parameter is a varargs of BeanDefinitionCustomizer. (If this doesn’t sound familiar, that’s okay; the BeanDefinitionCustomizer is just an interface you implement to configure different characteristics of the bean; e.g.,making it primary.) Being defined as a varargs type, you can omit this parameter entirely, or you can give it more values of type BeanDefinitionCustomizer.

Example:

```java
@Configuration
public class ProjectConfig {
}
```

```java
public class Parrot {
    private String name;
    // Omitted getters and setters
}
```

Using the registerBean() method:
```java
public class Main {
    public static void main(String[] args) {
        // creates an instance of the spring context and make Spring use the configuration class
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        // we create the instance we want to add programmatically
        Parrot x = new Parrot();
        x.setName("Kiki");

        // We define a Supplier to return this instance.
        Supplier<Parrot> parrotSupplier = () -> x;

        // We call the registerBean() method to add the instance to the Spring context.
        context.registerBean("parrot1", Parrot.class, parrotSupplier);
        
        // To verify the bean is now in the
        //context, we refer to the parrot bean
        //and print its name in the console.
        Parrot p = context.getBean(Parrot.class);
        System.out.println(p.getName());
    }
}
```

![](/images/registerBeanMethod.png)

Use one or more bean configurator instances as the last parameters to set different characteristics of the beans you add.
