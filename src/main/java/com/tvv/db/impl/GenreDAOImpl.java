package com.tvv.db.impl;

import com.tvv.db.DBManager;
import com.tvv.db.dao.GenreDAO;
import com.tvv.db.entity.Genre;
import com.tvv.db.util.Fields;
import com.tvv.service.exception.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class GenreDAOImpl implements GenreDAO {

    public static final String SQL__FIND_GENRE_BY_NAME =
            "select * from genres where name = ?";
    private static final String SQL__FIND_GENRE_BY_ID =
            "SELECT * FROM genres WHERE id=?";

    private static final String SQL__FIND_GENRES_BY_BOOK_ID =
            "SELECT g.id, " +
                    "g.name " +
                    "FROM book_genre bg inner join genres g on bg.genre_id=g.id " +
                    "WHERE bg.book_id=?;";

    public static final String SQL_INSERT_GENRE =
            "INSERT INTO genres (id, name) VALUES (0, ?);";
    private static final String SQL_UPDATE_GENRE =
            "UPDATE genres SET name = ? WHERE id = ?;";

    private static final String SQL_DELETE_GENRE =
            "DELETE FROM genres WHERE id = ?;";
    private final DBManager dbManager;

    public GenreDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    @Override
    public boolean create(Genre genre) throws AppException {
        PreparedStatement pstmt;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_GENRE);
            pstmt.setString(1, genre.getName());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can not insert author to DB", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con != null) dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public Genre findById(long id) throws AppException {
        Genre genre = null;
        PreparedStatement pstmt;
        ResultSet rs;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            GenreLoad mapper = new GenreLoad();
            pstmt = con.prepareStatement(SQL__FIND_GENRE_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                genre = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can't find genre by id", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con != null) dbManager.commitCloseConnection(con);
        }
        return genre;
    }

    @Override
    public boolean update(Genre genre) throws AppException {
        PreparedStatement pstmt;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_GENRE);
            pstmt.setString(1, genre.getName());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can not update genre in DB", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con != null) dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public boolean delete(Genre genre) throws AppException {
        PreparedStatement pstmt;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_GENRE);
            pstmt.setLong(1, genre.getId());
            result = pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can not delete author from DB", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con != null) dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public Set<Genre> findGenresByBookId(long id) throws AppException {
        Set<Genre> genres = new HashSet<>();
        PreparedStatement pstmt;
        ResultSet rs;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            GenreLoad mapper = new GenreLoad();
            con.createStatement();
            pstmt = con.prepareStatement(SQL__FIND_GENRES_BY_BOOK_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) genres.add(mapper.loadRow(rs));
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            if (con != null) {
                dbManager.rollbackCloseConnection(con);
                ex.printStackTrace();
                throw new AppException("Can't find genres by book id", ex);
            } else throw new AppException("Can not connect to DB", new NullPointerException());
        } finally {
            if (con != null) dbManager.commitCloseConnection(con);
        }
        return genres;
    }

    private static class GenreLoad implements LoadEntity<Genre> {

        /**
         * Load object from ResultSet
         *
         * @param rs ResultSet
         * @return Author object
         * @throws AppException custom exception
         */
        @Override
        public Genre loadRow(ResultSet rs) throws AppException {
            try {
                Genre genre = new Genre();
                genre.setId(rs.getLong(Fields.ENTITY__ID));
                genre.setName(rs.getString(Fields.GENRE__NAME));
                return genre;
            } catch (Exception e) {
                throw new AppException("Can't read data from DB, table: genres", e);
            }
        }
    }

}
