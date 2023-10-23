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

## Adding new beans to the Spring context

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

1. Define a configuration class (annotated with @Configuration) for your project, which, as we’ll discuss later, we use to configure the context of Spring.
2. Add a method to the configuration class that returns the object instance you want to add to the context and annotate the method with the @Bean annotation.
3. Make Spring use the configuration class defined in step 1. As you’ll learn later, we use configuration classes to write different configurations for the framework.
