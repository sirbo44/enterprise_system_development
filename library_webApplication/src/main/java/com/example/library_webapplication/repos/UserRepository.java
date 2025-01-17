package com.example.library_webapplication.repos;

import com.example.library_webapplication.entities.Author;
import com.example.library_webapplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {
    User findByUsernameAndPassword(String username,String password);
    User findByUsername(String username);

}