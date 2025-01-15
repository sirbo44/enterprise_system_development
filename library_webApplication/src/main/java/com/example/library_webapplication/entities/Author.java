package com.example.library_webapplication.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 24)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 24)
    private String lastName;

    @Column(name = "DOB", nullable = false)
    private LocalDate dob;

    @Column(name = "Nationality", nullable = false, length = 24)
    private String nationality;

    @OneToOne(mappedBy = "author")
    private JkBooksAuthor jkBooksAuthor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public JkBooksAuthor getJkBooksAuthor() {
        return jkBooksAuthor;
    }

    public void setJkBooksAuthor(JkBooksAuthor jkBooksAuthor) {
        this.jkBooksAuthor = jkBooksAuthor;
    }

}