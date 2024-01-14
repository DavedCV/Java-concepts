package com.example.tacocloudclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class TacoCloudClient {

    private RestTemplate rest;

    @Autowired
    public TacoCloudClient(RestTemplate rest) {
        this.rest = rest;
    }

    // get example -----------------------------------------------------------------------------------------------------
    public Ingredient getIngredientById(String ingredientId) {
        try {
            return rest.getForObject("http://localhost:8080/api/ingredients/{id}", Ingredient.class, ingredientId);
        } catch (HttpClientErrorException.NotFound notFoundException) {
            // Handle 404 Not Found response
            return null; // or throw a custom exception
        }
    }

    public Ingredient getIngredientById2(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity = rest.getForEntity("http://localhost:8080/api/ingredients/{id}",
                                                                      Ingredient.class, ingredientId);

        log.info("Fetched time: {}", responseEntity.getHeaders().getDate());
        return responseEntity.getBody();
    }

    // put example -----------------------------------------------------------------------------------------------------

    public void updateIngredient(Ingredient ingredient) {
        rest.put("http://localhost:8080/api/ingredients/{id}", ingredient, ingredient.getId());
    }

    // post example ----------------------------------------------------------------------------------------------------

    public Ingredient createIngredient(Ingredient ingredient) {
        return rest.postForObject("http://localhost:8080/api/ingredients", ingredient, Ingredient.class);
    }

    // delete example --------------------------------------------------------------------------------------------------

    public void deleteIngredient(Ingredient ingredient) {
        rest.delete("http://localhost:8080/api/ingredients/{id}", ingredient.getId());
    }
}
