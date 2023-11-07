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

When you enter a web app and log in, you expect to then surf through that app’s pages, and the app still remembers you’ve logged in.

A **session-scoped** bean is an object managed by Spring, for which Spring creates an instance and links it to the HTTP session. Once a client sends a request to the server, the server reserves a place in the memory for this request, for the whole duration of their session.

Spring creates an instance of a session-scoped bean when the HTTP session is created for a specific client. That instance can be reused for the same client while it still has the HTTP session active. The data you store in the session-scoped bean attribute is available for all the client’s requests throughout an HTTP session. This approach of storing the data allows you to store information about what users do while they’re surfing through the pages of your app.

![](/images/sessionScopedBeans.png)

A session-scoped bean allows us to store data shared by multiple requests of the same client.

![](/images/keyAspectsSessionScopedBans.png)

We continue to use a session-scoped bean to make our app aware that a user logged in and recognize them as a logged-in user while they access different pages of the app.

Let’s change the application we implemented to display a page that only logged-in users can access. Once a user logs in, the app redirects them to this page, which displays a welcome message containing the logged-in username and offers the user the option to log out by clicking a link.

These are the steps we need to take to implement this change:
1. Create a session-scoped bean to keep the logged-in user’s details.
2. Create the page a user can only access after login.
3. Make sure a user cannot access the page created at point 1 without logging in first.
4. Redirect the user from login to the main page after successful authentication.

Fortunately, creating a session-scoped bean in Spring is as simple as using the @SessionScope annotation with the bean class. Let’s create a new class, LoggedUserManagementService, and make it session-scoped, as presented in the following listing.

```java
@Service
// We use the @SessionScope
// annotation to change the
// scope of the bean to session.
@SessionScope
public class LoggedUserManagementService {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
```

Every time a user successfully logs in, we store its name in this bean’s username attribute. We auto-wire the LoggedUserManagementService bean in the LoginProcessor class, which we implemented to take care of the authentication logic, as shown in the following listing.

```java
@Component
// We use the @RequestScope annotation to change the
// bean’s scope to request scope. This way, Spring creates
// a new instance of the class for every HTTP request.
@RequestScope
public class LoginProcessor {

    private final LoggedUserManagementService loggedUserManagementService;
    private String username;
    private String password;

    @Autowired
    public LoginProcessor(LoggedUserManagementService loggedUserManagementService) {
        this.loggedUserManagementService = loggedUserManagementService;
    }

    // The bean defines a method for
    // implementing the login logic.
    public boolean login() {

        boolean loginResult = false;

        if ("natalie".equals(username) && "password".equals(password)) {
            loginResult = true;

            // We store the username on the LoggedUserManagementService bean.
            loggedUserManagementService.setUsername(username);
        }

        return loginResult;
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

Observe that the LoginProcessor bean stays request-scoped. We still use Spring to create this instance for each login request. We only need the username and password attributes’ values during the request to execute the authentication logic.

Because the LoggedUserManagementService bean is session-scoped, the username value will now be accessible throughout the entire HTTP session. You can use this value to know if someone is logged in, and who. You don’t have to worry about the case where multiple users are logged in; the application framework makes sure to link each HTTP request to the correct session.

Now we create a new page and make sure a user can access it only if they have already logged in. We define a new controller (that we’ll call MainController) for the new page. We’ll define an action and map it to the /main path. To make sure a user can access this path only if they logged in, we check if the LoggedUserManagementService bean stores any username. If it doesn’t, we redirect the user to the login page. To redirect the user to another page, the controller action needs to return the string “redirect:” followed by the path to which the action wants to redirect the user.

```java
@Controller
public class MainController {
    private final LoggedUserManagementService loggedUserManagementService;

    @Autowired
    public MainController(LoggedUserManagementService loggedUserManagementService) {
        this.loggedUserManagementService = loggedUserManagementService;
    }

    @GetMapping("/main")
    public String home() {
        // We take the username value, which should be different than null if someone logged in.
        String username = loggedUserManagementService.getUsername();

        // If the user is not logged in, we redirect the user to the login page.
        if (username == null) return "redirect:/";

        // If the user is logged in, we return the view for the main page.
        return "main.html";
    }
}
```

You need to add the main.html that defines the view in the “resources/templates” folder of your Spring Boot project.

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
</head>
<body>
    <h1>Welcome!</h1>
</body>
</html>
```


To allow the user to log out is also easy. You just need to set the username in the LoggedUserManagementService session bean as null. Let’s create a logout link on the page and also add the logged-in username in the welcome message. The following list- ing shows the changes to the main.html page that defines our view.

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
</head>
<body>
    <h1>Welcome, <span th:text="${username}"></span></h1>
    <a href="/main?logout">Log Out!</a>
</body>
</html>
```

```java
public class MainController {
    private final LoggedUserManagementService loggedUserManagementService;

