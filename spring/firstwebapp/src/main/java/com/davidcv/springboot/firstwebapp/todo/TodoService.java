package com.davidcv.springboot.firstwebapp.todo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService {
    private static List<Todo> todos = new ArrayList<>();

    static {
        todos.add(new Todo(1, "david", "learn AWS", LocalDate.now().plusYears(1), false));
        todos.add(new Todo(2, "david", "learn JAVA", LocalDate.now().plusYears(1), false));
        todos.add(new Todo(3, "david", "learn PYTHON", LocalDate.now().plusYears(1), false));
    }

    public List<Todo> findByUsername(String username) {
        return todos;
    }
}