# Understanding SpringC Boot and Spring MVC

Spring makes web app development straightforward.

## What is Web App?

Any app you access through your web browser is a web app.

### A general overview of a web app

First, a web app is composed of two parts:
- *The client side* is what the user directly interacts with. A web browser represents the client side of a web app. The browser sends requests to a web server, receives responses from it, and provides a way for the user to interact with the app. We also refer to the client side of a web app as the *frontend*.
- *The server side* receives requests from the client and sends back data in response. The server side implements logic that processes and sometimes stores the client requested data before sending a response. We also refer to the server side of a web app as the *backend*.

![](/images/webAppComponents.png)

When discussing web apps, we usually refer to a client and a server, but it’s important to keep in mind that the backend serves multiple clients concurrently. Numerous people may use the same app at the same time on different platforms. Users can access the app through a browser on a computer, phone, tablet, and so on.

### Different fashions of implementing a web app with Spring

We classify the approaches of creating a web app as the following:

- **Apps where the backend provides the fully prepared view in response to a client’s request.** The browser directly interprets the data received from the backend and displays this information to the user in these apps.

  For these apps, almost everything happens on the backend side. The back-end gets requests representing user actions and executes some logic. In the end, the server responds with what the browser needs to display. The backend responds with the data in formats that the browser can interpret and display, such as HTML, CSS, images, and so on. It can also send scripts written in languages that the browser can understand and execute (such as JavaScript).

- **Apps using frontend-backend separation.** For these apps, the backend only serves raw data. The browser doesn’t display the data in the backend’s response directly. The browser runs a separate frontend app that gets the backend responses, processes the data, and instructs the browser what to display.

  Instead of telling the browser precisely what to display, the server now only sends raw data. The browser runs an independent frontend app it loads at an initial request from the server. This frontend app takes the server’s raw response, interprets it, and decides how the information is displayed.

### Using a servlet container in web app development

Thus far we’ve seen that a web app has a frontend and a backend. But we didn’t explicitly discuss implementing a web app with Spring. Of course, our purpose is to learn Spring and to implement apps with it, so we have to take a step forward and find out what we need to implement web apps with the framework.

One of the most important things to consider is the communication between the client and the server. A web browser uses a protocol named Hypertext Transfer Protocol (HTTP) to communicate with the server over the network. This protocol accurately describes how the client and the server exchange data over the network. But unless you are passionate about networking, you don’t need to understand how HTTP works in detail to write web apps. As a software developer, you’re expected to know that the web app components use this protocol to exchange data in a request-response fashion.

The client sends a request to the server, and the server responds. The client waits for the response after every request it sends.

You need something that can translate the HTTP request and response to a Java app. This something is a **servlet container** (sometimes referred to as a web server): a translator of the HTTP messages for your Java app. This way, your Java app doesn’t need to take care of implementing the communication layer. One of the most appreciated servlet container implementations is Tomcat.

![](/images/servletContainer.png)

But if this is everything a servlet container does, why name it “servlet” container? What is a servlet? A servlet is nothing more than a Java object that directly interacts with the servlet container. When the servlet container gets an HTTP request, it calls a servlet object’s method and provides the request as a parameter. The same method also gets a parameter representing the HTTP response used by the servlet to set the response sent back to the client that made the request.

![](/images/servletContainerRepresentation.png)

Some time ago, the servlet was the most critical component of a backend web app from the developer’s point of view. Suppose a developer had to implement a new page accessible at a specific path in the URL (e.g., /home/profile/edit, etc.) for a web app. The developer needed to create a new servlet instance, configure it in the servlet container, and assign it to a specific path. The servlet contained the logic associated with the user’s request and the ability to prepare a response, including info for the browser on how to display the response. For any path the web client could call, the developer needed to add the instance in the servlet container and configure it. Because such a component manages servlet instances you add into its context, we name it a servlet container. It basically has a context of servlet instances it controls, just as Spring does with its beans. For this reason, we call a component such as Tomcat a servlet container.

