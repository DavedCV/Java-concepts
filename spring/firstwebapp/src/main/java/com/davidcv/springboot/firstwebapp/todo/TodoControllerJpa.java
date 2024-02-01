package com.davidcv.springboot.firstwebapp.todo;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@SessionAttributes("name")
public class TodoControllerJpa {

    private TodoRepository todoRepository;

    public TodoControllerJpa(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @RequestMapping(path = "listTodos")
    public String listAllTodos(ModelMap model) {
        String name = getLoggedInUsername();
        List<Todo> todos = todoRepository.findByUsername(name);
        model.addAttribute("todos", todos);
        return "listTodos";
    }

    @RequestMapping(path = "addTodo", method = RequestMethod.GET)
    public String showNewTodoPage(ModelMap model) {
        Todo todo = new Todo(0, getLoggedInUsername(), "Default Description", LocalDate.now().plusYears(1), false);
        model.put("todo", todo);
        return "todo";
    }

    @RequestMapping(path = "addTodo", method = RequestMethod.POST)
    public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {

        if (result.hasErrors()) {
            return "todo";
        }

        todo.setUsername(getLoggedInUsername());
        todoRepository.save(todo);

        return "redirect:listTodos";
    }

    @RequestMapping(path = "deleteTodo")
    public String deleteTodo(@RequestParam long id) {
        todoRepository.deleteById(id);
        return "redirect:listTodos";
    }

    @RequestMapping(path = "updateTodo", method = RequestMethod.GET)
    public String showUpdateTodo(@RequestParam long id, ModelMap model) {
        Todo todo = todoRepository.findById(id).get();
        model.put("todo", todo);
        return "todo";
    }

    @RequestMapping(path = "updateTodo", method = RequestMethod.POST)
    public String showUpdateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {

        if (result.hasErrors()) {
            return "todo";
        }

        todo.setUsername(getLoggedInUsername());
        todoRepository.save(todo);

        return "redirect:listTodos";
    }
}
