package com.example.tacocloudclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Slf4j
public class TacoCloudClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudClientApplication.class, args);
    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner fetchIngredients(TacoCloudClient tacoCloudClient) {
        return args -> {
            log.info("----------------------- GET -------------------------");
            log.info("GETTING INGREDIENT BY ID");
            log.info("Ingredient:  " + tacoCloudClient.getIngredientById("CHED"));
        };
    }

    /*
    @Bean
    public CommandLineRunner putAnIngredient(TacoCloudClient tacoCloudClient) {
        return args -> {
            log.info("----------------------- PUT -------------------------");
            Ingredient before = tacoCloudClient.getIngredientById("LETC");
            log.info("BEFORE:  " + before);
            tacoCloudClient.updateIngredient(new Ingredient("LETC", "Shredded Lettuce", Ingredient.Type.VEGGIES));
            Ingredient after = tacoCloudClient.getIngredientById("LETC");
            log.info("AFTER:  " + after);
        };
    }
    */

    @Bean
    public CommandLineRunner addAnIngredient(TacoCloudClient tacoCloudClient) {
        return args -> {
            log.info("----------------------- POST -------------------------");
            Ingredient chix = new Ingredient("CHIX", "Shredded Chicken", Ingredient.Type.PROTEIN);
            Ingredient chixAfter = tacoCloudClient.createIngredient(chix);
            log.info("AFTER=1:  " + chixAfter);
        };
    }

    @Bean
    public CommandLineRunner deleteAnIngredient(TacoCloudClient tacoCloudClient) {
        return args -> {
            log.info("----------------------- DELETE -------------------------");
            // start by adding a few ingredients so that we can delete them later...
            Ingredient beefFajita = new Ingredient("BFFJ", "Beef Fajita", Ingredient.Type.PROTEIN);
            tacoCloudClient.createIngredient(beefFajita);
            Ingredient shrimp = new Ingredient("SHMP", "Shrimp", Ingredient.Type.PROTEIN);
            tacoCloudClient.createIngredient(shrimp);

            Ingredient before = tacoCloudClient.getIngredientById("CHIX");
            log.info("BEFORE:  " + before);
            tacoCloudClient.deleteIngredient(before);
            Ingredient after = tacoCloudClient.getIngredientById("CHIX");
            log.info("AFTER:  " + after);
            before = tacoCloudClient.getIngredientById("BFFJ");
            log.info("BEFORE:  " + before);
            tacoCloudClient.deleteIngredient(before);
            after = tacoCloudClient.getIngredientById("BFFJ");
            log.info("AFTER:  " + after);
            before = tacoCloudClient.getIngredientById("SHMP");
            log.info("BEFORE:  " + before);
            tacoCloudClient.deleteIngredient(before);
            after = tacoCloudClient.getIngredientById("SHMP");
            log.info("AFTER:  " + after);

        };
    }
}
