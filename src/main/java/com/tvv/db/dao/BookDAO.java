package com.tvv.db.dao;

import com.tvv.db.DBManager;
import com.tvv.db.entity.Book;
import com.tvv.db.entity.CountBook;
import com.tvv.db.entity.RentBook;
import com.tvv.db.util.PageSettings;
import com.tvv.service.exception.AppException;

import java.util.List;

public interface BookDAO {
    void init(DBManager dbManager);

    boolean create(Book book, int count) throws AppException;

    Book findById(long id) throws AppException;

    CountBook findByIdWithCount(long id) throws AppException;

    boolean update(Book book, int count) throws AppException;

    boolean delete(Book book) throws AppException;

    List<CountBook> findAllBooks(PageSettings pageSettings) throws AppException;

    List<RentBook> findAllUserBooks(long userId, PageSettings pageSettings) throws AppException;

    long bookCount() throws AppException;

    long bookCount(long userId) throws AppException;

    long bookCount(String search) throws AppException;

    boolean addBookToRent(long bookId, long userId, int days) throws AppException;

    RentBook changeStartDateRentBook(long id, long bookId, int days) throws AppException;

    RentBook changePayStatusRentBook(long id) throws AppException;

    RentBook findRendBookByRentId(long id) throws AppException;

    boolean deleteRentBook(long id) throws AppException;

    boolean deleteRentBookById(long id) throws AppException;

    List<CountBook> findBooksWithSearch(PageSettings pageSettings, String search) throws AppException;

    boolean addBookToRentByUser(long bookId, long userId) throws AppException;

    boolean updateImageBookById(long bookId, String image) throws AppException;

    int countFineByUser(String number, long id) throws AppException;
}
