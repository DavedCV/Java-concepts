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


