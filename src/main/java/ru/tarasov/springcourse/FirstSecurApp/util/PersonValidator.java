package ru.tarasov.springcourse.FirstSecurApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.tarasov.springcourse.FirstSecurApp.models.Person;
import ru.tarasov.springcourse.FirstSecurApp.services.PersonDetailsService;

import java.util.Optional;


@Component
public class PersonValidator implements Validator {

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
        try{
            personDetailsService.loadUserByUsername(person.getUsername());
        }catch (UsernameNotFoundException ignored){
            return; // Все ок, пользователь не найден
        }
        errors.rejectValue("username", "", "Такой человек уже существует");
    }
}
