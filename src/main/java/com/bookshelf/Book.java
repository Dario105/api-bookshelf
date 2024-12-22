package com.bookshelf;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/*
record Book (@Id Long id, String title, String author, int book_read_number, boolean book_read){


}
*/

@Entity
public class Book {

	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private int bookReadNumber; // Cambié el nombre para seguir las convenciones de camelCase
    private boolean bookRead;

    // Constructor sin argumentos (requerido por JPA)
    public Book() {}

    // Constructor completo
    public Book(Long id, String title, String author, int bookReadNumber, boolean bookRead) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.bookReadNumber = bookReadNumber;
        this.bookRead = bookRead;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBookReadNumber() {
        return bookReadNumber;
    }

    public void setBookReadNumber(int bookReadNumber) {
        this.bookReadNumber = bookReadNumber;
    }

    public boolean isBookRead() {
        return bookRead;
    }

    public void setBookRead(boolean bookRead) {
        this.bookRead = bookRead;
    }
}
