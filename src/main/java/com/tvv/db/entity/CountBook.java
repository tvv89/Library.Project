package com.tvv.db.entity;

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
}
