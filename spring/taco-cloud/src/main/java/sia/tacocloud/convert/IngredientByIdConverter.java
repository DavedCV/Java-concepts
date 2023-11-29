package sia.tacocloud.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sia.tacocloud.domain.Ingredient;
import sia.tacocloud.domain.Ingredient.Type;
import sia.tacocloud.repository.IngredientRepository;

import java.util.HashMap;
import java.util.Map;

/*
* This converter is used in the DesignTacoController, to convert the data from the post request to a valid taco format
* */
@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientByIdConverter(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient convert(String id) {
        return ingredientRepository.findById(id).orElse(null);
    }
}
