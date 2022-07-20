package com.tvv.db.impl;

import com.tvv.db.DBManager;
import com.tvv.db.dao.AuthorDAO;
import com.tvv.db.entity.Author;
import com.tvv.db.util.Fields;
import com.tvv.service.exception.AppException;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AuthorDAOImpl implements AuthorDAO {

    private static final String SQL__FIND_AUTHOR_BY_ID =
            "SELECT * FROM authors WHERE id=?";
    public static final String SQL__FIND_AUTHOR_BY_NAME =
            "select * from authors where first_name = ? and last_name = ?";
    private static final String SQL__FIND_AUTHORS_BY_BOOK_ID =
            "SELECT a.id, " +
                    "a.first_name, " +
                    "a.last_name " +
                    "FROM books_authors ba inner join authors a on ba.author_id=a.id " +
                    "WHERE ba.book_id=?;";

    public static final String SQL_INSERT_AUTHOR =
            "INSERT INTO authors (id, first_name, last_name) VALUES (0, ?, ?);";
    private static final String SQL_UPDATE_AUTHOR =
            "UPDATE authors SET first_name = ?, last_name = ? WHERE id = ?;";

    private static final String SQL_DELETE_AUTHOR =
            "DELETE FROM authors WHERE id = ?;";
    private final DBManager dbManager;

    public AuthorDAOImpl() {dbManager = DBManager.getInstance();}

    @Override
    public boolean create(Author author) throws AppException {
        PreparedStatement pstmt;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_AUTHOR);
            pstmt.setString(1, author.getFirstName());
            pstmt.setString(2, author.getLastName());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can not insert author to DB",ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con!=null) dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public Author findById(long id) throws AppException {
        Author author = new Author();
        PreparedStatement pstmt;
        ResultSet rs;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            AuthorLoad mapper = new AuthorLoad();
            pstmt = con.prepareStatement(SQL__FIND_AUTHOR_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                author = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can't find account by id", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con!=null) dbManager.commitCloseConnection(con);
        }
        return author;
    }

    @Override
    public boolean update(Author author) throws AppException {
        PreparedStatement pstmt;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_AUTHOR);
            pstmt.setString(1, author.getFirstName());
            pstmt.setString(2, author.getLastName());
            pstmt.setLong(3, author.getId());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can not update author in DB",ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con!=null) dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public boolean delete(Author author) throws AppException {
        PreparedStatement pstmt;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_AUTHOR);
            pstmt.setLong(1, author.getId());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can not delete author from DB",ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con!=null) dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public Set<Author> findAuthorsByBookId(long id) throws AppException {
        Set<Author> authors = new HashSet<>();
        PreparedStatement pstmt;
        ResultSet rs;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            AuthorLoad mapper = new AuthorLoad();
            con.createStatement();
            pstmt = con.prepareStatement(SQL__FIND_AUTHORS_BY_BOOK_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) authors.add(mapper.loadRow(rs));
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can't find authors by book id", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con!=null) dbManager.commitCloseConnection(con);
        }
        return authors;
    }

    private static class AuthorLoad implements LoadEntity<Author> {

        /**
         * Load object from ResultSet
         *
         * @param rs ResultSet
         * @return Author object
         * @throws AppException custom exception
         */
        @Override
        public Author loadRow(ResultSet rs) throws AppException {
            try {
                Author author = new Author();
                author.setId(rs.getLong(Fields.ENTITY__ID));
                author.setFirstName(rs.getString(Fields.AUTHOR__FIRST_NAME));
                author.setLastName(rs.getString(Fields.AUTHOR__LAST_NAME));
                return author;
            } catch (Exception e) {
                throw new AppException("Can't read data from DB, table: authors", e);
            }
        }
    }

}
