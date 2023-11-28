package com.example.persistencewithspringdatajpa.repositories;

import com.example.persistencewithspringdatajpa.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
