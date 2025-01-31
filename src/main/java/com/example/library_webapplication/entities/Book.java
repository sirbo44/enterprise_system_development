package com.example.library_webapplication.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "isbn", nullable = false, length = 24)
    private String isbn;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "language", nullable = false, length = 25)
    private String language;

    @Column(name = "`release`", nullable = false)
    private LocalDate release;

    @Lob
    @Column(name = "summary")
    private String summary;

    @Column(name = "pieces", nullable = false)
    private Integer pieces;

    @Column(name = "photo", length = 1000)
    private String photo;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDate getRelease() {
        return release;
    }

    public void setRelease(LocalDate release) {
        this.release = release;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getPieces() {
        return pieces;
    }

    public void setPieces(Integer pieces) {
        this.pieces = pieces;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}