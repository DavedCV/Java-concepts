# Using the Spring web scopes

In any Spring app, you can choose to declare a bean as one of the following:

- **Singleton:** The default bean scope in Spring, for which the framework uniquely identifies each instance with a name in the context
- **Prototype:** The bean scope in Spring, for which the framework only manages the type and creates a new instance of that class every time someone requests it (directly from the context or through wiring or auto-wiring).

In this chapter, you’ll learn that in web apps you can use other bean scopes that are relevant only to web applications. We call them web scopes:

- **Request scope:** Spring creates an instance of the bean class for every HTTP request. The instance exists only for that specific HTTP request.
- **Session scope:** Spring creates an instance and keeps the instance in the server’s memory for the full HTTP session. Spring links the instance in the context with the client’s session.
- **Application scope:** The instance is unique in the app’s context, and it’s available while the app is running.

## Using the request scope in a Spring web app

Web apps are focused on HTTP requests and responses. For this reason, and often in web apps, certain functionalities are easier to manage if Spring offers you a way to manage the bean life cycle in relationship with the HTTP request.

A request-scoped bean is an object managed by Spring, for which the framework creates a new instance for every HTTP request. The app can use the instance only for the request that created it. Any new HTTP request (from the same or other clients) creates and uses a different instance of the same class.

![](/images/requestScopedBean.png)

Let’s demonstrate the use of a request-scoped bean in an example. We’ll implement a web application’s login functionality, and we’ll use a request-scoped bean to manage the user’s credentials for the login logic.

![](/images/keyAspectsRequestScopedBeans.png)

We’ll create a page that contains a login form asking for a user’s name and password. The app compares the username and the password with a set of credentials it knows (in my case, user “natalie” with password “password”). If we provide correct credentials (they match with the credentials the app knows), then the page displays a message “You are now logged in” under the login form. If the credentials we provide are not correct, then the app displays a message: “Login failed.”

Template html:

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <form action="/" method="post">
        <label for="username">Username:</label>
        <input type="text" name="username" id="username" />
        <hr />
        <label for="password">Password:</label>
        <input type="password" id="password" name="password"/>
        <hr />
        <button type="submit">Log In!</button>

        <p th:text="${message}"></p>
    </form>
</body>
</html>
```

The Controller to get the login page:

```java
// We use the @Controller stereotype annotation
// to define the class as a Spring MVC controller.
@Controller
public class LoginController {

    // We map the controller’s action to the
    // root ("/ ") path of the application.
    @GetMapping("/")
    public String loginGet() {

        // We return the view name we
        // want to be rendered by the app.
        return "login.html";
    }
}

```

Now that we have a login page, we want to implement the login logic. When a user clicks on the Submit button, we want the page to display a proper message under the login form. If the user submitted the correct set of credentials, the message is “You are now logged in”; otherwise, the displayed message will be “Login failed”

To process the HTTP POST request that the HTML form creates when the user clicks on the Submit button, we need to add one more action to our LoginController. This action takes the client’s request parameters (the username and the password) and sends a message to the view according to the login result.

Controllers login action: 

```java
// We use the @Controller stereotype annotation
// to define the class as a Spring MVC controller.
@Controller
public class LoginController {

    // ...

    // We are mapping the controller’s action to
    // the HTTP POST request of the login page
    @PostMapping("/")
    public String loginPost(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    )
    {
        // false just for testing
        boolean loggedIn = false;

        if (loggedIn) model.addAttribute("message", "You are now logged in");
        else model.addAttribute("message", "Login Failed!");

        return "login.html";
    }
}
```

Now we have a controller and a view, but where is the **request scope** in all of this? The only class we wrote is the LoginController, and we left it a singleton, which is the default Spring scope. We don’t need to change the scope for LoginController as long as it doesn’t store any detail in its attributes. But remember, we need to implement the login logic. The login logic depends on the user’s credentials, and we have to take into consideration two things about these credentials:

1. The credentials are sensitive details, and you don’t want to store them in the app’s memory for longer than the login request.
2. More users with different credentials might attempt to log in simultaneously.

Considering these two points, we need to make sure that if we use a bean for implementing the login logic, each instance is unique for each HTTP request. We need to use a request-scoped bean.

![](/images/requestScopedBean-LoginProcessor.png)

We’ll extend the app as presented. We add a request-scoped bean LoginProcessor, which takes the credentials on the request and validates them.

```java
@Component
// We use the @RequestScope annotation to change the
// bean’s scope to request scope. This way, Spring creates
// a new instance of the class for every HTTP request.
@RequestScope
public class LoginProcessor {
    private String username;
    private String password;

    // The bean defines a method for
    // implementing the login logic.
    public boolean login() {
        return "natalie".equals(username) && "password".equals(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

```

To change the scope of the bean, we use the @RequestScoped annotation. Of course, we still need to make a bean of this class type in the Spring context by using the @Bean annotation in either a configuration class or a stereotype annotation. I chose to annotate the class with the @Component stereotype annotation.

Then we use LoginProcessor to process credentials:

```java
// We use the @Controller stereotype annotation
// to define the class as a Spring MVC controller.
@Controller
public class LoginController {

    private final LoginProcessor loginProcessor;

    @Autowired
    public LoginController(LoginProcessor loginProcessor) {
        this.loginProcessor = loginProcessor;
    }

    // We map the controller’s action to the
    // root ("/ ") path of the application.
    @GetMapping("/")
    public String loginGet() {

        // We return the view name we
        // want to be rendered by the app.
        return "login.html";
    }

    // We are mapping the controller’s action to
    // the HTTP POST request of the login page
    @PostMapping("/")
    public String loginPost(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    )
    {
        loginProcessor.setUsername(username);
        loginProcessor.setPassword(password);
        boolean loggedIn = loginProcessor.login();

        if (loggedIn) model.addAttribute("message", "You are now logged in");
        else model.addAttribute("message", "Login Failed!");

        return "login.html";
    }
}
```

## Using the session scope in a Spring web app
