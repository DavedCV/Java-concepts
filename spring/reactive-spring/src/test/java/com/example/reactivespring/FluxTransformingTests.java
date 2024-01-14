package com.example.reactivespring;

import lombok.Data;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FluxTransformingTests {
    @Test
    public void skipAFew() {
        Flux<String> countFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred").skip(3);

        StepVerifier.create(countFlux)
                    .expectNext("ninety nine", "one hundred")
                    .verifyComplete();
    }

    @Test
    public void skipAFewSeconds() {
        Flux<String> countFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
                                     .delayElements(Duration.ofSeconds(1))
                                     .skip(Duration.ofSeconds(4));

        StepVerifier.create(countFlux)
                    .expectNext("ninety nine", "one hundred")
                    .verifyComplete();
    }

    // Opposite of skip
    @Test
    public void take() {
        Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Acadia")
                                            .take(3);

        StepVerifier.create(nationalParkFlux)
                    .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                    .verifyComplete();
    }

    @Test
    public void takeForAWhile() {
        Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                                            .delayElements(Duration.ofSeconds(1))
                                            .take(Duration.ofMillis(3500));

        StepVerifier.create(nationalParkFlux)
                    .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                    .verifyComplete();
    }

    @Test
    public void filter() {
        Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                                            .filter(np -> !np.contains(" "));

        StepVerifier.create(nationalParkFlux)
                    .expectNext("Yellowstone", "Yosemite", "Zion")
                    .verifyComplete();
    }

    @Test
    public void distinct() {
        Flux<String> animalFlux = Flux.just("dog", "cat", "bird", "dog", "bird", "anteater")
                                      .distinct();

        StepVerifier.create(animalFlux)
                    .expectNext("dog", "cat", "bird", "anteater")
                    .verifyComplete();
    }

    @Test
    public void map() {
        Flux<Player> playerFlux =
                Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                    .map(n -> {
                        String[] split = n.split("\\s");
                        return new Player(split[0], split[1]);
                    });

        StepVerifier.create(playerFlux)
                    .expectNext(new Player("Michael", "Jordan"))
                    .expectNext(new Player("Scottie", "Pippen"))
                    .expectNext(new Player("Steve", "Kerr"))
                    .verifyComplete();
    }

    @Test
    public void flatMap() {
        Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                                      .flatMap(
                                              n -> Mono.just(n).map(p -> {
                                                           String[] split = p.split("\\s");
                                                           return new Player(split[0], split[1]);
                                                       })
                                                       .subscribeOn(Schedulers.parallel())
                                      );

        List<Player> playerList = Arrays.asList(
                new Player("Michael", "Jordan"),
                new Player("Scottie", "Pippen"),
                new Player("Steve", "Kerr")
        );

        StepVerifier.create(playerFlux)
                    .expectNextMatches(playerList::contains)
                    .expectNextMatches(playerList::contains)
                    .expectNextMatches(playerList::contains)
                    .verifyComplete();
    }

    @Test
    public void buffer() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");
        Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);

        StepVerifier.create(bufferedFlux)
                    .expectNext(Arrays.asList("apple", "orange", "banana"))
                    .expectNext(Arrays.asList("kiwi", "strawberry"))
                    .verifyComplete();
    }

    @Test
    public void bufferAndFlatMap() throws Exception {
        Flux.just("apple", "orange", "banana", "kiwi", "strawberry")
            .buffer(3)
            .flatMap(x -> Flux.fromIterable(x)
                              .map(y -> y.toUpperCase())
                              .subscribeOn(Schedulers.parallel())
                              .log()
            ).subscribe();
    }

    @Test
    public void collectList() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");
        Mono<List<String>> fruitListMono = fruitFlux.collectList();

        StepVerifier.create(fruitListMono)
                    .expectNext(Arrays.asList("apple", "orange", "banana", "kiwi", "strawberry"))
                    .verifyComplete();
    }

    @Test
    public void collectMap() {
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");
        Mono<Map<Character, String>> animalMapMono =
                animalFlux.collectMap(a -> a.charAt(0));

        StepVerifier.create(animalMapMono)
                    .expectNextMatches(map -> {
                        return map.size() == 3 &&
                                map.get('a').equals("aardvark") &&
                                map.get('e').equals("eagle") &&
                                map.get('k').equals("kangaroo");
                    })
                    .verifyComplete();
    }

    @Data
    private static class Player {
        private final String firstName;
        private final String lastName;
    }


}