We don’t typically create servlet instances. We’ll use a servlet with the Spring apps we develop with Spring, but you won’t need to write this yourself, so you don’t have to focus on learning to implement servlets. But you do need to remember the servlet is the entry point to your app’s logic. It’s the component the servlet container (Tomcat, in our case) directly interacts with. It’s how the request data enters your app and how the response goes through Tomcat back to the client

![](/images/springAndServlet.png)

## The magic of Spring Boot

To create a Spring web app, we need to configure a servlet container, create a servlet instance, and then make sure we correctly configure this servlet instance such that Tomcat calls it for any client request. What a headache to write so many configurations! Fortunately, times changed.

Spring Boot is now one of the most appreciated projects in the Spring ecosystem. It helps you create Spring apps more efficiently and focus on the business code you write by eliminating a huge part of the code you used to write for configurations. Especially in a world of service-oriented architectures (SOA) and microservices, where you create apps more often, avoiding the pain of writing configurations is helpful.

- **Simplified project creation** You can use a project initialization service to get an empty but configured skeleton app.
- **Dependency starters** Spring Boot groups certain dependencies used for a specific purpose with dependency starters. You don’t need to figure out all the must-have dependencies you need to add to your project for one particular purpose nor which versions you should use for compatibility.
- **Autoconfiguration based on dependencies** Based on the dependencies you added to your project, Spring Boot defines some default configurations. Instead of writing all the configurations yourself, you only need to change the ones provided by Spring Boot that don’t match what you need. Changing the configs likely requires less code (if any).

### Using a project initialization service to create a Spring Boot project

Some IDEs integrate directly with a project initialization service, and some don’t. For example, in IntelliJ Ultimate or STS, you’ll find this feature available when creating a new project.

![](/images/projectInitializerIntellij.png)

If your IDE supports this feature, you’ll probably find it named Spring Initializr in your project creation menu. But if your IDE doesn’t support direct integration with a Spring Boot project initialization service, you can use this feature by accessing http://start.spring.io directly in your browser.

The following list summarizes the steps we’ll take to create the Spring Boot project using start.spring.io:

1. Access start.spring.io in a web browser.
2. Select the project properties (language, the version, the build tool, and so on).
3. Select the needed dependencies you want to add to your project.
4. Use the Generate button to download the archived project.
5. Unarchive the project and open it in your IDE.

![](/images/springInitializerWeb.png)
![](/images/springInitializerWebExample.png)

When you click the Generate button, the browser downloads a zip archive containing a Spring Boot project. Now we discuss the main things Spring Initializr configured into your Maven project:

1. The Spring app main class
2. The Spring Boot POM parent
3. The dependencies
4. The Spring Boot maven plugins
5. The properties file

#### THE APP’S MAIN CLASS CREATED BY START.SPRING.IO

You can observe that Spring Initializr added the Main class to your app and also some configurations in the pom.xml file. The Main class of a Spring Boot app is annotated with the @SpringBootApplication annotation, and it looks similar to the next code snippet:

```java
// This annotation defines the Main class of a Spring Boot app.
@SpringBootApplication
public class Main {
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}
```

Spring Initializr generated all this code.

#### THE SPRING BOOT MAVEN PARENT CONFIGURED BY START.SPRING.IO

Second, we look at the pom.xml file. If you open your project’s pom.xml file, you’ll find that the project initialization service also added some details here. One of the most important details you’ll find is the Spring Boot parent node, which looks similar to the next code snippet:

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>2.3.4.RELEASE</version>
  <relativePath/>
