package com.tvv.db.dao;

import com.tvv.db.entity.Book;
import com.tvv.db.entity.CountBook;
import com.tvv.db.entity.RentBook;
import com.tvv.db.util.PageSettings;
import com.tvv.service.exception.AppException;

import java.util.List;

/**
 * Interface for work with Books, create pagination lists, CRUD, manage rent books and counts, update image, etc.
 */
public interface BookDAO {

    /**
     * Create book and add counts of books to DB
     *
     * @param book  entity book
     * @param count count of books in library
     * @return result
     * @throws AppException custom exception
     */
    boolean create(Book book, int count) throws AppException;

    /**
     * Find book from DB by ID
     *
     * @param id long value id
     * @return result
     * @throws AppException custom exception
     */
    Book findById(long id) throws AppException;

    /**
     * Find book by id with count
     *
     * @param id long value id
     * @return result CountBook (include book and count)
     * @throws AppException custom exception
     */
    CountBook findByIdWithCount(long id) throws AppException;

    /**
     * Update book and counts of books to DB
     *
     * @param book  entity book
     * @param count count of books in library
     * @return result
     * @throws AppException custom exception
     */
    boolean update(Book book, int count) throws AppException;

    /**
     * Delete book and counts of books from DB
     *
     * @param book entity book
     * @return result
     * @throws AppException custom exception
     */
    boolean delete(Book book) throws AppException;

    /**
     * Method return list of books with pagination settings
     *
     * @param pageSettings pagination settings
     * @return list of CountBook (books and count)
     * @throws AppException custom exception
     */
    List<CountBook> findAllBooks(PageSettings pageSettings) throws AppException;

    /**
     * Method return list of rented books by user with pagination settings
     *
     * @param userId       long value user id
     * @param pageSettings pagination settings
     * @return list of RentBook (books, user, start date, end date etc.)
     * @throws AppException custom exception
     */
    List<RentBook> findAllUserBooks(long userId, PageSettings pageSettings) throws AppException;

    /**
     * All count of books in DB
     *
     * @return long value result
     * @throws AppException custom exception
     */
    long bookCount() throws AppException;

    /**
     * All count of rented books by user (@param userId)
     *
     * @param userId long value user id
     * @return long value result count
     * @throws AppException custom exception
     */
    long bookCount(long userId) throws AppException;

    /**
     * All count of books by search text
     *
     * @param search search string parameter
     * @return long value result count
     * @throws AppException custom exception
     */
    long bookCount(String search) throws AppException;

    /**
     * Method create rented book row in DB
     *
     * @param bookId long value book id
     * @param userId long value user id
     * @param days   int value days for rent this book
     * @return success operation
     * @throws AppException custom exception
     */
    boolean addBookToRent(long bookId, long userId, int days) throws AppException;

    /**
     * Method changes start date for rent
     *
     * @param id     row id in DB (rented books)
     * @param bookId book id
     * @param days   int value days for rent this book
     * @return rented book
     * @throws AppException custom exception
     */
    RentBook changeStartDateRentBook(long id, long bookId, int days) throws AppException;

    /**
     * Change fine status for rented book (paid or didn't)
     *
     * @param id item id
     * @return result RentBook entity
     * @throws AppException custom exception
     */
    RentBook changePayStatusRentBook(long id) throws AppException;

    /**
     * Method for find rented book by row id
     *
     * @param id row id
     * @return entity RentBook
     * @throws AppException custom exception
     */
    RentBook findRendBookByRentId(long id) throws AppException;

    /**
     * Delete rented book from table (rent book)
     *
     * @param id long value row id
     * @return success operation
     * @throws AppException custom exception
     */
    boolean deleteRentBook(long id) throws AppException;

    /**
     * Delete (cancel booking) rented book from table
     *
     * @param id long value row id
     * @return success operation
     * @throws AppException custom exception
     */
    boolean deleteRentBookById(long id) throws AppException;

    /**
     * Method return list of books by searching string with pagination settings
     *
     * @param pageSettings pagination settings
     * @param search       pagination settings: searching string
     * @return list of CountBook (books, count)
     * @throws AppException custom exception
     */
    List<CountBook> findBooksWithSearch(PageSettings pageSettings, String search) throws AppException;

    /**
     * Create row for rent book (by user)
     *
     * @param bookId long value book id
     * @param userId long value book id
     * @return success operation
     * @throws AppException custom exception
     */
    boolean addBookToRentByUser(long bookId, long userId) throws AppException;

    /**
     * Method for update image (image link in DB)
     *
     * @param bookId long value book id
     * @param image  string value image link
     * @return success operation
     * @throws AppException custom exception
     */
    boolean updateImageBookById(long bookId, String image) throws AppException;

    /**
     * Method looks for all fine of user and return summary count
     *
     * @param number string value user login (library number)
     * @param id     long value user id
     * @return int value summary count
     * @throws AppException custom exception
     */
    int countFineByUser(String number, long id) throws AppException;

}
