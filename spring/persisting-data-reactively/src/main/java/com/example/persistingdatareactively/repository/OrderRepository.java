package com.example.persistingdatareactively.repository;

import com.example.persistingdatareactively.domain.TacoOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<TacoOrder, Long> {
}
