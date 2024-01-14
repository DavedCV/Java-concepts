package sia.tacocloud.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import sia.tacocloud.domain.Taco;
import sia.tacocloud.repository.TacoReactiveRepository;

import java.net.URI;
import java.util.Objects;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterFunctionConfig {

    @Autowired
    private TacoReactiveRepository tacoRepo;

    @Bean
    public RouterFunction<?> routerFunction() {
        return route(GET("/api/reactive/tacos").and(queryParam("recent", Objects::nonNull)), this::recents)
                .andRoute(POST("/api/reactive/tacos"), this::postTaco);
    }

    public Mono<ServerResponse> recents(ServerRequest request) {
        return ServerResponse.ok().body(tacoRepo.findAll().take(12), Taco.class);
    }

    public Mono<ServerResponse> postTaco(ServerRequest request) {
        return request.bodyToMono(Taco.class)
                      .flatMap(taco -> tacoRepo.save(taco))
                      .flatMap(savedTaco -> {
                          return ServerResponse
                                  .created(URI.create("http://localhost:8080/api/tacos/" + savedTaco.getId()))
                                  .body(savedTaco, Taco.class);
                      });
    }
}

