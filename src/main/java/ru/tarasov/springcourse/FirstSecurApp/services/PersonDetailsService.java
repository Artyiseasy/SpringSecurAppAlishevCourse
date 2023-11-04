package ru.tarasov.springcourse.FirstSecurApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.tarasov.springcourse.FirstSecurApp.models.Person;
import ru.tarasov.springcourse.FirstSecurApp.repositories.PeopleRepositories;
import ru.tarasov.springcourse.FirstSecurApp.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepositories peopleRepositories;

    @Autowired
    public PersonDetailsService(PeopleRepositories peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepositories.findByUsername(username);
        if (person.isEmpty())
            throw new UsernameNotFoundException("user not found");


            return new PersonDetails(person.get());

    }
}
