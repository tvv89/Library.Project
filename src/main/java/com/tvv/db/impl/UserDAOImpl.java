package com.tvv.db.impl;

import com.tvv.db.DBManager;
import com.tvv.db.dao.RoleDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.db.util.Fields;
import com.tvv.db.util.PageSettings;
import com.tvv.service.exception.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static final String SQL__FIND_USER_BY_ID =
            "SELECT * FROM users WHERE id=?";
    private static final String SQL__FIND_USER_BY_NUMBER =
            "SELECT * FROM users WHERE number=?";
    private static final String SQL_INSERT_USER =
            "INSERT INTO users (id, number, password, first_name, last_name," +
                    "date_of_birth, phone, status, photo, role_id, locale) " +
                    "VALUES (0, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE_USER =
            "UPDATE users SET number = ?, password = ?, first_name = ?, last_name = ?," +
                    "date_of_birth = ?, phone = ?, status = ?, photo = ?, role_id = ?, locale = ?" +
                    "WHERE id = ?;";
    private static final String SQL_UPDATE_USER_LOCALE =
            "UPDATE users SET locale = ? " +
                    "WHERE id = ?;";

    private static final String SQL_DELETE_USER =
            "DELETE FROM users WHERE id = ?;";
    private static final String SQL_FIND_ALL_USERS_PAGINATION_NUMBER =
            "SELECT * FROM users WHERE role_id=? ORDER BY number limit ?,? ";
    private static final String SQL_FIND_ALL_USERS_PAGINATION_NAME =
            "SELECT * FROM users WHERE role_id=? ORDER BY last_name limit ?,?";
    private static final String SQL_COUNT_USER =
            "SELECT count(*) as count FROM users WHERE role_id = ?";
    private static final String SQL__FIND_FREE_NUMBER =
            "SELECT max(cast(number as UNSIGNED)) as number FROM users WHERE role_id = ?";

    private DBManager dbManager;

    public UserDAOImpl() {dbManager = DBManager.getInstance();}

    @Override
    public boolean create(User user) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_USER);
            pstmt.setString(1, user.getNumber());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setDate(5, Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(6, user.getPhone());
            pstmt.setString(7, user.getStatus());
            pstmt.setString(8, user.getPhoto());
            pstmt.setLong(9, user.getRole().getId());
            pstmt.setString(10, user.getLocale());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not insert User to DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public User findById(long id) throws AppException {
        User user = new User();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL__FIND_USER_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                user = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't find User by id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return user;
    }

    @Override
    public boolean update(User user) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER);
            pstmt.setString(1, user.getNumber());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setDate(5, Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(6, user.getPhone());
            pstmt.setString(7, user.getStatus());
            pstmt.setString(8, user.getPhoto());
            pstmt.setLong(9, user.getRole().getId());
            pstmt.setString(10, user.getLocale());
            pstmt.setLong(11, user.getId());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not update Publisher in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public boolean delete(User user) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_USER);
            pstmt.setLong(1, user.getId());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not delete User from DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public List<User> findAllUsers(PageSettings pageSettings, long role) throws AppException {
        List<User> users = new ArrayList<>();
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            UserLoad mapper = new UserLoad();
            String executeSQLScript;
            switch (pageSettings.getSort()){
                case "lastName":
                    executeSQLScript = SQL_FIND_ALL_USERS_PAGINATION_NAME;
                    break;
                default:
                    executeSQLScript = SQL_FIND_ALL_USERS_PAGINATION_NUMBER;
                    break;
            }
            pstmt = con.prepareStatement(executeSQLScript);
            long startIndex = (pageSettings.getPage()-1) * pageSettings.getSize();
            pstmt.setLong(1, role);
            pstmt.setLong(2, startIndex);
            pstmt.setLong(3, pageSettings.getSize());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                user = mapper.loadRow(rs);
                users.add(user);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found users in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return users;
    }

    @Override
    public long userCount(long role) throws AppException {
        long count = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_COUNT_USER);
            pstmt.setLong(1, role);
            rs = pstmt.executeQuery();
            if (rs.next())
                count = rs.getLong("count");
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found users in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return count;
    }

    @Override
    public User findUserByNumber(String number) throws AppException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL__FIND_USER_BY_NUMBER);
            pstmt.setString(1, number);
            rs = pstmt.executeQuery();
            if (rs.next())
                user = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't find User by id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return user;
    }

    @Override
    public String findFirstFreeNumber(long roleId) throws AppException {
        long number = 0L;
        String str = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL__FIND_FREE_NUMBER);
            pstmt.setLong(1, roleId);
            rs = pstmt.executeQuery();
            if (rs.next())
                number = rs.getLong("number");
            number++;
            rs.close();
            pstmt.close();
            str = String.valueOf(number);
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't find max number by id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return str;
    }

    @Override
    public boolean updateLocale(long id, String currentLanguage) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER_LOCALE);
            pstmt.setString(1, currentLanguage);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not update Publisher in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    private static class UserLoad implements LoadEntity<User> {
        RoleDAO roleDAO = new RoleDAOImpl();

        /**
         * Load object from ResultSet
         *
         * @param rs ResultSet
         * @return User object
         * @throws AppException
         */
        @Override
        public User loadRow(ResultSet rs) throws AppException {
            try {
                User user = new User();
                user.setId(rs.getLong(Fields.ENTITY__ID));
                user.setNumber(rs.getString(Fields.USER__NUMBER));
                user.setPassword(rs.getString(Fields.USER__PASSWORD));
                user.setFirstName(rs.getString(Fields.USER__FIRST_NAME));
                user.setLastName(rs.getString(Fields.USER__LAST_NAME));
                user.setDateOfBirth(rs.getDate(Fields.USER__DATE_OF_BIRTH).toLocalDate());
                user.setPhone(rs.getString(Fields.USER__PHONE));
                user.setStatus(rs.getString(Fields.USER__STATUS));
                user.setPhoto(rs.getString(Fields.USER__PHOTO));

                Role role = roleDAO.findById(rs.getInt(Fields.USER__ROLE));
                user.setRole(role);

                user.setLocale(rs.getString(Fields.USER__LOCAL));
                return user;
            } catch (Exception e) {
                throw new AppException("Can't read data from DB, table: User", e);
            }
        }
    }

}
