package ru.tarasov.springcourse.FirstSecurApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.tarasov.springcourse.FirstSecurApp.services.PersonDetailsService;

import java.util.ArrayList;

    @Component
    public class CustomAuthenticationProvider implements AuthenticationProvider {
        private final PersonDetailsService personDetailsService;

        @Autowired
        public CustomAuthenticationProvider(PersonDetailsService personDetailsService) {
            this.personDetailsService = personDetailsService;
        }

        @Override
        public Authentication authenticate(Authentication authentication)
                throws AuthenticationException {

            String name = authentication.getName();
            String password = authentication.getCredentials().toString();
            UserDetails personDetails = personDetailsService.loadUserByUsername(name);

            if (!password.equals(personDetails.getPassword()))
                throw new BadCredentialsException("Incorrect Password");

                return new UsernamePasswordAuthenticationToken(
                        name, password, new ArrayList<>());
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(UsernamePasswordAuthenticationToken.class);
        }
    }
