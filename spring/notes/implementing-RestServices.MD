# Implementing Rest Services

REST services are one of the most often encountered ways to implement communication between two apps. REST offers access to functionality the server exposes through endpoints a client can call.

You use REST services to establish the communication between a client and a server in a web app. But you can also use REST services to develop the communication between a mobile app and a backend or even two backend services.

![](/images/REST-Services.png)

## Using REST services to exchange data between apps

REST endpoints are simply a way for implementing com- munication between two apps. REST endpoints are as simple as implementing a controller action mapped to an HTTP method and a path. An app calls this controller action through HTTP. Because it’s how an app exposes a service through a web protocol, we call this endpoint a **web service**.

In the end, in Spring a REST endpoint is still a controller action mapped to an HTTP method and path. Spring uses the same mechanism you learned for web apps for exposing REST endpoints. The only difference is that for REST services we’ll tell the Spring MVC dispatcher servlet not to look for a view.

The server sends back, in the HTTP response to the client, directly what the controller’s action returns.

![](/images/springRestDiagram.png)

You’ll find REST services are comfortable to use. Their simplicity is one reason they’re so often used today, and Spring makes their implementation straightforward. But before starting with our first example, I’d like to make you aware of some communication issues the REST endpoint might bring:

- If the controller’s action takes a long time to complete, the HTTP call to the endpoint might time out and break the communication.
- Sending a large quantity of data in one call (through the HTTP request) might cause the call to time out and break the communication. Sending more than a few megabytes through a REST call usually isn’t the right choice.
- Too many concurrent calls on an endpoint exposed by a backend component might put too much pressure on the app and cause it to fail.
- The network supports the HTTP calls, and the network is never 100% reliable. There’s always a chance a REST endpoint call might fail because of the network.

When you implement the communication between two apps using REST, you always need to consider what should happen if a call fails and how it might affect the app. Ask yourself if the data could be affected in any way. Could the way you designed your app lead to data inconsistencies if an endpoint call fails? In case the app needs to display an error to the user, how would you do that? These are complex issues

## Implementing a REST endpoint

The good news is that Spring uses the same Spring MVC mechanism behind REST endpoints.

The following fragment shows you a controller class that implements a simple action. We annotate the controller class with the @Controller stereotype annotation. This way, an instance of the class becomes a bean in the Spring context, and Spring MVC knows this is a controller that maps its methods to specific HTTP paths. Also, we used the @GetMapping annotation to specify the action path and HTTP method. The only new thing you find in this listing is the use of the @ResponseBody annotation. The @ResponseBody annotation tells the dispatcher servlet that the controller’s action doesn’t return a view name but the data sent directly in the HTTP response.

```java
@Controller
public class HelloController {

    // We use the @GetMapping annotation to associate the GET
    // HTTP method and a path with the controller’s action.
    @GetMapping("/hello")
    // We use the @ResponseBody annotation to inform
    // the dispatcher servlet that this method doesn’t
    // return a view name but the HTTP response directly.
    @ResponseBody
    public String hello() {
        return "Hello!";
    }
}
```

We want to somehow prevent repeating the @ResponseBody annotation for each method. To help us with this aspect, Spring offers the @RestController annotation, a combination of @Controller and @ResponseBody. You use @RestController to instruct Spring that all the controller’s actions are REST endpoints. This way, you avoid repeating the @ResponseBody annotation.

```java
// Instead of repeating the @ResponseBody
// annotation for each method, we replace
// @Controller with @RestController.
@RestController
public class HelloController {

    // We use the @GetMapping annotation to associate the GET
    // HTTP method and a path with the controller’s action.
    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/ciao")
    public String ciao() {
        return "Ciao!";
    }
}
```

It was indeed easy to implement a couple of endpoints. But how do we validate they work correctly?

- **Postman:** Offers a nice GUI and is comfortable to use

  ![](/images/postmanInterface.png)
  ![](/images/postmanResponseInterface.png)

- **cURL:** A command-line tool useful in cases where you don’t have a GUI (when you connect to a virtual machine via SSH or when you write a batch script)

  If the HTTP method is HTTP GET, you don’t need to specify it explicitly. When the method is not HTTP GET, or if you want to specify it explicitly, you can use the -X flag, as presented in the next snippet:

  ```bash
  curl -X GET http://localhost:8080/hello
  ```

  If you want to get more details of the HTTP request, you can add the -v option to the command, as presented in the next snippet:

  ```bash
  curl -v http://localhost:8080/hello
  ```

## Managing the HTTP response

The HTTP response is how the backend app sends data back to the client due to a client’s request. The HTTP response holds data as the following:

- **Response headers:** Short pieces of data in the response (usually not more than a few words long)
- **The response body:** A larger amount of data the backend needs to send in the response
- **The response status:** A short representation of the request’s result
