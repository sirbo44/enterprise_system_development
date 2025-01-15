package com.example.library_webapplication.repos;

import com.example.library_webapplication.entities.Book;
import com.example.library_webapplication.entities.JkBooksUser;
import com.example.library_webapplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JkBooksUserRepository extends JpaRepository<JkBooksUser, Integer> {
  JkBooksUser findByUsername(User username);

  List<JkBooksUser> findAllByUsername(User username);

  JkBooksUser findByUsernameAndIsbn(User username, Book isbn);
}