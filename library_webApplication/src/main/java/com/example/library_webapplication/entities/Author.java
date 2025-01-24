package com.example.library_webapplication.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "DOB", nullable = false)
    private LocalDate dob;

    @Column(name = "Nationality", nullable = false, length = 24)
    private String nationality;

    @OneToMany(mappedBy = "author")
    private Set<JkBooksAuthor> jkBooksAuthors = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Set<JkBooksAuthor> getJkBooksAuthors() {
        return jkBooksAuthors;
    }

    public void setJkBooksAuthors(Set<JkBooksAuthor> jkBooksAuthors) {
        this.jkBooksAuthors = jkBooksAuthors;
    }

}