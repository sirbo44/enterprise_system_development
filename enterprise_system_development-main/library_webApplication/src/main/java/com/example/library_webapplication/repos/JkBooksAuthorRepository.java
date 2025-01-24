package com.example.library_webapplication.repos;

import com.example.library_webapplication.entities.Book;
import com.example.library_webapplication.entities.JkBooksAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JkBooksAuthorRepository extends JpaRepository<JkBooksAuthor, Integer> {
    JkBooksAuthor deleteByBookCode(Book isbn);
    List<JkBooksAuthor> findAll();

//    List<JkBooksAuthor> findByBookCode(String isbn);
}