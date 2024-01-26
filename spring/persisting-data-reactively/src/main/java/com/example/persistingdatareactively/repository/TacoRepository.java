package com.example.persistingdatareactively.repository;

import com.example.persistingdatareactively.domain.Taco;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TacoRepository extends ReactiveCrudRepository<Taco, Long> {
}
