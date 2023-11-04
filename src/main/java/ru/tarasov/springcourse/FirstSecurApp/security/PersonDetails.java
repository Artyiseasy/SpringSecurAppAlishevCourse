package ru.tarasov.springcourse.FirstSecurApp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.tarasov.springcourse.FirstSecurApp.models.Person;

import java.util.Collection;

/*
    Когда мы работаем с security мы не напрямую общаемся с сущностью, а через класс обертку
     */
public class PersonDetails implements UserDetails {
    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    //метод возвращает коллекцию тех прав, которые доступны после авторизации
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    //метод нужен чтобы получить аутентифицированого пользователя
    public Person getPerson(){
        return this.person;
    }
}
