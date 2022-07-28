package com.tvv.db.dao;

import com.tvv.db.entity.Role;
import com.tvv.service.exception.AppException;

public interface RoleDAO {
    boolean create(Role role) throws AppException;

    Role findById(long id) throws AppException;

    boolean update(Role role) throws AppException;

    boolean delete(Role role) throws AppException;
}
