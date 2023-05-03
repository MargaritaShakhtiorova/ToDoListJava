package com.example.todolist.services;

import com.example.todolist.models.Person;
import com.example.todolist.repositories.PeopleRepository;
import com.example.todolist.repositories.TaskRepository;
import com.example.todolist.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    public final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository, TaskRepository taskRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByEmail(username);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }

    public Person findByUsername(String username){
        return peopleRepository.findByEmail(username).get();
    }

    public Person findById(int id){
        Optional<Person> person = peopleRepository.findById(id);
        if(person.isPresent())
            return person.get();
        else return null;
    }

    public void makeAdmin(int id, Person person) {
        person.setRole("ROLE_ADMIN");
        peopleRepository.save(person);
    }
}