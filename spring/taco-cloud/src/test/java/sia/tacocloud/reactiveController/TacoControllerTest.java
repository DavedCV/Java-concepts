package sia.tacocloud.reactiveController;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sia.tacocloud.domain.Ingredient;
import sia.tacocloud.domain.Taco;
import sia.tacocloud.repository.TacoReactiveRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TacoControllerTest {

    @Test
    public void shouldReturnRecentTacos() {
        Taco[] tacos = {
                testTaco(1L), testTaco(2L),
                testTaco(3L), testTaco(4L),
                testTaco(5L), testTaco(6L),
                testTaco(7L), testTaco(8L),
                testTaco(9L), testTaco(10L),
                testTaco(11L), testTaco(12L),
                testTaco(13L), testTaco(14L),
                testTaco(15L), testTaco(16L)
        };
        Flux<Taco> tacoFlux = Flux.just(tacos);

        // Mocks the taco repository
        TacoReactiveRepository tacoReactiveRepository = Mockito.mock(TacoReactiveRepository.class);
        when(tacoReactiveRepository.findAll()).thenReturn(tacoFlux);

        // Creates a WebTestClient
        WebTestClient testClient = WebTestClient.bindToController(new TacoController(tacoReactiveRepository)).build();

        // Request recent tacos
        // Verifies the expected response
        testClient.get().uri("/api/reactive/tacosControllers?recent").exchange().expectStatus().isOk().expectBody()
                  .jsonPath("$").isArray()
                  .jsonPath("$").isNotEmpty()
                  .jsonPath("$[0].id").isEqualTo(tacos[0].getId().toString())
                  .jsonPath("$[0].name").isEqualTo("Taco 1")
                  .jsonPath("$[1].id").isEqualTo(tacos[1].getId().toString())
                  .jsonPath("$[1].name").isEqualTo("Taco 2")
                  .jsonPath("$[11].id").isEqualTo(tacos[11].getId().toString())
                  .jsonPath("$[11].name").isEqualTo("Taco 12")
                  .jsonPath("$[12]").doesNotExist();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSaveATaco() {

        // Mock the repository
        TacoReactiveRepository tacoReactiveRepository = Mockito.mock(TacoReactiveRepository.class);

        // Creates a WebTestClient
        WebTestClient testClient = WebTestClient.bindToController(new TacoController(tacoReactiveRepository)).build();

        Mono<Taco> unsavedTacoMono = Mono.just(testTaco(1L));
        Taco savedTaco = testTaco(1L);
        Mono<Taco> savedTacoMono = Mono.just(savedTaco);

        // Sets up test data
        when(tacoReactiveRepository.save(any(Taco.class))).thenReturn(savedTacoMono);

        // Posts a taco
        testClient.post()
                  .uri("/api/reactive/tacosControllers")
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(unsavedTacoMono, Taco.class)
                  .exchange()
                  .expectStatus().isCreated()
                  .expectBody(Taco.class)
                  .isEqualTo(savedTaco);
    }

    private Taco testTaco(Long number) {
        Taco taco = new Taco();
        taco.setId(number);
        taco.setName("Taco " + number);

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP));
        ingredients.add(new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN));
        taco.setIngredients(ingredients);

        return taco;
    }
}
