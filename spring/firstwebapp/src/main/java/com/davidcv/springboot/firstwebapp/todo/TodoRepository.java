package com.davidcv.springboot.firstwebapp.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, long> {
    public List<Todo> findByUsername(String username);
}
