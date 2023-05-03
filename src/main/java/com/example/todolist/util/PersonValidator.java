package com.example.todolist.util;

import com.example.todolist.models.Person;
import com.example.todolist.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

//    private PersonValidatorService personValidatorService;
//
//    @Autowired
//    public PersonValidator(PersonValidatorService personValidatorService) {
//        this.personValidatorService = personValidatorService;
//    }

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
//
//        Optional<Person> optionalPerson = personValidatorService.loadUserByUsername(person.getUsername());
//        if(!optionalPerson.isPresent()){
//            return;
//        }

        try {
            personDetailsService.loadUserByUsername(person.getEmail());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("email", "", "Person with this email already exist");
    }
}