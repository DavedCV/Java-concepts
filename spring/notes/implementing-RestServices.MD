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

### Sending objects as a response body

The only thing you need to do to send an object to the client in a response is make the controller’s action return that object.

we define a model object named Country with the attributes name (representing the country name) and population (representing the number of millions of people located in that country). We implement a controller action to return an instance of type Country.

When we use an object (such as Country) to model the data transferred between two apps, we name this object a data transfer object (DTO). We can say that Country is our DTO, whose instances are returned by the REST endpoint we implement in the HTTP response body.

```java
public class Country {

    private String name;
    private int population;

    // To make a Country instance simpler, we define a
    // static factory method that receives the name and
    // the population. This method returns a Country
    // instance with the provided values set.
    public static Country of(
            String name,
            int population
    )
    {
        Country country = new Country();
        country.setName(name);
        country.setPopulation(population);
        return country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
```

The following listing shows the implementation of a controller’s action that returns an instance of type Country.

```java
// Marking the class as a REST controller to add a bean
// in the Spring context and also inform the dispatcher
// servlet not to look for a view when this method returns
@RestController
public class CountryController {

    @GetMapping("/mapping")
    public Country france(){
        Country c = Country.of("france", 67);

        // Returning an instance of type Country
        return c;
    }
}
```

What happens when you call this endpoint? How would the object look in the HTTP response body? By default, Spring creates a string representation of the object and formats it as JSON. JavaScript Object Notation (JSON) is a simple way to format strings as attribute-value pairs.

When calling the /france endpoint, the response body looks as presented in the next snippet:

```json
{
    "name": "france",
    "population": 67
}
```

You could also send object collection instances in the response body. The next listing shows that we added a method that returns a List of Country objects.

```java
// Marking the class as a REST controller to add a bean
// in the Spring context and also inform the dispatcher
// servlet not to look for a view when this method returns
@RestController
public class CountryController {

    @GetMapping("/france")
    public Country france() {
        Country c = Country.of("france", 67);

        // Returning an instance of type Country
        return c;
    }

    @GetMapping("/all")
    public List<Country> all() {
        Country c1 = Country.of("France", 67);
        Country c2 = Country.of("Spain", 47);

        // Returns a collection in the HTTP response body
        return List.of(c1, c2);
    }
}
```

When you call this endpoint, the response body looks as presented in the next snippet:

```json
[
    {
        "name": "France",
        "population": 67
    },
    {
        "name": "Spain",
        "population": 47
    }
]
```

**Using JSON is the most common way to represent objects when working with REST endpoints.**

### Setting the response status and headers

In this section, we discuss setting the response status and response headers. Sometimes it’s more comfortable to send part of the data in the response headers. The response status is also an essential flag in the HTTP response you use to signal the request’s result. By default, Spring sets some common HTTP statuses:

- 200 OK if no exception was thrown on the server side while processing the request.
- 404 Not Found if the requested resource doesn’t exist.
- 400 Bad Request if a part of the request could not be matched with the way the server expected the data.
- 500 Error on server if an exception was thrown on the server side for any reason while processing the request. Usually, for this kind of exception, the client can’t do anything, and it’s expected someone should solve the problem on the backend.

However, in some cases, the requirements ask you to configure a custom status. How could you do that? The easiest and most common way to customize the HTTP response is using the **ResponseEntity** class. This class provided by Spring allows you to specify the response body, status, and headers on the HTTP response.

To show an example: 

```java
// Marking the class as a REST controller to add a bean
// in the Spring context and also inform the dispatcher
// servlet not to look for a view when this method returns
@RestController
public class CountryController {

    @GetMapping("/france")
    public Country france() {
        Country c = Country.of("france", 67);

        // Returning an instance of type Country
        return c;
    }

    @GetMapping("/all")
    public List<Country> all() {
        Country c1 = Country.of("France", 67);
        Country c2 = Country.of("Spain", 47);

        // Returns a collection in the HTTP response body
        return List.of(c1, c2);
    }

    @GetMapping("/colombia")
    public ResponseEntity<Country> colombia() {
        Country c = Country.of("Colombia",  51);

        /*
        * - Changes the HTTP response status 202 Accepted
        * - Adds three custom headers to the response
        * - Sets the response body
        * */
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header("continent", "Latin America")
                .header("capital", "Bogota")
                .header("favorite_food", "bandeja paisa")
                .body(c);
    }
}
```

A controller action returns a ResponseEntity instance instead of the object you want to set on the response body directly. The ResponseEntity class allows you to set the response body’s value and the response status and headers. We set three headers and change the response status to “202 Accepted.”

### Managing exceptions at the endpoint level

It’s essential to consider what happens if the controller’s action throws an exception. In many cases, we use exceptions to signal specific situations, some of these related to the business logic.

Suppose you create an endpoint the client calls to make a payment. If the user doesn’t have enough money in their account, the app might represent this situation by throwing an exception. In this case, you’ll probably want to set some details on the HTTP response to inform the client of the specific situation that occurred.

- One of the ways you can manage exceptions is catching them in the controller’s action and using the ResponseEntity class, to send a different configuration of the response when the exception occurs.
- An alternative approach is by using a REST controller advice class: an aspect that intercepts an endpoint call when it throws an exception, and you can specify a custom logic to be executed for that specific exception.

---

