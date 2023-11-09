# Consuming REST endpoints

REST services are a common way to implement the communication between two system components. The client of a web app can call the backend, and so can another backend component. In a backend solution composed of multiple services (see appendix A), these components need to “speak” to exchange data, so when you implement such a service using Spring, you need to know how to call a REST endpoint exposed by another service

![](/images/consumingRestEndpoints.png)

In this chapter, you’ll learn three ways to call REST endpoints from a Spring app:

1. OpenFeign: best
2. RestTemplate
3. WebClient

![](/images/consumingRestEndpointsExampleModel.png)

With the first project, we implement the payment service app.

We’ll model the payment with the Payment class, as presented in the next code snippet:

```java
public class Payment {
    private String id;
    private double amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
```

The method receives a Payment instance and sets a random ID to the payment before returning it. The endpoint is simple but good enough for our demonstration. We use HTTP POST. We need to specify a request header and the request body. The endpoint returns a header in the HTTP response and the Payment object in the response body when called.

You can now run this app, and it will start Tomcat on port 8080, which is the Spring Boot default. The endpoint is accessible, and you could call it with cURL or Postman. But the purpose of this chapter is to learn how to implement an app that calls the endpoint.

## Calling REST endpoints using Spring Cloud OpenFeign

In this section, we discuss a modern approach for calling REST endpoints from a Spring app.

With OpenFeign, as you’ll find out in the example we write in this section, you only need to write an interface, and the tool provides you with the implementation.

![](/images/consumeRestEndpointWithOpenfeignm.png)

To teach you how OpenFeign works, we’ll create the project and implement an app that uses OpenFeign to call the endpoint that the previous app exposes.

We’ll define an interface where we declare the methods that consume REST endpoints. The only thing we need to do is annotate these methods to define the path, the HTTP method, and eventually parameters, headers, and the body of the request. The interesting thing is that we don’t need to implement the methods ourselves. You define with the interface methods based on the annotations, and Spring knows to implement them. We rely again on the excellent magic of Spring.

![](/images/consumeRestEndpointsWIthOpenfeign.png)

To use Openfeign we need:

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

Once you have the dependency in place, you can create the proxy interface. In OpenFeign terminology, we also name this interface the OpenFeign client. OpenFeign implements this interface, so you don’t have to bother writing the code that calls the endpoint. You only need to use a few annotations to tell OpenFeign how to send the request.

```java
// We use the @FeignClient annotation to configure
// the REST client. A minimal configuration defines a
// name and the endpoint base URI.
@FeignClient(name = "payments", url = "${name.service.url}")
public interface PaymentsProxy {

    // We specify the endpoint’s and http method
    @PostMapping("/payment")
    // we define the request headers and body
    Payment createPayment(
            @RequestHeader String requestId,
            @RequestBody Payment payment
    );
}
```

The first thing to do is annotate the interface with the @FeignClient annotation to tell OpenFeign it has to provide an implementation for this contract. We have to assign a name to the proxy using the name attribute of the @FeignClient annotation, which OpenFeign internally uses. The name uniquely identifies the client in your app. The @FeignClient annotation is also where we specify the base URI of the request. You can define the base URI as a string using the url attribute of @FeignClient.

> Ensure you always store URIs and other details that might differ from one environment to another in the properties files and never hardcode them in the app.

You can define a property in the project’s “application.properties” file and refer it from the source code using the following syntax: ${property_name}. Using this practice, you don’t need to recompile the code when you want to run the app in different environments.

Each method you declare in the interface represents a REST endpoint call. You use the same annotations you learned in chapter 10 for the controller’s actions to expose REST endpoints:

- To specify the path and HTTP method: @GetMapping, @PostMapping, @Put-
Mapping, and so on
- To specify a request header: @RequestHeader
- To specify the request body: @RequestBody

OpenFeign needs to know where to find the interfaces defining the client contracts. We use the @EnableFeignClients annotation on a configuration class to enable the OpenFeign functionality and tell OpenFeign where to search for the client contracts.

