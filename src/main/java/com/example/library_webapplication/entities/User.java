package com.example.library_webapplication.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "username", nullable = false, length = 24)
    private String username;

    @Column(name = "password", nullable = false, length = 24)
    private String password;

    @ColumnDefault("'member'")
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @OneToMany(mappedBy = "username")
    private Set<JkBooksUser> jkBooksUsers = new LinkedHashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<JkBooksUser> getJkBooksUsers() {
        return jkBooksUsers;
    }

    public void setJkBooksUsers(Set<JkBooksUser> jkBooksUsers) {
        this.jkBooksUsers = jkBooksUsers;
    }

}