For our scenario, we define an exception named NotEnoughMoneyException, and the app will throw this exception when it cannot fulfill the payment because the client doesn’t have enough money in their account. The next code snippet shows the class defining the exception:

```java
public class NotEnoughMoneyException extends RuntimeException{
}
```

We also implement a service class that defines the use case. For our test, we directly throw this exception. In a real-world scenario, the service would implement the complex logic for making the payment. The next code snippet shows the service class we use for our test:

```java
@Service
public class PaymentService {
public PaymentDetails processPayment() {
throw new NotEnoughMoneyException();
}
}
```

```java
public class PaymentDetails {
    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
```

When the app encounters an exception, it uses another model class named ErrorDetails to inform the client of the situation.

```java
public class ErrorDetails {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```

How could the controller decide what object to send back depending on how the flow executed? When there’s no exception (the app successfully completes the payment), we want to return an HTTP response with the status “Accepted” of type PaymentDetails. Suppose the app encountered an exception during the execution flow. In that case, the controller’s action returns an HTTP response with the status “400 Bad Request” and an ErrorDetails instance containing a message that describes the issue.

The next listing shows this logic implemented by the controller’s method:

```java
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public ResponseEntity<?> makePayment() {
        try {
            PaymentDetails paymentDetails = paymentService.processPayment();

            // If calling the service method succeeds,
            // we return an HTTP response with status
            // Accepted and the PaymentDetails
            // instance as a response body.
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(paymentDetails);
        } catch (NotEnoughMoneyException e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setMessage(e.getMessage());

            // If an exception of type NotEnoughMoneyException is
            // thrown, we return an HTTP response with status Bad
            // Request and an ErrorDetails instance as a body.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorDetails);
        }
    }
}
```

This approach is good, and you’ll often find developers using it to manage the excep- tion cases. However, in a more complex application, you would find it more comfortable to separate the responsibility of exception management. First, sometimes the same exception has to be managed for multiple endpoints, and, as you guessed, we don’t want to introduce duplicated code. Second, it’s more comfortable to know you find the exception logic all in one place when you need to understand how a specific case works. For these reasons, I prefer using a REST controller advice, an aspect that intercepts exceptions thrown by controllers’ actions and applies custom logic you define according to the intercepted exception.

The controller action is much simplified because it no longer treats the exception case, as presented in the following listing.

```java
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public ResponseEntity<?> makePayment() {
        PaymentDetails paymentDetails = paymentService.processPayment();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(paymentDetails);
    }
}
```

Instead, we created a separate class named ExceptionControllerAdvice that implements what happens if the controller’s action throws a NotEnoughMoneyException. The ExceptionControllerAdvice class is a REST controller advice. To mark it as a REST controller advice, we use the @RestControllerAdvice annotation. The method the class defines is also called an exception handler. You specify what exceptions trigger a controller advice method using the @ExceptionHandler annotation over the method. The following listing shows the REST controller advice class’s definition and the excep- tion handler method that implements the logic associated with the NotEnoughMoneyException exception.

In production apps, you sometimes need to send information about the exception that occurred, from the controller’s action to the advice. In this case, you can add a parameter to the advice’s exception handler method of the type of the handled exception. Spring is smart enough to pass the exception reference from the controller to the advice’s exception handler method. You can then use any details of the exception instance in the advice’s logic.

```java
// We use the @RestControllerAdvice
// annotation to mark the class as a
// REST controller advice.
@RestControllerAdvice
public class ExceptionControllerAdvice {

    // We use the @ExceptionHandler method to associate
    // an exception with the logic the method implements.
    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ErrorDetails> exceptionNotEnoughMoneyHandler(NotEnoughMoneyException e) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDetails);
    }
}
```

## Using a request body to get data from the client

You learned in chapter 8 that you can send data in the HTTP request using request parameters and path variables. Because REST endpoints rely on the same Spring MVC mechanism, nothing from the syntaxes you learned in chapter 8 changes regarding sending data in request parameters and path variables. You can use the same annotations and implement the REST endpoints identically as you were implementing the controller actions for your web pages.

However, we didn’t discuss one essential thing: the HTTP request has a request body, and you can use it to send data from the client to the server. The HTTP request body is often used with REST endpoints.

To use the request body, you just need to annotate a parameter of the controller’s action with **@RequestBody**. By default, Spring assumes you used JSON to represent the parameter you annotated and will try to decode the JSON string into an instance of your parameter type. In the case Spring cannot decode the JSON-formatted string into that type, the app sends back a response with the status “400 Bad Request.”

To show this:

The controller defines an action mapped to the /payment path with HTTP POST and expects to get a request body of PaymentDetails type. The controller prints the amount of the PaymentDetails object in the server’s console and sends the same object in the response body back to the client.

```java
@RestController
public class PaymentController {
    private final PaymentService paymentService;
    private static Logger logger = Logger.getLogger(PaymentController.class.getName());

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    // We get the payment details from the HTTP request body.
    public ResponseEntity<PaymentDetails> makePayment(@RequestBody PaymentDetails paymentDetails) {

        // We log the amount of the payment in the server’s console.
        logger.info("Received payment: " + paymentDetails.getAmount());

        // PaymentDetails paymentDetails = paymentService.processPayment();

        // We send back the payment details object
        // in the HTTP response body, and we set the
        // HTTP response status to 202 ACCEPTED
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(paymentDetails);
    }
}
```
