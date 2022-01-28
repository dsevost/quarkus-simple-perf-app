package com.redhat.quarkus.backend;

import java.time.LocalDate;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Cacheable
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDate birth;

    public Person() {
    }

    public static Person by(String name, LocalDate birth) {
        return new Person(null, name, birth);
    }
    
    private Person(Long id, String name, LocalDate birth) {
        this.id = null;
        this.name = name;
        this.birth = birth;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return String.format("(Person) { id: '%s', name: '%s', birth: '%s' }", id, name, birth);
    }

}