</parent>
```

One of the essential things this parent does is provide you with compatible versions for the dependencies you’ll add to your project. We let (and it’s recommended) Spring Boot choose the version of a dependency to make sure we don’t run into incompatibilities.

#### THE SPRING BOOT MAVEN PLUGIN CONFIGURED BY START.SPRING.IO

Next we look at the Spring Boot Maven plugin configured by start.spring.io when creating the project. You find this plugin also configured in the pom.xml file. The next code snippet shows the plugin declaration, which you usually find at the end of the pom.xml file inside the \<build> <plugins> … \</plugins></build> tags. This plugin is responsible for adding part of the default configurations you’ll observe in your project:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
  </plugins>
</build>
```

#### THE MAVEN DEPENDENCIES ADDED BY START.SPRING.IO WHEN CREATING THE PROJECT

Also in the pom.xml file, you find the dependency you added when creating the project in start.spring.io, Spring Web. You’ll find this dependency provided, as shown in the next code snippet. It is a dependency starter named spring-boot-starter-web.

For all the examples we have written, we also specified a version for each dependency. The reason you don’t specify one now is to let Spring Boot choose the right one for you. As we discussed earlier in this section, this is why we need the Spring Boot parent to the pom.xml file:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

#### THE APPLICATION PROPERTIES FILE

The last essential thing Spring Initializr added to your project is a file named “application.properties.” You find this file in the resources folder of your Maven project. Initially, this file is empty, and for this first example we’ll keep it this way. Later, we’ll discuss using this file to configure property values your app needs during its execution

### Using dependency starters to simplify the dependency management

Now that you learned how to use a Spring Boot project initialization service and have a better overview of the Spring Boot project you created, let’s focus on the second essential advantage Spring Boot offers: dependency starters. Dependency starters save you plenty of time, and they’re an invaluable feature Spring Boot offers.

A dependency starter is a group of dependencies you add to configure your app for a specific purpose. In your project’s pom.xml file, the starter looks like a normal dependency, as presented in the next code snippet. Observe the name of the dependency: A starter name usually starts with “spring-boot-starter-” followed by a relevant name that describes the capabilities it added to the app:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Say you want to add web capabilities to your app. In the past, to configure a Spring web app you had to add all the needed dependencies to your pom.xml file yourself and make sure their versions were compatible one with the other. Configuring all the dependencies you need is not an easy job. Taking care of the version compatibility is even more complicated.

With dependency starters, we don’t request dependencies directly. We request capabilities. You add a dependency starter for a particular capability you need, say web functionalities, a database, or security. Spring Boot makes sure to add the right dependencies to your app with the proper compatible version for your requested capability. **We can say that dependency starters are capability-oriented groups of compatible dependencies**.

![](/images/dependencyStarters.png)

Look at your pom.xml file. You only added the spring-boot-starter-web dependency, no Spring context, no AOP, no Tomcat! But, if you look in the “External Libraries” folder of your app, you’ll find JAR archives for all these. Spring Boot knew you would need them and downloaded them with specific versions it knows are compatible

### Using autoconfiguration by convention based on dependencies

Spring Boot also provides autoconfiguration for your application. We say that it applies the convention-over-configuration principle. Out of all the previous Spring Boot features discussed, the auto-configuration is probably the most appreciated and the most known.

You can start the app, and you’ll find your app boots a Tomcat instance by default accessible on port 8080.

Based on the dependencies you added, Spring Boot realizes what you expect from your app and provides you some default configurations. Spring Boot gives you the configurations, which are generally used for the capabilities you requested when adding the dependencies.

For example, Spring knows when you added the web dependency you need for a servlet container and configures you a Tomcat instance because, in most cases, developers use this implementation. For Spring Boot, Tomcat is the convention for a servlet container.

The convention represents the most-used way to configure the app for a specific purpose. Spring Boot configures the app by convention such that you now only need to change those places where your app needs a more particular configuration. With this approach, you’ll write less code for configuration (if any).

## Implementing a web app with Spring MVC

It’s true we already have a Spring Boot project with the default configurations, but this app only starts a Tomcat server. These configurations don’t make our app a web app yet! We still have to implement the pages that someone can access using a web browser.