```java
@Configuration
// We enable the OpenFeign clients and tell the OpenFeign dependency where to search for the proxy contracts.
@EnableFeignClients(basePackages = {"com.example.consumingrestendpointsopenfeign"})
public class ProjectConfiguration {
}
```

You can now inject the OpenFeign client through the interface you defined. Once you enable OpenFeign, it knows to implement the interfaces annotated with @FeignClient. We already discussed that Spring is smart enough to provide you with a bean instance from its context when you use an abstraction, and this is exactly what you do here.

```java
@RestController
public class PaymentController {
    private final PaymentsProxy paymentsProxy;

    public PaymentController(PaymentsProxy paymentsProxy) {
        this.paymentsProxy = paymentsProxy;
    }

    @PostMapping("/payment")
    public Payment createPayment(
            @RequestBody Payment payment
    )
    {
        String requestId = UUID.randomUUID().toString();
        return paymentsProxy.createPayment(requestId, payment);
    }
}
```

Now start both projects (the payments service and this section’s app) and call the app’s /payment endpoint using cURL or Postman.

## Calling REST endpoints using RestTemplate

We again implement the app that calls the /payment endpoint of the payment service, but this time we use a different approach: RestTemplate.

It is being put to sleep not because it’s not working properly or because it’s not a good tool. But as apps evolved, we started to need more capabilities. Developers wanted to be able to benefit from different things that aren’t easy to implement with RestTemplate, such as the following:

- Calling the endpoints both synchronously and asynchronously
- Writing less code and treating fewer exceptions (eliminate boilerplate code)
- Retrying call executions and implementing fallback operations (logic performed when the app can’t execute a specific REST call for any reason)

The steps for defining the call are as follows:

1. Define the HTTP headers by creating and configuring an HttpHeaders
instance.
2. Create an HttpEntity instance that represents the request data (headers and body).
3. Send the HTTP call using the exchange() method and get the HTTP response.

```java
@Component
public class PaymentProxy {
    private final RestTemplate rest;

    // We take the URL to the payment
    // service from the properties file.
    @Value("${name.service.url}")
    private String paymentsServiceURL;

    // We inject the RestTemplate from the
    // Spring context using constructor DI.
    @Autowired
    public PaymentProxy(RestTemplate rest) {
        this.rest = rest;
    }

    public Payment createPayment(Payment payment) {
        String uri = paymentsServiceURL + "/payment";

        // We build the HttpHeaders object to
        // define the HTTP request headers.
        HttpHeaders headers = new HttpHeaders();
        headers.add("requestId", UUID.randomUUID().toString());

        // We build the HttpEntity object
        // to define the request data.
        HttpEntity<Payment> httpEntity = new HttpEntity<>(payment, headers);

        // We send the HTTP request
        // and retrieve the data on
        // the HTTP response.
        ResponseEntity<Payment> response = rest.exchange(uri, HttpMethod.POST, httpEntity, Payment.class);

        // We return the HTTP response body.
        return response.getBody();
    }
}
```

Observe how the createPayment() method defines the header by creating an HttpHeaders instance and adding the needed header “requestId” to this instance using the add() method. It then creates a HttpEntity instance based on the headers and the body (received by the method as a parameter). The method then sends the HTTP request using RestTemplate’s exchange() method. The exchange() method’s parameters are the URI and the HTTP method, followed by the HttpEntity instance (that holds the request data) and the type expected for the response body.

We define a simple endpoint to call this implementation:

```java
@RestController
public class PaymentController {
    private final PaymentProxy paymentsProxy;

    public PaymentController(PaymentProxy paymentsProxy) {
        this.paymentsProxy = paymentsProxy;
    }

    @PostMapping("/payment")
    public Payment createPayment(
            @RequestBody Payment payment
    )
    {
        return paymentsProxy.createPayment(payment);
    }
}
```

We run both apps, the payments service and this section’s app, on different ports to validate our implementation works as expected.
