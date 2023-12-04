package sia.tacocloud.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class IngredientRef {
    private final String ingredient;
}
