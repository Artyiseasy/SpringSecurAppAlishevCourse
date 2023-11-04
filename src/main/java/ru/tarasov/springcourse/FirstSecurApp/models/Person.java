package ru.tarasov.springcourse.FirstSecurApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "Person")
public class Person {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 1, max = 100)
    @Column(name = "username")
    private String username;
    @Column(name = "year_of_birth")
    @Min(value = 1900, message = "Не может быть меньше 1900")
    private int year_of_birthday;
    @Column(name = "password")
    private String password;

    public Person() {}

    public Person(String username, int year_of_birthday) {
        this.username = username;
        this.year_of_birthday = year_of_birthday;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public int getYear_of_birthday() {return year_of_birthday;}
    public void setYear_of_birthday(int year_of_birthday) {this.year_of_birthday = year_of_birthday;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", year_of_birthday=" + year_of_birthday +
                ", password='" + password + '\'' +
                '}';
    }
}
