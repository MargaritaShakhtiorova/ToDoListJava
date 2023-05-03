package com.example.todolist.controllers;

import com.example.todolist.models.Person;
import com.example.todolist.repositories.PeopleRepository;
import com.example.todolist.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PeopleRepository peopleRepository;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public AdminController(PeopleRepository peopleRepository, PersonDetailsService personDetailsService) {
        this.peopleRepository = peopleRepository;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("/listOfUsers")
    public String listOfUsers(Model model) {
        model.addAttribute("people", peopleRepository.findAllByOrderByIdAsc());
        return "listOfUsers";
    }

    @PatchMapping("/makeadmin/{id}")
    public String makeAdmin (@PathVariable("id") int id){
        Person person = personDetailsService.findById(id);

        personDetailsService.makeAdmin(id, person);
        return "redirect:/admin/listOfUsers";
    }
}