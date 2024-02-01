package com.davidcv.springboot.firstwebapp.todo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService {
    private static List<Todo> todos = new ArrayList<>();
    private static long currentId = 0;

    static {
        todos.add(new Todo(++currentId, "david", "learn AWS", LocalDate.now().plusYears(1), false));
        todos.add(new Todo(++currentId, "david", "learn JAVA", LocalDate.now().plusYears(1), false));
        todos.add(new Todo(++currentId, "david", "learn PYTHON", LocalDate.now().plusYears(1), false));
    }

    public List<Todo> findByUsername(String username) {
        return todos;
    }

    public void addTodo(String username, String description, LocalDate targetDate, boolean done) {
        todos.add(new Todo(++currentId, username, description, targetDate, done));
    }
}