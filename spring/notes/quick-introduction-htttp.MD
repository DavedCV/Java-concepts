# Quick introduction to HTTP

## What is HTTP?

Is how a client communicates with the server in a web app. Applications prefer to have rigid ways to “speak,” and the protocols offer the rules they need to exchange information.

**HTTP:** Stateless, text-based, request-response protocol that uses the client- server computing model.

![](/images/httpDefinition.png)

## The HTTP request as a language between client and server

In the apps you implement with Spring, you’ll need to use the HTTP request to send data from client to server. If you implement the client, you’ll need to add data on the HTTP request. If you implement the server, you’ll need to get data from the request. Either way, you need to understand the HTTP request.

The HTTP request has a simple format. The things you have to take into consideration are the following:

1. **The request URI:** The client uses the path to tell the server what resource it requests. The request URI looks like this one: http://www.manning.com/books/spring-start-here

    The request URI identifies a resource on the server side the client wants to work with. The URI is the part of the HTTP request most people know about because we have to write a URI in our browser’s address bar each time we access a web site. The URI has a format like that in the next snippet. In the snippet, \<server_location> is the network address of the system where the server app runs, \<application_port> is the port num- ber identifying the server app instance running, and \<resource_path> is a path the developer associated with a specific resource. The client needs to request a specific path to work with a particular resource:

    ```http://<server_location>:<application_port>/<resource_path>```

2. **The request method:** A verb that the client uses to indicate what action it will do with the requested resource. For example, when you write an address in a web browser’s address bar, the browser always uses an HTTP method named GET. In other circumstances, you’ll find in the next paragraphs, the client can issue an HTTP request with a different method such as POST, PUT, or DELETE.

    Always remember that the verbs are not a constraint to what you implement. The HTTP protocol cannot force you not to implement an HTTP GET functionality that changes data on the backend side. However, you should never misuse the HTTP methods! Always consider the meaning of the HTTP method used to ensure your app’s reliability, security, and maintainability.

3. **The request parameters (optional):** Data in small quantity the client sends to the server with the request. When I say “small quantity,” I refer to something that can be expressed in maybe 10 to 50 characters. Parameters on the request aren’t mandatory. The request parameters (also referred to as query parameters) are sent in the URI by appending a query expression.
4. **The request headers (optional):** Data in small quantity sent in the request header. Unlike request parameters, these values are not visible in the URI.
5. **The request body (optional):** A larger quantity of data the client sends to the server in the request. When the client needs to send data composed of some hundreds of characters, it can use the HTTP body. A body on the request is not mandatory.

![](/images/httpRequestPieces.png)

## The HTTP response: The way the server responds

Once you take care of the client’s request in an app, it’s time to implement the server’s response. In response to a client’s request, the server sends the following:

- **The response status:** An integer between 100 and 599 that defines a short representation of the request’s result.
- **Response headers (optional):** Similar to request parameters, they represent key-value pair data. They are designed for sending a small amount of data (10 to 50 characters) from server to client in response to a client’s request.
- **The response body (optional):** A way for the server to send a larger quantity (eg., the server needs to send some hundreds of characters or entire files) of data back to the client.

The next snippet helps you visualize the HTTP response:

![](/images/httpResponsePieces.png)

The response status is the only mandatory detail a server delivers in response to a client’s request. The status tells the client if the server understood the request and everything worked fine, or if the server encountered issues while processing the client’s request.

The HTTP status is a short representation of the result of the full request (including if the server was able to manage the business logic of the request). You don’t need to learn all the statuses in detail.

- **Starting with 2**, meaning the server correctly processed the request. The request processing is okay, and the server executed what the client asked.

  - **200—OK** is the most known and most straightforward of the response statuses. It just tells the client the server didn’t encounter any issue when processing its request.
  - **201—Created** might be used, for example, in response to a POST request to tell the client that the server managed to add the requested resource. It’s not always mandatory to add such detail to the response status, and that’s why 200—OK is, in general, the most used response status to identify that everything’s okay.
  - **204—No Content** could tell the client it shouldn’t expect a response body for this response.

- **Starting with 4**, where the server tells the client something is wrong with its request (it’s a problem on the client side). For example, the client requested a resource that doesn’t exist, or the client sent some request parameters that the server didn’t expect.

  - **400—Bad Request**, A generic status often used to represent any kind of problem with the HTTP request (e.g., validation of the data or problem with reading a specific value in the request body or a request parameter).
  - **401—Unauthorized**, A status value generally used to communicate to the client that the request needs authentication.
  - **403—Forbidden**, A status value generally sent by the server to tell the client it’s not authorized to execute its request.
  - **404—Not Found**, A status value sent by the server to inform the client the requested resource doesn’t exist.

- **Starting with 5**, where the server communicates that something went wrong on its side. For example, the server needed to connect to a database but the it was not accessible. In this case, the server sends back a status telling the client that it couldn’t complete the request but not because of something the client didn’t do well.

  - **500—Internal Server Error**, This response status is a generic error value the server sends to inform the client that an issue occurred while the backend was processing its request.

## The HTTP session

In this section, we discuss the HTTP session, a mechanism that allows a server to store data between multiple request-response interactions with the same client. Remember that for HTTP every request is independent of another. In other words, a request doesn’t know anything about other previous, next, or simultaneous requests. A request cannot share data with order requests or access the details the backend responds for them.

However, you’ll find scenarios where the server needs to correlate some requests. A good example is the cart functionality of an online shop. A user adds multiple items to their cart. To add an item to the cart, the client makes a request. To add a second item, the client makes another request. The server needs a way to know that the same client previously added an item to the same cart.

One way to implement such behavior is using the HTTP session. The backend assigns a unique identifier named “session ID” to a client and then associates it with a place in the app’s memory. Each request the client sends after being assigned the session ID needs to contain the session ID in a request header. This way, the backend app knows to associate the specific session requests (figure C.4).

![](/images/httpSessionMechanism.png)

The HTTP session usually ends after a time if the client doesn’t send more requests. You can configure this time, usually both in the servlet container and the app. It shouldn’t be more than maybe a few hours. If the session lives too long, the server will spend a lot of memory. For most apps, a session ends after less than one hour if the client doesn’t send more requests.
