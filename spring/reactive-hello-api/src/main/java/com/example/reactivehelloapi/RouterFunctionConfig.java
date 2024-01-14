package com.example.reactivehelloapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

@Configuration
public class RouterFunctionConfig {
    /*
    * - RouterFunction: declares mappings between one or more RequestPredicate objects and the functions that will
    * handle the matching request(s).
    * - The route() method from RouterFunctions accepts two parameters: a RequestPredicate and a function to handle
    * matching requests.
    * - Although it isn’t explicitly declared, the handler lambda accepts a ServerRequest as a parameter. It returns
    * a ServerResponse using ok() from Server- Response and body() from BodyBuilder, which was returned from ok(). This
    * was done to create a response with an HTTP 200 (OK) status code and a body payload that says "Hello World!"
    * */
    @Bean
    public RouterFunction<?> helloRouterFunction() {
        return route(GET("/hello"), request -> ok().body(just("Hello World!"), String.class))
                .andRoute(GET("/bye"), request -> ok().body(just("See ya!"), String.class));
    }
}
