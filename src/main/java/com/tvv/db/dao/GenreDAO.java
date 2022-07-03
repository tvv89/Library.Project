package com.tvv.db.dao;

import com.tvv.db.entity.Genre;
import com.tvv.service.exception.AppException;

import java.util.Set;

public interface GenreDAO {
    boolean create(Genre genre) throws AppException;
    Genre findById(long id) throws AppException;
    boolean update(Genre genre) throws AppException;
    boolean delete(Genre genre) throws AppException;
    Set<Genre> findGenresByBookId(long id) throws AppException;
}
