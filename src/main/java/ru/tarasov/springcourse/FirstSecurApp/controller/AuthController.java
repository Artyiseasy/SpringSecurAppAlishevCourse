package ru.tarasov.springcourse.FirstSecurApp.controller;


import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tarasov.springcourse.FirstSecurApp.Dto.AuthenticationDTO;
import ru.tarasov.springcourse.FirstSecurApp.Dto.PersonDTO;
import ru.tarasov.springcourse.FirstSecurApp.models.Person;
import ru.tarasov.springcourse.FirstSecurApp.security.JWTUtil;
import ru.tarasov.springcourse.FirstSecurApp.services.RegistrationService;
import ru.tarasov.springcourse.FirstSecurApp.util.PersonValidator;


import java.sql.Struct;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService,
                          JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }



//    @GetMapping("/login")
//    public String loginPage(){
//        return "auth/login";
//    }
//
//    @GetMapping("/registration")
//    public String registrationPage(@ModelAttribute ("person") Person person){
//        return "auth/registration";
//    }

    @PostMapping("/registration")
    public Map performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                      BindingResult bindingResult){

    Person person = convertToPerson(personDTO);
    personValidator.validate(person, bindingResult);

    if(bindingResult.hasErrors()){
        return Map.of("message", "ошибка");
    }
        registrationService.register(person);

        String token = jwtUtil.generateToken(person.getUsername());
        return Map.of("jwt-token", token);
    }



    public Person convertToPerson(PersonDTO personDTO){
        return this.modelMapper.map(personDTO, Person.class);
    }
    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        }
        catch (BadCredentialsException e){
            return Map.of("message" , "Incorrect credetionals!");
        }
        String token =  jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token: ", token);
    }

}
