package com.example.library_webapplication.repos;

import com.example.library_webapplication.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
  }