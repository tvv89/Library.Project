package com.tvv.db.impl;

import com.tvv.db.DBManager;
import com.tvv.db.dao.PublisherDAO;
import com.tvv.db.entity.Publisher;
import com.tvv.db.util.Fields;
import com.tvv.service.exception.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PublisherDAOImpl implements PublisherDAO {

    public static final String SQL__FIND_PUBLISHER_BY_NAME =
            "select * from publishers where name = ?";
    private static final String SQL__FIND_PUBLISHER_BY_ID =
            "SELECT * FROM publishers WHERE id=?";

    public static final String SQL_INSERT_PUBLISHER =
            "INSERT INTO publishers (id, name, address, phone, city) " +
                    "VALUES (0, ?, ?, ?, ?);";

    private static final String SQL_UPDATE_PUBLISHER =
            "UPDATE publishers SET name = ?, " +
                    "address = ?," +
                    "phone = ?," +
                    "city = ? WHERE id = ?;";

    private static final String SQL_DELETE_PUBLISHER =
            "DELETE FROM publishers WHERE id = ?;";
    private DBManager dbManager;

    public PublisherDAOImpl() {dbManager = DBManager.getInstance();}

    @Override
    public boolean create(Publisher publisher) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_PUBLISHER);
            pstmt.setString(1, publisher.getName());
            pstmt.setString(2, publisher.getAddress());
            pstmt.setString(3, publisher.getPhone());
            pstmt.setString(4, publisher.getCity());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not insert Publisher to DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public Publisher findById(long id) throws AppException {
        Publisher publisher = new Publisher();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PublisherLoad mapper = new PublisherLoad();
            pstmt = con.prepareStatement(SQL__FIND_PUBLISHER_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                publisher = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't find publisher by id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return publisher;
    }

    @Override
    public boolean update(Publisher publisher) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_PUBLISHER);
            pstmt.setString(1, publisher.getName());
            pstmt.setString(2, publisher.getAddress());
            pstmt.setString(3, publisher.getPhone());
            pstmt.setString(4, publisher.getCity());
            pstmt.setLong(5, publisher.getId());
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
    public boolean delete(Publisher publisher) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_PUBLISHER);
            pstmt.setLong(1, publisher.getId());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not delete Publisher from DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    private static class PublisherLoad implements LoadEntity<Publisher> {

        /**
         * Load object from ResultSet
         *
         * @param rs ResultSet
         * @return Publisher object
         * @throws AppException
         */
        @Override
        public Publisher loadRow(ResultSet rs) throws AppException {
            try {
                Publisher publisher = new Publisher();
                publisher.setId(rs.getLong(Fields.ENTITY__ID));
                publisher.setName(rs.getString(Fields.PUBLISHER__NAME));
                publisher.setAddress(rs.getString(Fields.PUBLISHER__ADDRESS));
                publisher.setPhone(rs.getString(Fields.PUBLISHER__PHONE));
                publisher.setCity(rs.getString(Fields.PUBLISHER__CITY));
                return publisher;
            } catch (Exception e) {
                throw new AppException("Can't read data from DB, table: Publishers", e);
            }
        }
    }

}
