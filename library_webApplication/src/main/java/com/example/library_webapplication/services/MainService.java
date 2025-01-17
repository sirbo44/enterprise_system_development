package com.example.library_webapplication.services;

import com.example.library_webapplication.entities.Author;
import com.example.library_webapplication.entities.JkBooksUser;
import com.example.library_webapplication.entities.User;
import com.example.library_webapplication.entities.Book;
import com.example.library_webapplication.repos.AuthorRepository;
import com.example.library_webapplication.repos.BookRepository;
import com.example.library_webapplication.repos.JkBooksUserRepository;
import com.example.library_webapplication.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MainService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JkBooksUserRepository jkBooksUserRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public User dologin(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User uniqueUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<Book> allBooks() {
        return bookRepository.findAll();
    }

    public List<Object> searchList(String isbn, String title) {
        List<Object> mylist = new ArrayList<>();
        if (!bookRepository.findByIsbnContaining(isbn).isEmpty()) {
            mylist.add(bookRepository.findByIsbnContaining(isbn));
        }
        if (!bookRepository.findByTitleContainingIgnoreCase(title).isEmpty()) {
            mylist.add(bookRepository.findByTitleContainingIgnoreCase(title));
        }
        return mylist;
    }

    public Book deleteBooksByIsbn(String isbn){
        return bookRepository.deleteBooksByIsbn(isbn);
    }

    public JkBooksUser findByUsername(User username) {
        return jkBooksUserRepository.findByUsername(username);
    }

    public List<JkBooksUser> findAllByUsername(String username){
        return (List<JkBooksUser>) jkBooksUserRepository.findAllByUsername(uniqueUsername(username));
    }

    public JkBooksUser findByUsernameAndIsbn(User username, Book isbn){
        return jkBooksUserRepository.findByUsernameAndIsbn(username, isbn);
    }

    public Book findByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn);
    }

    public List<JkBooksUser> findAllJkBooksUser() {
        return (List<JkBooksUser>) jkBooksUserRepository.findAll();
    }

    public Long findAllCheckedInBooks() {
        return jkBooksUserRepository.count();
    }

    public Integer findAllCheckedOutBooks() {
        List<Book> books = bookRepository.findAll();
        int sum = 0;
        for (Book book: books){
            sum = sum + book.getPieces();
        }
        return sum;
    }

    public List<Author> findAllAuthors(){
        return authorRepository.findAll();
    }

    public Author findAuthorById(Integer id) {
        return authorRepository.findAuthorById(id);
    }
}
