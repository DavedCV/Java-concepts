package sia.tacocloud.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import sia.tacocloud.domain.Taco;

public interface TacoReactiveRepository extends ReactiveCrudRepository<Taco, Long> {
}
