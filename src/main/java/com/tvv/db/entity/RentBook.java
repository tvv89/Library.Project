package com.tvv.db.entity;

import java.time.LocalDate;
import java.time.Year;
import java.util.Objects;
import java.util.Set;

public class RentBook extends Entity{
    long id;
    private Book book;
    private User user;
    private LocalDate startDate;
    private LocalDate endDate;
    private String statusPay;
    private String status;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatusPay() {
        return statusPay;
    }

    public void setStatusPay(String statusPay) {
        this.statusPay = statusPay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RentBook{" +
                "book=" + book.toString() +
                ", user=" + user.toString() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentBook rentBook = (RentBook) o;
        return id == rentBook.id && Objects.equals(book, rentBook.book) && Objects.equals(user, rentBook.user) && Objects.equals(startDate, rentBook.startDate) && Objects.equals(endDate, rentBook.endDate) && Objects.equals(statusPay, rentBook.statusPay) && Objects.equals(status, rentBook.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, user, startDate, endDate, statusPay, status);
    }
}
