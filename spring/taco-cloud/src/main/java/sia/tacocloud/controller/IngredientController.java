package sia.tacocloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.tacocloud.domain.Ingredient;
import sia.tacocloud.repository.IngredientRepository;
import sia.tacocloud.repository.TacoRepository;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/ingredients", produces = "application/json")
public class IngredientController {
    private final TacoRepository tacoRepository;
    private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientController(IngredientRepository ingredientRepository,
                                TacoRepository tacoRepository)
    {
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable String id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);

        return optionalIngredient
                .map(ingredient -> new ResponseEntity<>(ingredient, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> putIngredient(@PathVariable String id, @RequestBody Ingredient ingredient) {
        ingredientRepository.save(ingredient);
        return new ResponseEntity<>(ingredient, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ingredient> postIngredient(@RequestBody Ingredient ingredient) {
        ingredientRepository.save(ingredient);
        return new ResponseEntity<>(ingredient, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredientById(@PathVariable String id) {
        ingredientRepository.deleteById(id);
    }

}
