package sia.tacocloud.reactiveController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sia.tacocloud.domain.Taco;
import sia.tacocloud.repository.TacoReactiveRepository;

@RestController
@RequestMapping(path = "/api/reactive/tacosControllers", produces = "application/json")
@CrossOrigin(origins = "*")
public class TacoController {

    TacoReactiveRepository tacoReactiveRepository;

    @Autowired
    public TacoController(TacoReactiveRepository tacoReactiveRepository) {
        this.tacoReactiveRepository = tacoReactiveRepository;
    }

    @GetMapping(params = "recent")
    public Flux<Taco> recentTacos() {
        return tacoReactiveRepository.findAll().take(12);
    }

    @GetMapping("/{id}")
    public Mono<Taco> tacoById(@PathVariable("id") Long id) {
        return tacoReactiveRepository.findById(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Taco> postTaco(@RequestBody Mono<Taco> tacoMono) {
        return tacoMono.flatMap(tacoReactiveRepository::save);
    }


}
