package com.tvv.db.impl;

import com.tvv.db.DBManager;
import com.tvv.db.dao.*;
import com.tvv.db.entity.*;
import com.tvv.db.util.Fields;
import com.tvv.db.util.PageSettings;
import com.tvv.service.exception.AppException;

import java.sql.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookDAOImpl implements BookDAO {

    private static final String SQL__INSERT_BOOK =
            "INSERT INTO books (id, isbn, name, year, image, publisher_id) " +
                    "values (0, ?, ?, ?, ?, ?);";
    private static final String SQL__INSERT_BOOK_AUTHOR =
            "INSERT INTO books_authors (id, book_id, author_id) " +
                    "values (0, ?, ?)";
    private static final String SQL__INSERT_BOOK_GENRE =
            "INSERT INTO book_genre (id, book_id, genre_id) " +
                    "values (0, ?, ?)";

    private static final String SQL__INSERT_BOOK_COUNT =
            "INSERT INTO current_books (id, book_id, count) " +
                    "values (0, ?, ?)";

    private static final String SQL__FIND_BOOK_BY_ID =
            "SELECT * FROM books WHERE id=?";

    private static final String SQL__FIND_BOOK_BY_ID_WITH_COUNT =
            "SELECT b.*, cb.count as count FROM books b inner join current_books cb on b.id=cb.book_id WHERE b.id=?";
    private static final String SQL_FIND_ALL_BOOKS_PAGINATION_AUTHOR =
            "SELECT b.id as id," +
                    "   GROUP_CONCAT(a.first_name, ' ', a.last_name SEPARATOR ', ') as author," +
                    "   b.isbn as isbn," +
                    "   b.name as name," +
                    "   b.year as year," +
                    "   b.image as image," +
                    "   b.publisher_id as publisher_id, " +
                    "   cb.count as count, " +
                    "   cb.id as cb_id " +
                    "FROM books_authors inner join authors a on author_id=a.id " +
                    "inner join books b on book_id = b.id  " +
                    "inner join current_books cb on b.id = cb.book_id " +
                    "GROUP BY book_id order by author limit ?,?;";

    private static final String SQL_FIND_ALL_BOOKS_PAGINATION_PUBLISHER =
            "SELECT b.id as id," +
                    "   p.name as publisher," +
                    "   b.isbn as isbn," +
                    "   b.name as name," +
                    "   b.year as year," +
                    "   b.image as image," +
                    "   b.publisher_id as publisher_id, " +
                    "   cb.count as count, " +
                    "   cb.id as cb_id " +
                    "FROM books b inner join publishers p on publisher_id=p.id " +
                    "inner join current_books cb on b.id = cb.book_id " +
                    "order by publisher limit ?,?;";


    private static final String SQL_FIND_REND_BOOK_BY_ID = "select bu.id as id," +
            "bu.book_id as book_id, " +
            "bu.user_id as user_id, " +
            "bu.start_date as start_date, " +
            "bu.end_date as end_date, " +
            "IF(bu.start_date>date(0), " +
            "if(bu.end_date<curdate() and bu.status_pay<>'paid', 'need pay', 'reading'),'booked') as status, " +
            "bu.status_pay as status_pay " +
            "from book_user bu " +
            "inner join books b inner join publishers p on b.publisher_id=p.id on book_id = b.id " +
            "inner join users u on user_id = u.id " +
            "where bu.id = ? ";
    private static final String SQL_FIND_ALL_BOOKS_PAGINATION_NAME =
            "SELECT *, cb.count as count, cb.id as cb_id FROM books b " +
                    "inner join current_books cb on b.id = cb.book_id order by name limit ?,?;";
    private static final String SQL_FIND_ALL_BOOKS_PAGINATION_ISBN =
            "SELECT *, cb.count as count, cb.id as cb_id FROM books b " +
                    "inner join current_books cb on b.id = cb.book_id order by isbn limit ?,?;";
    private static final String SQL_COUNT_BOOK =
            "SELECT count(*) as count FROM books";

    private static final String SQL_COUNT_BOOK_BY_USER_ID =
            "SELECT count(*) as count FROM book_user where user_id = ?";

    private static final String SQL_COUNT_BOOK_BY_ALL_USER =
            "SELECT count(*) as count FROM book_user";

    private static final String SQL_UPDATE_RENT_BOOK_START_DATE =
            "UPDATE book_user SET start_date = curdate(), " +
                    "end_date = DATE_ADD(curdate(), INTERVAL 30 DAY)" +
                    "WHERE id = ?";
    private static final String SQL_UPDATE_RENT_BOOK_STATUS_PAY =
            "UPDATE book_user SET status_pay = 'paid' " +
                    "WHERE id = ?";

    private static final String SQL_UPDATE_BOOK_IMAGE =
            "UPDATE books SET image = ? " +
                    "WHERE id = ?";

    private static final String SQL_DELETE_REND_BOOK_BY_ID =
            "DELETE FROM book_user WHERE id = ?";
    private static final String SQL_UPDATE_ADD_BOOK_COUNT_BY_BOOK_ID =
            "UPDATE current_books SET count = count + 1 WHERE book_id = ?";
    private static final String SQL_UPDATE_REMOVE_BOOK_COUNT_BY_BOOK_ID =
            "UPDATE current_books SET count = count - 1 WHERE book_id = ?";

    private static final String SQL_UPDATE_BOOK_BY_ID =
            "UPDATE books SET isbn = ?," +
                    "name = ?, " +
                    "year = ? " +
                    "  WHERE id = ?";

    private static final String SQL_UPDATE_BOOK_COUNT_BY_BOOK_ID =
            "UPDATE current_books SET count = ? " +
                    "  WHERE book_id = ?";
    private static final String SQL_INSERT_RENT_BOOK_BY_USER =
            "INSERT INTO book_user (id, book_id, user_id, start_date, end_date, status_pay) " +
                    "VALUES (0, ?, ?, curdate(), DATE_ADD(curdate(), INTERVAL 30 DAY), '')";
    private static final String SQL_INSERT_RENT_BOOK_BY_USER_FROM_USER =
            "INSERT INTO book_user (id, book_id, user_id, start_date, end_date, status_pay) " +
                    "VALUES (0, ?, ?, null, null, '')";

    private static final String SQL_DELETE_BOOK_BY_ID =
            "DELETE FROM books WHERE id= ?";
    private static final String SQL_COUNT_FINE_USER =
            "select count(*) as count from (select " +
                    " IF(bu.start_date>date(0),if(bu.end_date<curdate() and bu.status_pay<>'paid', 'need pay', 'reading'),'booked') as stat " +
                    "from book_user bu " +
                    "inner join users u on bu.user_id=u.id " +
                    "where u.number = ? or u.id = ?) as table1 " +
                    "where table1.stat like '%need pay%'";
    private DBManager dbManager;

    @Override
    public void init(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public BookDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    @Override
    public boolean create(Book book, int count) throws AppException {
        Connection con = null;
        PreparedStatement pstmt;
        if (book == null || count < 0)
            throw new AppException("Can not insert book to DB", new IllegalArgumentException());
        boolean result;
        try {
            con = dbManager.getConnection();
            //AUTHORS KEYS
            Set<Long> authorsId = new HashSet<>();
            for (Author author : book.getAuthors()) {
                pstmt = con.prepareStatement(AuthorDAOImpl.SQL__FIND_AUTHOR_BY_NAME);
                pstmt.setString(1, author.getFirstName());
                pstmt.setString(2, author.getLastName());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    authorsId.add(rs.getLong(Fields.ENTITY__ID));
                } else {
                    pstmt = con.prepareStatement(AuthorDAOImpl.SQL_INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, author.getFirstName());
                    pstmt.setString(2, author.getLastName());
                    pstmt.executeUpdate();
                    rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        authorsId.add(rs.getLong(1));
                    }
                }
            }
            //GENRE KEYS
            Set<Long> genresId = new HashSet<>();
            for (Genre genre : book.getGenres()) {
                pstmt = con.prepareStatement(GenreDAOImpl.SQL__FIND_GENRE_BY_NAME);
                pstmt.setString(1, genre.getName());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    genresId.add(rs.getLong(Fields.ENTITY__ID));
                } else {
                    pstmt = con.prepareStatement(GenreDAOImpl.SQL_INSERT_GENRE, Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, genre.getName());
                    pstmt.executeUpdate();
                    rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        genresId.add(rs.getLong(1));
                    }
                }
            }
            //PUBLISHER KEY
            long publisherId = 0;
            pstmt = con.prepareStatement(PublisherDAOImpl.SQL__FIND_PUBLISHER_BY_NAME);
            pstmt.setString(1, book.getPublisher().getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                publisherId = rs.getLong(Fields.ENTITY__ID);
            } else {
                pstmt = con.prepareStatement(PublisherDAOImpl.SQL_INSERT_PUBLISHER, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, book.getPublisher().getName());
                pstmt.setString(2, book.getPublisher().getAddress());
                pstmt.setString(3, book.getPublisher().getPhone());
                pstmt.setString(4, book.getPublisher().getCity());
                pstmt.executeUpdate();
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    publisherId = rs.getLong(1);
                }
            }
            //INSERT BOOK
            long bookId = 0;
            pstmt = con.prepareStatement(SQL__INSERT_BOOK, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getName());
            pstmt.setString(3, book.getYear().toString());
            pstmt.setString(4, book.getImage());
            pstmt.setLong(5, publisherId);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                bookId = rs.getLong(1);
            }
            //INSERT BOOK--AUTHORS
            for (Long i : authorsId) {
                pstmt = con.prepareStatement(SQL__INSERT_BOOK_AUTHOR);
                pstmt.setLong(1, bookId);
                pstmt.setLong(2, i);
                pstmt.executeUpdate();
            }
            //INSERT BOOK--GENRE
            for (Long i : genresId) {
                pstmt = con.prepareStatement(SQL__INSERT_BOOK_GENRE);
                pstmt.setLong(1, bookId);
                pstmt.setLong(2, i);
                pstmt.executeUpdate();
            }
            //INSERT BOOK--COUNT
            pstmt = con.prepareStatement(SQL__INSERT_BOOK_COUNT);
            pstmt.setLong(1, bookId);
            pstmt.setLong(2, count);
            pstmt.executeUpdate();
            rs.close();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not insert book to DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public Book findById(long id) throws AppException {
        Book book = new Book();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        BookLoad mapper = new BookLoad();
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL__FIND_BOOK_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                book = mapper.loadRow(rs);
                mapper.loadFields(con, book, rs);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't find publisher by id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return book;
    }

    @Override
    public CountBook findByIdWithCount(long id) throws AppException {
        CountBook bookCount = new CountBook();
        Book book;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL__FIND_BOOK_BY_ID_WITH_COUNT);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                bookCount.setId(rs.getLong(Fields.ENTITY__ID));
                bookCount.setCount(rs.getInt("count"));
                BookLoad mapper = new BookLoad();
                book = mapper.loadRow(rs);
                mapper.loadFields(con, book, rs);
                bookCount.setBook(book);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't find findByIdWithCount by book id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return bookCount;
    }

    @Override
    public boolean update(Book book, int count) throws AppException {
        Connection con = null;
        PreparedStatement pstmt = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_BOOK_BY_ID);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getName());
            pstmt.setString(3, book.getYear().toString());
            pstmt.setLong(4, book.getId());
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(SQL_UPDATE_BOOK_COUNT_BY_BOOK_ID);
            pstmt.setInt(1, count);
            pstmt.setLong(2, book.getId());
            pstmt.executeUpdate();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not update book to DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }


    @Override
    public boolean delete(Book book) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_BOOK_BY_ID);
            pstmt.setLong(1, book.getId());
            pstmt.executeUpdate();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found books in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public List<CountBook> findAllBooks(PageSettings pageSettings) throws AppException {
        List<CountBook> books = new ArrayList<>();
        Book book = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            String executeSQLScript;
            switch (pageSettings.getSort()) {
                case Fields.BOOK__ISBN:
                    executeSQLScript = SQL_FIND_ALL_BOOKS_PAGINATION_ISBN;
                    break;
                case "publisher":
                    executeSQLScript = SQL_FIND_ALL_BOOKS_PAGINATION_PUBLISHER;
                    break;
                case "author":
                    executeSQLScript = SQL_FIND_ALL_BOOKS_PAGINATION_AUTHOR;
                    break;
                default:
                    executeSQLScript = SQL_FIND_ALL_BOOKS_PAGINATION_NAME;
                    break;
            }
            pstmt = con.prepareStatement(executeSQLScript);
            long startIndex = (pageSettings.getPage() - 1) * pageSettings.getSize();
            pstmt.setLong(1, startIndex);
            pstmt.setLong(2, pageSettings.getSize());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BookLoad mapper = new BookLoad();
                book = mapper.loadRow(rs);
                mapper.loadFields(con, book, rs);
                CountBook cb = new CountBook();
                cb.setId(rs.getLong("cb_id"));
                cb.setBook(book);
                cb.setCount(rs.getInt("count"));
                books.add(cb);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found book in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return books;
    }

    @Override
    public List<RentBook> findAllUserBooks(long userId, PageSettings pageSettings) throws AppException {
        List<RentBook> books = new ArrayList<>();
        RentBook book = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String userCriteria;
        if (userId == 0) userCriteria = "";
        else userCriteria = "where bu.user_id=? ";
        try {
            con = dbManager.getConnection();
            String sortColumn;
            switch (pageSettings.getSort()) {
                case "isbn":
                    sortColumn = "b.isbn";
                    break;
                case "startDate":
                    sortColumn = "bu.start_date";
                    break;
                case "user":
                    sortColumn = "u.number";
                    break;
                default:
                    sortColumn = "b.name";
                    break;
            }
            String query = "select bu.id as id," +
                    "bu.book_id as book_id, " +
                    "bu.user_id as user_id, " +
                    "bu.start_date as start_date, " +
                    "bu.end_date as end_date, " +
                    "IF(bu.start_date>date(0), " +
                    "if(bu.end_date<curdate() and bu.status_pay<>'paid', 'need pay', 'reading'),'booked') as status, " +
                    "bu.status_pay as status_pay " +
                    "from book_user bu " +
                    "inner join books b inner join publishers p on b.publisher_id=p.id on book_id = b.id " +
                    "inner join users u on user_id = u.id " +
                    userCriteria +
                    "order by " + sortColumn + " limit ?,? ";
            pstmt = con.prepareStatement(query);
            long startIndex = (pageSettings.getPage() - 1) * pageSettings.getSize();
            if (userId > 0) {
                pstmt.setLong(1, userId);
                pstmt.setLong(2, startIndex);
                pstmt.setLong(3, pageSettings.getSize());
            } else {
                pstmt.setLong(1, startIndex);
                pstmt.setLong(2, pageSettings.getSize());
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BookLoad mapper = new BookLoad();
                book = mapper.loadRentBook(con, rs);
                books.add(book);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found users in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return books;
    }

    @Override
    public long bookCount() throws AppException {
        long count = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_COUNT_BOOK);
            rs = pstmt.executeQuery();
            if (rs.next())
                count = rs.getLong("count");
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found books in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return count;
    }

    @Override
    public long bookCount(long userId) throws AppException {
        long count = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            String query = SQL_COUNT_BOOK_BY_ALL_USER;
            if (userId > 0) query = SQL_COUNT_BOOK_BY_USER_ID;
            pstmt = con.prepareStatement(query);
            if (userId > 0) pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next())
                count = rs.getLong("count");
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found book_user in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return count;
    }

    @Override
    public long bookCount(String search) throws AppException {
        long count = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            String query = "select count(*) as count from (" +
                    "SELECT count(*)" +
                    "                    FROM books b " +
                    "                    inner join books_authors ba on ba.book_id = b.id " +
                    "                    inner join authors a on ba.author_id=a.id " +
                    "                    inner join current_books cb on b.id = cb.book_id " +
                    "                    where LOWER(b.name) like LOWER(CONCAT('%', ?, '%')) " +
                    "                    or LOWER(a.first_name) like LOWER(CONCAT('%', ?, '%')) " +
                    "                    or LOWER(a.last_name) like LOWER(CONCAT('%', ?, '%')) " +
                    "                    GROUP BY b.id) t";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, search);
            pstmt.setString(2, search);
            pstmt.setString(3, search);
            rs = pstmt.executeQuery();
            if (rs.next())
                count = rs.getLong("count");
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found book_user in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return count;
    }

    @Override
    public boolean addBookToRent(long bookId, long userId) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_RENT_BOOK_BY_USER);
            pstmt.setLong(1, bookId);
            pstmt.setLong(2, userId);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(SQL_UPDATE_REMOVE_BOOK_COUNT_BY_BOOK_ID);
            pstmt.setLong(1, bookId);
            pstmt.executeUpdate();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Cannot update book_user in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public RentBook changeStartDateRentBook(long id, long bookId) throws AppException {
        RentBook book = new RentBook();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;

        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_RENT_BOOK_START_DATE);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(SQL_UPDATE_REMOVE_BOOK_COUNT_BY_BOOK_ID);
            pstmt.setLong(1, bookId);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(SQL_FIND_REND_BOOK_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            BookLoad mapper = new BookLoad();
            if (rs.next()) {
                book = mapper.loadRentBook(con, rs);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't update start date by id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return book;
    }

    @Override
    public RentBook changePayStatusRentBook(long id) throws AppException {
        RentBook book = new RentBook();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_RENT_BOOK_STATUS_PAY);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(SQL_FIND_REND_BOOK_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            BookLoad mapper = new BookLoad();
            if (rs.next()) {
                book = mapper.loadRentBook(con, rs);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't update pay status by id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return book;
    }

    @Override
    public RentBook findRendBookByRentId(long id) throws AppException {
        RentBook book = new RentBook();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_REND_BOOK_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            BookLoad mapper = new BookLoad();
            if (rs.next()) {
                book = mapper.loadRentBook(con, rs);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't find rent book by id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return book;
    }

    @Override
    public boolean deleteRentBook(long id) throws AppException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        boolean result;
        try {
            con = dbManager.getConnection();
            String query = "select * from book_user where id = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            long bookId = 0;
            if (rs.next()) bookId = rs.getLong(Fields.BU__BOOK);
            pstmt = con.prepareStatement(SQL_UPDATE_ADD_BOOK_COUNT_BY_BOOK_ID);
            pstmt.setLong(1, bookId);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(SQL_DELETE_REND_BOOK_BY_ID);
            pstmt.setLong(1, id);
            pstmt.execute();
            rs.close();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            throw new AppException("Can't find rent book by id", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public List<CountBook> findBooksWithSearch(PageSettings pageSettings, String search) throws AppException {
        List<CountBook> books = new ArrayList<>();
        Book book = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            String orderColumn;
            switch (pageSettings.getSort()) {
                case "isbn":
                    orderColumn = "isbn";
                    break;
                case "publisher":
                    orderColumn = "publisher";
                    break;
                case "author":
                    orderColumn = "author";
                    break;
                default:
                    orderColumn = "name";
                    break;
            }
            String query =
                    "SELECT b.id as id," +
                            "GROUP_CONCAT(a.first_name, ' ', a.last_name SEPARATOR ', ') as author, " +
                            "b.isbn as isbn, " +
                            "b.name as name, " +
                            "b.year as year, " +
                            "b.image as image, " +
                            "b.publisher_id as publisher_id, " +
                            "p.name as publisher, " +
                            "cb.count as count, " +
                            "cb.id as cb_id " +
                            "FROM books b " +
                            "inner join books_authors ba on ba.book_id = b.id " +
                            "inner join authors a on ba.author_id=a.id " +
                            "inner join publishers p on b.publisher_id=p.id " +
                            "inner join current_books cb on b.id = cb.book_id " +
                            "where LOWER(b.name) like LOWER(CONCAT('%', ?, '%')) " +
                            "or LOWER(a.first_name) like LOWER(CONCAT('%', ?, '%')) " +
                            "or LOWER(a.last_name) like LOWER(CONCAT('%', ?, '%')) " +
                            "GROUP BY b.id order by " + orderColumn + " limit ?,?;";
            pstmt = con.prepareStatement(query);
            long startIndex = (pageSettings.getPage() - 1) * pageSettings.getSize();
            pstmt.setString(1, search);
            pstmt.setString(2, search);
            pstmt.setString(3, search);
            pstmt.setLong(4, startIndex);
            pstmt.setLong(5, pageSettings.getSize());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BookLoad mapper = new BookLoad();
                book = mapper.loadRow(rs);
                mapper.loadFields(con, book, rs);
                CountBook cb = new CountBook();
                cb.setId(rs.getLong("cb_id"));
                cb.setBook(book);
                cb.setCount(rs.getInt("count"));
                books.add(cb);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found book in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return books;
    }

    @Override
    public boolean addBookToRentByUser(long bookId, long userId) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_RENT_BOOK_BY_USER_FROM_USER);
            pstmt.setLong(1, bookId);
            pstmt.setLong(2, userId);
            pstmt.executeUpdate();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Cannot update book_user in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public boolean updateImageBookById(long bookId, String image) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_BOOK_IMAGE);
            pstmt.setString(1, image);
            pstmt.setLong(2, bookId);
            pstmt.executeUpdate();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Cannot update book_user in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    @Override
    public int countFineByUser(String number, long id) throws AppException {
        int count = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_COUNT_FINE_USER);
            pstmt.setString(1, number);
            pstmt.setLong(2, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                count = rs.getInt("count");
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found books in DB", ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return count;
    }

    private class BookLoad implements LoadEntity<Book> {
        /**
         * Load object from ResultSet
         *
         * @param rs ResultSet
         * @return Publisher object
         * @throws AppException
         */
        @Override
        public Book loadRow(ResultSet rs) throws AppException {
            try {
                Book book = new Book();
                book.setId(rs.getLong(Fields.ENTITY__ID));
                book.setIsbn(rs.getString(Fields.BOOK__ISBN));
                book.setName(rs.getString(Fields.BOOK__NAME));
                book.setYear(Year.parse(rs.getString(Fields.BOOK__YEAR)));
                book.setImage(rs.getString(Fields.BOOK__IMAGE));

                return book;
            } catch (Exception e) {
                throw new AppException("Can't read data from DB, table: Publishers", e);
            }
        }

        public void loadFields(Connection con, Book book, ResultSet rs) throws AppException {
            Publisher publisher = new Publisher();
            PreparedStatement pstmt = null;
            ResultSet resultSet = null;
            try {
                pstmt = con.prepareStatement("SELECT * FROM publishers WHERE id=?");
                pstmt.setLong(1, rs.getLong(Fields.BOOK__PUBLISHER));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    publisher.setId(resultSet.getLong(Fields.ENTITY__ID));
                    publisher.setName(resultSet.getString(Fields.PUBLISHER__NAME));
                    publisher.setAddress(resultSet.getString(Fields.PUBLISHER__ADDRESS));
                    publisher.setPhone(resultSet.getString(Fields.PUBLISHER__PHONE));
                    publisher.setCity(resultSet.getString(Fields.PUBLISHER__CITY));
                }
                resultSet.close();
                pstmt.close();
                book.setPublisher(publisher);
                book.setAuthors(findAuthors(con, book.getId()));
                book.setGenres(findGenres(con, book.getId()));
            } catch (SQLException ex) {
                throw new AppException("Can't find publisher by id", ex);
            }
        }

        public Set<Author> findAuthors(Connection con, long id) throws AppException {
            Set<Author> authors = new HashSet<>();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = con.prepareStatement("SELECT a.id, " +
                        "a.first_name, " +
                        "a.last_name " +
                        "FROM books_authors ba inner join authors a on ba.author_id=a.id " +
                        "WHERE ba.book_id=?;");
                pstmt.setLong(1, id);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Author author = new Author();
                    author.setId(rs.getLong(Fields.ENTITY__ID));
                    author.setFirstName(rs.getString(Fields.AUTHOR__FIRST_NAME));
                    author.setLastName(rs.getString(Fields.AUTHOR__LAST_NAME));
                    authors.add(author);
                }
                rs.close();
                pstmt.close();
            } catch (SQLException ex) {
                throw new AppException("Can't find authors by book id", ex);
            }
            return authors;
        }

        public Set<Genre> findGenres(Connection con, long id) throws AppException {
            Set<Genre> genres = new HashSet<>();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = con.prepareStatement("SELECT g.id, " +
                        "g.name " +
                        "FROM book_genre bg inner join genres g on bg.genre_id=g.id " +
                        "WHERE bg.book_id=?;");
                pstmt.setLong(1, id);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Genre genre = new Genre();
                    genre.setId(rs.getLong(Fields.ENTITY__ID));
                    genre.setName(rs.getString(Fields.GENRE__NAME));
                    genres.add(genre);
                }
                rs.close();
                pstmt.close();
            } catch (SQLException ex) {
                throw new AppException("Can't find genres by book id", ex);
            }
            return genres;
        }

        public RentBook loadRentBook(Connection con, ResultSet rsRentBook) throws AppException {
            RentBook rentBook = new RentBook();
            PreparedStatement pstmt = null;
            ResultSet rsBook = null;
            ResultSet rsUser = null;
            ResultSet rsRole = null;
            try {
                rentBook.setId(rsRentBook.getLong(Fields.ENTITY__ID));
                pstmt = con.prepareStatement("SELECT * FROM books WHERE id=?");
                pstmt.setLong(1, rsRentBook.getLong(Fields.BU__BOOK));
                rsBook = pstmt.executeQuery();
                Book book = null;
                if (rsBook.next()) {
                    book = loadRow(rsBook);
                    loadFields(con, book, rsBook);
                }
                rsBook.close();
                rentBook.setBook(book);
                pstmt = con.prepareStatement("SELECT * FROM users WHERE id=?");
                pstmt.setLong(1, rsRentBook.getLong(Fields.BU__USER));
                rsUser = pstmt.executeQuery();
                User user = new User();
                if (rsUser.next()) {
                    user.setId(rsUser.getLong(Fields.ENTITY__ID));
                    user.setNumber(rsUser.getString(Fields.USER__NUMBER));
                    user.setPassword(rsUser.getString(Fields.USER__PASSWORD));
                    user.setFirstName(rsUser.getString(Fields.USER__FIRST_NAME));
                    user.setLastName(rsUser.getString(Fields.USER__LAST_NAME));
                    user.setDateOfBirth(rsUser.getDate(Fields.USER__DATE_OF_BIRTH).toLocalDate());
                    user.setPhone(rsUser.getString(Fields.USER__PHONE));
                    user.setStatus(rsUser.getString(Fields.USER__STATUS));
                    user.setPhoto(rsUser.getString(Fields.USER__PHOTO));
                }
                pstmt = con.prepareStatement("SELECT * FROM roles WHERE id=?");
                pstmt.setLong(1, rsUser.getLong(Fields.USER__ROLE));
                rsRole = pstmt.executeQuery();
                Role role = new Role();
                if (rsRole.next()) {
                    role.setId(rsRole.getLong(Fields.ENTITY__ID));
                    role.setName(rsRole.getString(Fields.ROLE__NAME));
                    role.setStatus(rsRole.getString(Fields.ROLE__STATUS));
                }
                rsRole.close();
                user.setRole(role);
                rsUser.close();
                rentBook.setUser(user);
                LocalDate date;
                if (rsRentBook.getDate(Fields.BU__START_DATE) == null) {
                    date = LocalDate.MIN;
                } else {
                    date = rsRentBook.getDate(Fields.BU__START_DATE).toLocalDate();
                }
                rentBook.setStartDate(date);
                if (rsRentBook.getDate(Fields.BU__END_DATE) == null) {
                    date = LocalDate.MIN;
                } else {
                    date = rsRentBook.getDate(Fields.BU__END_DATE).toLocalDate();
                }
                rentBook.setEndDate(date);
                rentBook.setStatus(rsRentBook.getString(Fields.BU__STATUS));
                rentBook.setStatusPay(rsRentBook.getString(Fields.BU__STATUS_PAY));
                rsBook.close();
                pstmt.close();
                return rentBook;
            } catch (Exception e) {
                throw new AppException("Can't read data from DB, table: book_user", e);
            }
        }
    }
}
