package com.example.library_webapplication.repos;

import com.example.library_webapplication.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
  Book findByIsbnAndTitle(String isbn, String title);

  Book findByIsbn(String isbn);

  List<Book> findByIsbnContaining(String isbn);

  List<Book> findByTitleContainingIgnoreCase(String title);

  Book deleteBooksByIsbn(String isbn);
}