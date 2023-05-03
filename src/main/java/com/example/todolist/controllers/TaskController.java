package com.example.todolist.controllers;

import com.example.todolist.models.Person;
import com.example.todolist.models.Task;
import com.example.todolist.services.PersonDetailsService;
import com.example.todolist.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public TaskController(TaskService taskService, PersonDetailsService personDetailsService) {
        this.taskService = taskService;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("tasks", taskService.findAllByPersonId(getCurrentPerson().getId()));
        model.addAttribute("person", personDetailsService.peopleRepository.findByEmail(getCurrentPerson().getEmail()).get());
        return "tasks/index";
    }

    @GetMapping("/create")
    public String newPerson(@ModelAttribute("task") Task task) {
        return "tasks/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("task") @Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "tasks/create";

        Person person = personDetailsService.findById(getCurrentPerson().getId());
        task.setPerson(person);
        taskService.save(task);
        return "redirect:/tasks";
    }

    private Person getCurrentPerson(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return personDetailsService.findByUsername(username);
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("task", taskService.findOne(id));
        return "tasks/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("task", taskService.findOne(id));
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("task") @Valid Task task, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "tasks/edit";

        taskService.rename(id, task, getCurrentPerson());
        return "redirect:/tasks";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @PatchMapping("/{id}/change")
    public String changeIsDone( @PathVariable("id") int id) {

        Task task = taskService.findOne(id);
        taskService.changeIsDone(id, task, getCurrentPerson());
        return "redirect:/tasks";
    }
}
