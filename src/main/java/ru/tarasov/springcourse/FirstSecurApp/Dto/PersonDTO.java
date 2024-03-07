package ru.tarasov.springcourse.FirstSecurApp.Dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {
        @NotEmpty(message = "Имя не может быть пустым")
        @Size(min = 1, max = 100)
        private String username;
        @Min(value = 1900, message = "Не может быть меньше 1900")
        private int year_of_birthday;
        private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYear_of_birthday() {
        return year_of_birthday;
    }

    public void setYear_of_birthday(int year_of_birthday) {
        this.year_of_birthday = year_of_birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