    @Autowired
    public MainController(LoggedUserManagementService loggedUserManagementService) {
        this.loggedUserManagementService = loggedUserManagementService;
    }

    @GetMapping("/main")
    public String home(
            @RequestParam(required = false) String logout,
            Model model
    )
    {
        // If the logout parameter is present, we erase the
        // username from the LoggedUserManagementService bean.
        if (logout != null) {
            loggedUserManagementService.setUsername(null);
        }

        // We take the username value, which should be different than null if someone logged in.
        String username = loggedUserManagementService.getUsername();

        // If the user is not logged in, we redirect the user to the login page.
        if (username == null) return "redirect:/";

        // Add variable to the template
        model.addAttribute("username", username);

        // If the user is logged in, we return the view for the main page.
        return "main.html";
    }
}
```

To complete the app, we’d like to change the LoginController to redirect users to the main page once they authenticate. To achieve this result, we need to change the LoginController’s action, as presented in the following listing.

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

        // When the user successfully authenticates,
        // the app redirects them to the main page.
        if (loggedIn) return "redirect:/main";

        model.addAttribute("message", "Login Failed!");
        return "login.html";
    }
}

```

## Using the application scope in a Spring web app

In this section, we discuss the application scope. I want to mention its existence, make you aware of how it works, and emphasize that it’s better not to use it in a production app. All client requests share an application-scoped bean.

The application scope is close to how a singleton works. The difference is that you can’t have more instances of the same type in the context and that we always use the HTTP requests as a reference point when discussing the life cycle of web scopes (including the application scope).

We face the same concurrency problems we discussed in chapter 5 for the singleton beans for application-scoped beans: it’s better to have immutable attributes for the singleton beans. The same advice is applicable to an application-scoped bean. But if you make the attributes immutable, then you can directly use a singleton bean instead.

Generally, I recommend developers avoid using application-scoped beans. It’s better to directly use a persistence layer, such as a database.

Let’s change the application we worked on in this chapter and add a feature that counts the login attempts.

Because we have to count the login attempts from all users, we’ll store the count in an application-scoped bean. Let’s create a LoginCountService application-scoped bean that stores the count in an attribute. The following listing shows the definition of this class.

```java
@Service
// The @ApplicationScope
// annotation changes the scope of
// this bean to the application scope.
@ApplicationScope
public class LoginCountService {
    private int count;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
```

The LoginProcessor can then auto-wire this bean and call the increment() method for any new login attempt, as presented in the following listing.

```java
@Component
// We use the @RequestScope annotation to change the
// bean’s scope to request scope. This way, Spring creates
// a new instance of the class for every HTTP request.
@RequestScope
public class LoginProcessor {

    private final LoggedUserManagementService loggedUserManagementService;
    private final LoginCountService loginCountService;
    private String username;
    private String password;

    // We auto-wire the LoggedUserManagementService bean
    @Autowired
    public LoginProcessor(LoggedUserManagementService loggedUserManagementService, LoginCountService loginCountService) {
        this.loggedUserManagementService = loggedUserManagementService;
        this.loginCountService = loginCountService;
    }

    // The bean defines a method for
    // implementing the login logic.
    public boolean login() {
        loginCountService.increment();

        boolean loginResult = false;

        if ("natalie".equals(username) && "password".equals(password)) {
            loginResult = true;

            // We store the username on the LoggedUserManagementService bean.
            loggedUserManagementService.setUsername(username);
        }

        return loginResult;
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

The last thing you need to do is to display this value.

```java
@Controller
public class MainController {
    private final LoggedUserManagementService loggedUserManagementService;
    private final LoginCountService loginCountService;

    @Autowired
    public MainController(LoggedUserManagementService loggedUserManagementService,
                          LoginCountService loginCountService) {
        this.loggedUserManagementService = loggedUserManagementService;
        this.loginCountService = loginCountService;
    }

    @GetMapping("/main")
    public String home(
            @RequestParam(required = false) String logout,
            Model model
    )
    {
        // If the logout parameter is present, we erase the
        // username from the LoggedUserManagementService bean.
        if (logout != null) {
            loggedUserManagementService.setUsername(null);
        }

        // We take the username value, which should be different than null if someone logged in.
        String username = loggedUserManagementService.getUsername();
        // Gets the count from the application-scoped bean
        int count = loginCountService.getCount();

        // If the user is not logged in, we redirect the user to the login page.
        if (username == null) return "redirect:/";

        // Add variable to the template
        model.addAttribute("username", username);
        // Sends the count value to the view
        model.addAttribute("loginCount", count);

        // If the user is logged in, we return the view for the main page.
        return "main.html";
    }
}
```

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
</head>
<body>
    <h1>Welcome, <span th:text="${username}"></span></h1>
    <h2>Your login number is <span th:text="${loginCount}"></span></h2>
    <a href="/main?logout">Log Out!</a>
</body>
</html>
```
