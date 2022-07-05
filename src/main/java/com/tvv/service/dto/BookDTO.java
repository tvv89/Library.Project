package com.tvv.service.dto;

import com.tvv.db.entity.CountBook;

import java.util.Objects;
import java.util.stream.Collectors;

public class BookDTO {
    long id;
    String isbn;
    String author;
    String name;
    String publisher;
    String year;
    String image;
    String genre;
    int count;

    public BookDTO() {
    }

    public BookDTO(CountBook countBook) {
        this.id = countBook.getBook().getId();
        this.isbn = countBook.getBook().getIsbn();
        String setOfAuthors = countBook.getBook().getAuthors()
                .stream().map(a->a.getFirstName()+ " "+a.getLastName())
                .collect(Collectors.joining(", "));
        this.author = setOfAuthors;
        this.name = countBook.getBook().getName();
        this.publisher = countBook.getBook().getPublisher().getName();
        this.year = countBook.getBook().getYear().toString();
        this.image = countBook.getBook().getImage();
        String setOfGenres = countBook.getBook().getGenres()
                .stream().map(g->g.getName())
                .collect(Collectors.joining(", "));
        this.genre = setOfGenres;
        this.count = countBook.getCount();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", year='" + year + '\'' +
                ", image='" + image + '\'' +
                ", genre='" + genre + '\'' +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Objects.equals(toString(), bookDTO.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, author, name, publisher, year, image, genre, count);
    }
}
