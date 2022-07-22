package com.tvv.db.entity;

import java.util.Objects;

public class CountBook {
    long id;
    Book book;
    int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CountBook{" +
                "id=" + id +
                ", book=" + book +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountBook countBook = (CountBook) o;
        return id == countBook.id && count == countBook.count && Objects.equals(book, countBook.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, count);
    }
}