To add a web page to your app, you follow two steps:

1. Write an HTML document with the content you want to be displayed by the browser.
2. Write a controller with an action for the web page created at point 1.

We first start adding a static web page with the content we want to display in the browser. This web page is just an HTML document, and for our example the page only displays a short text in a heading. You need to add the file in the “resources/static” folder of your Maven project. This folder is the default place where the Spring Boot app expects to find the pages to render.

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home Page</title>
</head>
<body>
    <h1>Welcome!</h1>
    <h2>This App is Served by Spring Boot!</h2>
</body>
</html>
```

The second step you take is writing a controller with a method that links the HTTP request to the page you want your app to provide in response. **The controller is a component of the web app that contains methods (often named actions) executed for a specific HTTP request.** In the end, the controller’s action returns a reference to the
web page the app returns in response.

We’ll just configure an action to return in response to the content of the index.html document we created and stored in the “resources/static” folder in the first step.

To mark a class as a controller, you only need to use the **@Controller** annotation, a stereotype annotation (like @Component and @Service). This means that Spring will also add a bean of this class to its context to manage it. Inside this class, you can define controller actions, which are methods associated with specific HTTP requests.

Say you want the browser to display this page’s content when the user accesses the /home path. To achieve this result, you annotate the action method with the **@RequestMapping** annotation specifying the path as a value of the annotation: @RequestMapping("/home"). The method needs to return, as a string, the name of the document you want the app to send as a response

```java
// We annotate the class with the @Controller stereotype annotation.
@Controller
public class MainController {

    // We use the @RequestMapping annotation to
    // associate the action with an HTTP request path.
    @RequestMapping("/home")
    public String home() {

        // We return the HTML document name that contains
        // the details we want the browser to display.
       return "index.html";
    }
}
```

When starting the app, you will see the log. It tells you Tomcat started and the port it uses in the app console. If you use the default, Tomcat uses port 8080.

Open a browser window on the same computer where you run the app and write the following address in the address bar: http://localhost:8080/home

![](/images/springBootTestingImplementation.png)

You will see the html served in the web browser.

Now that you’ve seen the app’s behavior, let’s discuss the mechanism behind it. Spring has a set of components that interact with each other to get the result you observed.

1. The client makes an HTTP request.
2. Tomcat gets the client’s HTTP request. Tomcat has to call a servlet component for the HTTP request. In the case of Spring MVC, Tomcat calls a servlet Spring Boot configured. We name this servlet **dispatcher servlet**.
3. The dispatcher servlet is the entry point of the Spring web app. Tomcat calls the dispatcher servlet for any HTTP request it gets. Its responsibility is to manage the request further inside the Spring app. It has to find what controller action to call for the request and what to send back in response to the client. This servlet is also referred to as a **“front controller.”**
4. The first thing the dispatcher servlet needs to do is find a controller action to call for the request. To find out which controller action to call, the dispatcher servlet delegates to a component named **handler mapping**. The handler mapping finds the controller action you associated with the request with the @RequestMapping annotation.
5. After finding out which controller action to call, the dispatcher servlet calls that specific controller action. If the handler mapping couldn’t find any action associated with the request, the app responds to the client with an HTTP “404 Not Found” status. The controller returns the page name it needs to render for the response to the dispatcher servlet. We refer to this HTML page also as “the view.”
6. At this moment, the dispatcher servlet needs to find the view with the name received from the controller to get its content and send it as response. The dispatcher servlet delegates the responsibility of getting the view content to a component named **“View Resolver.”**
7. The dispatcher servlet returns the rendered view in the HTTP response.

![](/images/springMVC-architecture.png)

Spring (with Spring Boot) considerably simplifies the development of a web app by arranging this setup. You only need to write controller actions and map them to requests using annotations. A large part of the logic is hidden in the framework, and this helps you write the apps faster and cleaner.
