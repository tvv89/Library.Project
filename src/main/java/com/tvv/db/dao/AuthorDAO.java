package com.tvv.db.dao;

import com.tvv.db.entity.Author;
import com.tvv.db.entity.Book;
import com.tvv.service.exception.AppException;

import java.util.Set;

/**
 * Interface for work with Authors (CRUD)
 */
public interface AuthorDAO {
    /**
     * Method for create author in DB
     * @param author entity author
     * @return result of creation (false/true)
     * @throws AppException custom exception
     */
    boolean create(Author author) throws AppException;

    /**
     * Method find Author from DB by id
     * @param id long value author id in DB
     * @return entity author
     * @throws AppException custom exception
     */
    Author findById(long id) throws AppException;

    /**
     * Method for update author in DB
     * @param author entity Author with new personnel field
     * @return result
     * @throws AppException custom exception
     */
    boolean update(Author author) throws AppException;

    /**
     * Method for delete author from DB
     * @param author entity Author
     * @return result
     * @throws AppException custom exception
     */
    boolean delete(Author author) throws AppException;

}
