package com.example.tacocloudclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class TacoClientReactive {


    private WebClient webClient;

    @Autowired
    public TacoClientReactive(WebClient webClient) {
        this.webClient = webClient;
    }

    // Getting resources -----------------------------------------------------------------------------------------------

    public Mono<Ingredient> getIngredinetById(Long ingredientId) {
        Mono<Ingredient> ingredient = webClient
                .get()
                .uri("/ingredients/{id}", ingredientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.just(new UnknownIngredientException()))
                .onStatus(status -> status == HttpStatus.NOT_FOUND,
                          response -> Mono.just(new UnknownIngredientException())) // We can be this level specific
                .bodyToMono(Ingredient.class);

        ingredient.subscribe(i -> {
        }, e -> {
        });

        return ingredient;
    }

    public Flux<Ingredient> getIngredients() {
        Flux<Ingredient> ingredients = webClient
                .get()
                .uri("/ingredients")
                .retrieve()
                .bodyToFlux(Ingredient.class);

        ingredients.timeout(Duration.ofSeconds(1)).subscribe(i -> {
        }, e -> {
        });

        return ingredients;
    }

    // Posting resources -----------------------------------------------------------------------------------------------

    public Mono<Ingredient> createIngredient() {
        Mono<Ingredient> ingredientMono = Mono.just(new Ingredient("INGC", "Ingredient C", Ingredient.Type.VEGGIES));

        Mono<Ingredient> result = webClient
                .post()
                .uri("/ingredients")
                .body(ingredientMono, Ingredient.class)
                .retrieve()
                .bodyToMono(Ingredient.class);

        result.subscribe(i -> {
        });

        return result;
    }

    public Mono<Ingredient> createIngredient2(Ingredient ingredient) {

        Mono<Ingredient> result = webClient
                .post()
                .uri("/ingredients")
                .bodyValue(ingredient)
                .retrieve()
                .bodyToMono(Ingredient.class);

        result.subscribe(i -> {
        });

        return result;
    }

    // Putting resources -----------------------------------------------------------------------------------------------

    public void updateIngredient(Ingredient ingredient) {
        Mono<Void> result = webClient
                .put()
                .uri("/ingredients/{id}", ingredient.getId())
                .bodyValue(ingredient)
                .retrieve()
                .bodyToMono(Void.class);

        result.subscribe();
    }

    // Deleting resources ----------------------------------------------------------------------------------------------
    public void deleteIngredient(Ingredient ingredient) {
        Mono<Void> result = webClient
                .delete()
                .uri("/ingredient/{id}", ingredient.getId())
                .retrieve()
                .bodyToMono(Void.class);

        result.subscribe();
    }
}
