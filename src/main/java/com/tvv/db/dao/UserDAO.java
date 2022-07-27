package com.tvv.db.dao;

import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.db.util.PageSettings;
import com.tvv.service.exception.AppException;

import java.util.List;

public interface UserDAO {
    boolean create(User user) throws AppException;

    User findById(long id) throws AppException;

    boolean update(User user) throws AppException;

    boolean delete(User user) throws AppException;

    List<User> findAllUsers(PageSettings pageSettings, long role) throws AppException;

    long userCount(long role) throws AppException;

    User findUserByNumber(String number) throws AppException;

    String findFirstFreeNumber(long roleId) throws AppException;

    boolean updateLocale(long id, String currentLanguage) throws AppException;

    boolean updateImageBookById(long id, String image) throws AppException;
}
