package com.tvv.service.dto;

import com.tvv.db.entity.RentBook;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class RentBookDTO {
    long id;
    String image;
    String author;
    String name;
    String number;
    String startDate;
    String endDate;
    String status;
    String statusPay;

    public RentBookDTO(RentBook rentBook) {
        this.id = rentBook.getId();
        this.image = rentBook.getBook().getImage();
        String setOfAuthors = rentBook.getBook().getAuthors()
                .stream().map(a->a.getFirstName()+ " "+a.getLastName())
                .collect(Collectors.joining(", "));
        this.author = setOfAuthors;
        this.name = rentBook.getBook().getName();
        this.number = rentBook.getUser().getNumber();
        String stringDate;
        stringDate = rentBook.getStartDate().equals(LocalDate.MIN)
                ? "" : rentBook.getStartDate().toString();
        this.startDate = stringDate;
        stringDate = rentBook.getEndDate().equals(LocalDate.MIN)
                ? "" : rentBook.getEndDate().toString();
        this.endDate = stringDate;
        this.status = rentBook.getStatus();
        this.statusPay = rentBook.getStatusPay()!=null ? rentBook.getStatusPay() : "";

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusPay() {
        return statusPay;
    }

    public void setStatusPay(String statusPay) {
        this.statusPay = statusPay;
    }
}
