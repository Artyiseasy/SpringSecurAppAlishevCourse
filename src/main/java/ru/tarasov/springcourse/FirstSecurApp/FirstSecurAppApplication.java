package ru.tarasov.springcourse.FirstSecurApp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;


@SpringBootApplication
public class FirstSecurAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstSecurAppApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
