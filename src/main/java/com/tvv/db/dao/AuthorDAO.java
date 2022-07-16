package com.tvv.db.dao;

import com.tvv.db.entity.Author;
import com.tvv.db.entity.Book;
import com.tvv.service.exception.AppException;

import java.util.Set;

public interface AuthorDAO {
    boolean create(Author author) throws AppException;
    Author findById(long id) throws AppException;
    boolean update(Author author) throws AppException;
    boolean delete(Author author) throws AppException;
    Set<Author> findAuthorsByBookId(long id) throws AppException;

}
