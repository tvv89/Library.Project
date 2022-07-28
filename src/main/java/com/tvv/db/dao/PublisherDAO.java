package com.tvv.db.dao;

import com.tvv.db.entity.Publisher;
import com.tvv.service.exception.AppException;

public interface PublisherDAO {
    boolean create(Publisher publisher) throws AppException;

    Publisher findById(long id) throws AppException;

    boolean update(Publisher publisher) throws AppException;

    boolean delete(Publisher publisher) throws AppException;
}
