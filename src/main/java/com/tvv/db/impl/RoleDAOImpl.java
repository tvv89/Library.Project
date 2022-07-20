package com.tvv.db.impl;

import com.tvv.db.DBManager;
import com.tvv.db.dao.RoleDAO;
import com.tvv.db.entity.Publisher;
import com.tvv.db.entity.Role;
import com.tvv.db.util.Fields;
import com.tvv.service.exception.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAOImpl implements RoleDAO {

    private static final String SQL__FIND_ROLE_BY_ID =
            "SELECT * FROM roles WHERE id=?";

    private static final String SQL_INSERT_ROLE =
            "INSERT INTO roles (id, name, status) " +
                    "VALUES (0, ?, ?);";

    private static final String SQL_UPDATE_ROLE =
            "UPDATE roles SET name = ?, " +
                    "status = ?" +
                    "WHERE id = ?;";

    private static final String SQL_DELETE_ROLE =
            "DELETE FROM roles WHERE id = ?;";
    private final DBManager dbManager;

    public RoleDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    @Override
    public boolean create(Role role) throws AppException {
        PreparedStatement pstmt;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_ROLE);
            pstmt.setString(1, role.getName());
            pstmt.setString(2, role.getStatus());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can not insert Role to DB", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con != null) dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public Role findById(long id) throws AppException {
        Role role = new Role();
        PreparedStatement pstmt;
        ResultSet rs;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            RoleLoad mapper = new RoleLoad();
            pstmt = con.prepareStatement(SQL__FIND_ROLE_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                role = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can't find role by id", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con != null) dbManager.commitCloseConnection(con);
        }
        return role;
    }

    @Override
    public boolean update(Role role) throws AppException {
        PreparedStatement pstmt;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_ROLE);
            pstmt.setString(1, role.getName());
            pstmt.setString(2, role.getStatus());
            pstmt.setLong(3, role.getId());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can not update Publisher in DB", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con != null) dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public boolean delete(Role role) throws AppException {
        PreparedStatement pstmt;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_ROLE);
            pstmt.setLong(1, role.getId());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can not delete Role from DB", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con != null) dbManager.commitCloseConnection(con);
        }
        return result;
    }

    private static class RoleLoad implements LoadEntity<Role> {

        /**
         * Load object from ResultSet
         *
         * @param rs ResultSet
         * @return Role object
         * @throws AppException custom exception
         */
        @Override
        public Role loadRow(ResultSet rs) throws AppException {
            try {
                Role role = new Role();
                role.setId(rs.getLong(Fields.ENTITY__ID));
                role.setName(rs.getString(Fields.ROLE__NAME));
                role.setStatus(rs.getString(Fields.ROLE__STATUS));
                return role;
            } catch (Exception e) {
                throw new AppException("Can't read data from DB, table: Role", e);
            }
        }
    }

}
