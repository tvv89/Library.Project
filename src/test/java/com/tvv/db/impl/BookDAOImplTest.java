package com.tvv.db.impl;

import com.tvv.db.DBManager;
import com.tvv.db.dao.BookDAO;
import com.tvv.db.entity.*;
import com.tvv.db.util.PageSettings;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookDAOImplTest {
    private Book assertBook;
    private User assertUser;
    private RentBook assertRentBook;
    private static final String SQL__FIND_AUTHOR_BY_NAME =
            "select * from authors where first_name = ? and last_name = ?";
    private static final String SQL_INSERT_AUTHOR =
            "INSERT INTO authors (id, first_name, last_name) VALUES (0, ?, ?);";

    private static final String SQL__FIND_GENRE_BY_NAME =
            "select * from genres where name = ?";
    private static final String SQL_INSERT_GENRE =
            "INSERT INTO genres (id, name) VALUES (0, ?);";

    private static final String SQL__FIND_PUBLISHER_BY_NAME =
            "select * from publishers where name = ?";

    public static final String SQL_INSERT_PUBLISHER =
            "INSERT INTO publishers (id, name, address, phone, city) " +
                    "VALUES (0, ?, ?, ?, ?);";
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

    private static final String SQL_UPDATE_BOOK_BY_ID =
            "UPDATE books SET isbn = ?, name = ?, year = ? WHERE id = ?";
    private static final String SQL_UPDATE_BOOK_COUNT_BY_BOOK_ID =
            "UPDATE current_books SET count = ? WHERE book_id = ?";

    private static final String SQL_DELETE_BOOK_BY_ID =
            "DELETE FROM books WHERE id= ?";

    private static final String SQL_FIND_ALL_BOOKS_PAGINATION_ISBN =
            "SELECT *, cb.count as count, cb.id as cb_id FROM books b " +
                    "inner join current_books cb on b.id = cb.book_id order by isbn limit ?,?;";

    private static final String SQL_COUNT_BOOK =
            "SELECT count(*) as count FROM books";

    private static final String SQL_COUNT_BOOK_BY_USER_ID =
            "SELECT count(*) as count FROM book_user where user_id = ?";

    private static final String SQL_INSERT_RENT_BOOK_BY_USER =
            "INSERT INTO book_user (id, book_id, user_id, start_date, end_date, status_pay) " +
                    "VALUES (0, ?, ?, curdate(), DATE_ADD(curdate(), INTERVAL ? DAY), '')";

    private static final String SQL_UPDATE_REMOVE_BOOK_COUNT_BY_BOOK_ID =
            "UPDATE current_books SET count = count - 1 WHERE book_id = ?";

    private static final String SQL_UPDATE_RENT_BOOK_START_DATE =
            "UPDATE book_user SET start_date = curdate(), " +
                    "end_date = DATE_ADD(curdate(), INTERVAL ? DAY)" +
                    "WHERE id = ?";

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

    private static final String SQL_UPDATE_RENT_BOOK_STATUS_PAY =
            "UPDATE book_user SET status_pay = 'paid' " +
                    "WHERE id = ?";

    private static final String SQL_UPDATE_ADD_BOOK_COUNT_BY_BOOK_ID =
            "UPDATE current_books SET count = count + 1 WHERE book_id = ?";

    private static final String SQL_DELETE_REND_BOOK_BY_ID =
            "DELETE FROM book_user WHERE id = ?";

    private static final String SQL_INSERT_RENT_BOOK_BY_USER_FROM_USER =
            "INSERT INTO book_user (id, book_id, user_id, start_date, end_date, status_pay) " +
                    "VALUES (0, ?, ?, null, null, '')";

    private static final String SQL_UPDATE_BOOK_IMAGE =
            "UPDATE books SET image = ? " +
                    "WHERE id = ?";

    private static final String SQL_COUNT_FINE_USER =
            "select count(*) as count from (select " +
                    " IF(bu.start_date>date(0),if(bu.end_date<curdate() and bu.status_pay<>'paid', 'need pay', 'reading'),'booked') as stat " +
                    "from book_user bu " +
                    "inner join users u on bu.user_id=u.id " +
                    "where u.number = ? or u.id = ?) as table1 " +
                    "where table1.stat like '%need pay%'";


    @BeforeEach
    private void init() {
        assertBook = new Book();
        assertBook.setId(1L);
        assertBook.setIsbn("1234567890123");
        assertBook.setName("The best book");
        assertBook.setYear(Year.parse("2022"));
        assertBook.setImage("1234567890123.jpg");

        Set<Author> authors = new HashSet<>();
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("AuthorFirstName");
        author.setLastName("AuthorLastName");
        authors.add(author);
        assertBook.setAuthors(authors);

        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Publisher books");
        publisher.setCity("");
        publisher.setAddress("");
        publisher.setPhone("");
        assertBook.setPublisher(publisher);

        Set<Genre> genres = new HashSet<>();
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("BookGenre");
        genres.add(genre);
        assertBook.setGenres(genres);

        assertUser = new User();
        assertUser.setId(1L);
        assertUser.setNumber("89000000");
        assertUser.setPassword("ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
        assertUser.setFirstName("UserFirstName");
        assertUser.setLastName("UserLastName");
        assertUser.setDateOfBirth(LocalDate.parse("2000-01-01"));
        assertUser.setPhone("+380638900000");
        assertUser.setPhoto("89000000.jpg");
        assertUser.setStatus("enabled");
        assertUser.setLocale("en");
        Role role = new Role();
        role.setId(3);
        role.setName("UserRole");
        role.setStatus("enabled");
        assertUser.setRole(role);

        assertRentBook = new RentBook();
        assertRentBook.setId(1);
        assertRentBook.setBook(assertBook);
        assertRentBook.setUser(assertUser);
        assertRentBook.setStartDate(LocalDate.parse("2022-07-01"));
        assertRentBook.setEndDate(LocalDate.parse("2022-08-01"));
        assertRentBook.setStatus("reading");
        assertRentBook.setStatusPay("");

    }

    @Test
    void testCreateNewBookCorrect() throws SQLException, AppException {

        ResultSet rsFindAuthor = Mockito.mock(ResultSet.class);
        when(rsFindAuthor.next())
                .thenReturn(false)
                .thenReturn(true);
        when(rsFindAuthor.getLong(1)).thenReturn(1L);

        ResultSet rsFindGenre = Mockito.mock(ResultSet.class);
        when(rsFindGenre.next())
                .thenReturn(false)
                .thenReturn(true);
        when(rsFindGenre.getLong(1)).thenReturn(1L);

        ResultSet rsFindPublisher = Mockito.mock(ResultSet.class);
        when(rsFindPublisher.next())
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(true);
        when(rsFindPublisher.getLong(1))
                .thenReturn(1L)
                .thenReturn(1L);

        PreparedStatement pstmt = Mockito.mock(PreparedStatement.class);

        when(pstmt.getGeneratedKeys())
                .thenReturn(rsFindAuthor)
                .thenReturn(rsFindGenre)
                .thenReturn(rsFindPublisher)
                .thenReturn(rsFindPublisher);

        when(pstmt.executeQuery())
                .thenReturn(rsFindAuthor)
                .thenReturn(rsFindGenre)
                .thenReturn(rsFindPublisher);
        when(pstmt.executeUpdate())
                .thenReturn(1)
                .thenReturn(1)
                .thenReturn(1)
                //INSERT BOOK
                .thenReturn(1)
                //BOOK-AUTHORS
                .thenReturn(1)
                //BOOK-GENRE
                .thenReturn(1)
                //BOOK-COUNT
                .thenReturn(1);

        Connection con = Mockito.mock(Connection.class);
        when(con.prepareStatement(SQL__FIND_AUTHOR_BY_NAME))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__FIND_GENRE_BY_NAME))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_INSERT_GENRE, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__FIND_PUBLISHER_BY_NAME))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_INSERT_PUBLISHER, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__INSERT_BOOK, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__INSERT_BOOK_AUTHOR))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__INSERT_BOOK_GENRE))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__INSERT_BOOK_COUNT))
                .thenReturn(pstmt);


        DBManager instance = Mockito.mock(DBManager.class);
        when(instance.getConnection())
                .thenReturn(con);


        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        boolean result = bookDAO.create(assertBook, 1);
        assertTrue(result);
        verify(pstmt, times(7)).executeUpdate();

    }

    @Test
    void testCreateBookWithException() throws SQLException {

        ResultSet rsFindAuthor = Mockito.mock(ResultSet.class);
        when(rsFindAuthor.next())
                .thenReturn(false)
                .thenReturn(true);
        when(rsFindAuthor.getLong(1)).thenReturn(1L);

        ResultSet rsFindGenre = Mockito.mock(ResultSet.class);
        when(rsFindGenre.next())
                .thenReturn(false)
                .thenReturn(true);
        when(rsFindGenre.getLong(1)).thenReturn(1L);

        ResultSet rsFindPublisher = Mockito.mock(ResultSet.class);
        when(rsFindPublisher.next())
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(true);
        when(rsFindPublisher.getLong(1))
                .thenReturn(1L)
                .thenReturn(1L);

        PreparedStatement pstmt = Mockito.mock(PreparedStatement.class);

        when(pstmt.getGeneratedKeys())
                .thenReturn(rsFindAuthor)
                .thenReturn(rsFindGenre)
                .thenReturn(rsFindPublisher)
                .thenReturn(rsFindPublisher);

        when(pstmt.executeQuery())
                .thenReturn(rsFindAuthor)
                .thenReturn(rsFindGenre)
                .thenReturn(rsFindPublisher);
        when(pstmt.executeUpdate())
                .thenReturn(1)
                .thenReturn(1)
                .thenReturn(1)
                //INSERT BOOK
                .thenReturn(1)
                //BOOK-AUTHORS
                .thenReturn(1)
                //BOOK-GENRE
                .thenReturn(1)
                //BOOK-COUNT
                .thenThrow(new SQLException());

        Connection con = Mockito.mock(Connection.class);
        when(con.prepareStatement(SQL__FIND_AUTHOR_BY_NAME))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__FIND_GENRE_BY_NAME))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_INSERT_GENRE, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__FIND_PUBLISHER_BY_NAME))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_INSERT_PUBLISHER, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__INSERT_BOOK, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__INSERT_BOOK_AUTHOR))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__INSERT_BOOK_GENRE))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL__INSERT_BOOK_COUNT))
                .thenReturn(pstmt);


        DBManager instance = Mockito.mock(DBManager.class);
        when(instance.getConnection())
                .thenReturn(con);


        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        assertThrows(AppException.class, () -> bookDAO.create(assertBook, 1));
    }

    @Test
    void testCreateBookNull() {
        DBManager instance = Mockito.mock(DBManager.class);
        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        assertThrows(AppException.class, () -> bookDAO.create(null, 1));
    }

    private void mockLoadRow(Connection con, ResultSet rs) throws SQLException {
        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("isbn")).thenReturn("1234567890123");
        when(rs.getString("name")).thenReturn("The best book");
        when(rs.getString("year")).thenReturn("2022");
        when(rs.getString("image")).thenReturn("1234567890123.jpg");
        when(rs.getLong("publisher_id")).thenReturn(1L);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);
        when(con.prepareStatement(SQL__FIND_BOOK_BY_ID))
                .thenReturn(pstmt);
        //loadFields from Mock Publisher
        ResultSet rsPublisher = mock(ResultSet.class);
        when(rsPublisher.next())
                .thenReturn(true);
        when(rsPublisher.getLong("id")).thenReturn(1L);
        when(rsPublisher.getString("name")).thenReturn("Publisher books");
        when(rsPublisher.getString("address")).thenReturn("");
        when(rsPublisher.getString("phone")).thenReturn("");
        when(rsPublisher.getString("city")).thenReturn("");
        PreparedStatement pstmtPublisher = mock(PreparedStatement.class);
        when(pstmtPublisher.executeQuery())
                .thenReturn(rsPublisher);
        when(con.prepareStatement("SELECT * FROM publishers WHERE id=?"))
                .thenReturn(pstmtPublisher);
        //loadFields from Mock Authors
        ResultSet rsAuthors = mock(ResultSet.class);
        when(rsAuthors.next())
                .thenReturn(true)
                .thenReturn(false);
        when(rsAuthors.getLong("id")).thenReturn(1L);
        when(rsAuthors.getString("first_name")).thenReturn("AuthorFirstName");
        when(rsAuthors.getString("last_name")).thenReturn("AuthorLastName");
        PreparedStatement pstmtAuthor = mock(PreparedStatement.class);
        when(pstmtAuthor.executeQuery())
                .thenReturn(rsAuthors);
        when(con.prepareStatement("SELECT a.id, " +
                "a.first_name, " +
                "a.last_name " +
                "FROM books_authors ba inner join authors a on ba.author_id=a.id " +
                "WHERE ba.book_id=?;"))
                .thenReturn(pstmtAuthor);

        //loadFields from Mock Genres
        ResultSet rsGenres = mock(ResultSet.class);
        when(rsGenres.next())
                .thenReturn(true)
                .thenReturn(false);
        when(rsGenres.getLong("id")).thenReturn(1L);
        when(rsGenres.getString("name")).thenReturn("BookGenre");
        PreparedStatement pstmtGenres = mock(PreparedStatement.class);
        when(pstmtGenres.executeQuery())
                .thenReturn(rsGenres);
        when(con.prepareStatement("SELECT g.id, " +
                "g.name " +
                "FROM book_genre bg inner join genres g on bg.genre_id=g.id " +
                "WHERE bg.book_id=?;"))
                .thenReturn(pstmtGenres);
    }

    @Test
    void testFindByIdCorrect() throws SQLException, AppException {

        ResultSet rs = mock(ResultSet.class);
        when(rs.next())
                .thenReturn(true);
        Connection con = mock(Connection.class);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        mockLoadRow(con, rs);
        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        Book book = bookDAO.findById(1L);
        assertEquals(book.getPublisher().getName(),
                assertBook.getPublisher().getName());
    }

    @Test
    void testFindByIdWithCount() throws SQLException, AppException {
        CountBook assertCountBook = new CountBook();
        assertCountBook.setId(1L);
        assertCountBook.setBook(assertBook);
        assertCountBook.setCount(10);

        ResultSet rs = mock(ResultSet.class);
        when(rs.next())
                .thenReturn(true);
        when(rs.getLong("count")).thenReturn(10L);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL__FIND_BOOK_BY_ID_WITH_COUNT))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        mockLoadRow(con, rs);
        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        CountBook book = bookDAO.findByIdWithCount(1L);
        assertEquals(book.getBook().getName(),
                assertCountBook.getBook().getName());
    }

    @Test
    void testUpdateSuccess() throws SQLException, AppException {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(1)
                .thenReturn(1);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_UPDATE_BOOK_BY_ID))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_UPDATE_BOOK_COUNT_BY_BOOK_ID))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        boolean result = bookDAO.update(assertBook, 10);
        verify(pstmt, times(2)).executeUpdate();
        assertTrue(result);
    }

    @Test
    void testDeleteSuccess() throws SQLException, AppException {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(1);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_DELETE_BOOK_BY_ID))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        boolean result = bookDAO.delete(assertBook);
        verify(pstmt, times(1)).executeUpdate();
        assertTrue(result);
    }

    @Test
    void testFindAllBooks() throws SQLException, AppException {
        List<CountBook> assertList = new ArrayList<>();
        CountBook countBook = new CountBook();
        countBook.setId(1L);
        countBook.setBook(assertBook);
        countBook.setCount(2);
        assertList.add(countBook);
        PageSettings pageSettings = new PageSettings();
        pageSettings.setSort("isbn");
        pageSettings.setPage(1);
        pageSettings.setSize(5);
        ResultSet rs = mock(ResultSet.class);
        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getLong("cb_id")).thenReturn(1L);
        when(rs.getInt("count")).thenReturn(2);
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_FIND_ALL_BOOKS_PAGINATION_ISBN))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        mockLoadRow(con, rs);
        //when(rs.next()).thenReturn(false);
        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        List<CountBook> result = bookDAO.findAllBooks(pageSettings);
        assertEquals(assertList.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(assertList.get(i).getId(), result.get(i).getId());
            assertEquals(assertList.get(i).getBook().getName(), result.get(i).getBook().getName());
            assertEquals(assertList.get(i).getCount(), result.get(i).getCount());
        }
    }

    private void mockLoadRentBook(Connection con, ResultSet rs) throws SQLException {
        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getLong("book_id")).thenReturn(1L);
        when(rs.getLong("user_id")).thenReturn(1L);
        when(rs.getDate("start_date")).thenReturn(Date.valueOf("2022-07-01"));
        when(rs.getDate("end_date")).thenReturn(Date.valueOf("2022-08-01"));
        when(rs.getString("status")).thenReturn("reading");
        when(rs.getString("status_pay")).thenReturn("");

        ResultSet rsBook = mock(ResultSet.class);
        mockLoadRow(con, rsBook);

        PreparedStatement pstmtBooks = mock(PreparedStatement.class);
        when(pstmtBooks.executeQuery())
                .thenReturn(rsBook);
        when(con.prepareStatement("SELECT * FROM books WHERE id=?"))
                .thenReturn(pstmtBooks);
        //loadFields from Mock User
        ResultSet rsUser = mock(ResultSet.class);
        when(rsUser.next())
                .thenReturn(true);
        when(rsUser.getLong("id")).thenReturn(1L);
        when(rsUser.getString("number")).thenReturn("89000000");
        when(rsUser.getString("password")).thenReturn("ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
        when(rsUser.getString("first_name")).thenReturn("UserFirstName");
        when(rsUser.getString("last_name")).thenReturn("UserLastName");
        when(rsUser.getDate("date_of_birth")).thenReturn(Date.valueOf("2000-01-01"));
        when(rsUser.getString("phone")).thenReturn("+380638900000");
        when(rsUser.getString("status")).thenReturn("enabled");
        when(rsUser.getString("photo")).thenReturn("89000000.jpg");
        when(rsUser.getLong("role_id")).thenReturn(1L);


        PreparedStatement pstmtUser = mock(PreparedStatement.class);
        when(pstmtUser.executeQuery())
                .thenReturn(rsUser);
        when(con.prepareStatement("SELECT * FROM users WHERE id=?"))
                .thenReturn(pstmtUser);
        //loadFields from Mock Role
        ResultSet rsRole = mock(ResultSet.class);
        when(rsRole.next())
                .thenReturn(true);
        when(rsRole.getLong("id")).thenReturn(1L);
        when(rsRole.getString("name")).thenReturn("UserRole");
        when(rsRole.getString("status")).thenReturn("enabled");
        PreparedStatement pstmtRole = mock(PreparedStatement.class);
        when(pstmtRole.executeQuery())
                .thenReturn(rsRole);
        when(con.prepareStatement("SELECT * FROM roles WHERE id=?"))
                .thenReturn(pstmtRole);

    }

    @Test
    void findAllUserBooks() throws SQLException, AppException {
        List<RentBook> assertRentBookList = new ArrayList<>();
        assertRentBookList.add(assertRentBook);

        PageSettings pageSettings = new PageSettings();
        pageSettings.setSort("isbn");
        pageSettings.setPage(1);
        pageSettings.setSize(5);

        ResultSet rs = mock(ResultSet.class);
        //when(rs.next()).thenReturn(true);
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery()).thenReturn(rs);

        Connection con = mock(Connection.class);
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
                "where bu.user_id=? " +
                "order by b.isbn limit ?,? ";
        when(con.prepareStatement(query))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        mockLoadRentBook(con, rs);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        List<RentBook> result = bookDAO.findAllUserBooks(1, pageSettings);
        assertEquals(assertRentBookList.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(assertRentBookList.get(i).getId(), result.get(i).getId());
            assertEquals(assertRentBookList.get(i).getBook().getName(), result.get(i).getBook().getName());
            assertEquals(assertRentBookList.get(i).getStartDate(), result.get(i).getStartDate());
        }

    }

    @Test
    void testBookCount() throws SQLException, AppException {
        long assertCount = 5;
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("count")).thenReturn(5L);
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery()).thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_COUNT_BOOK))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        long result = bookDAO.bookCount();
        assertEquals(assertCount, result);
    }

    @Test
    void testBookCountUserId() throws SQLException, AppException {
        long assertCount = 5;
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("count")).thenReturn(5L);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery()).thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_COUNT_BOOK_BY_USER_ID))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        long result = bookDAO.bookCount(1);
        assertEquals(assertCount, result);
    }

    @Test
    void testBookCountWithSearch() throws SQLException, AppException {
        long assertCount = 5;
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("count")).thenReturn(5L);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery()).thenReturn(rs);

        Connection con = mock(Connection.class);
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
        when(con.prepareStatement(query))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        long result = bookDAO.bookCount("book");
        assertEquals(assertCount, result);
    }

    @Test
    void testAddBookToRentCorrect() throws SQLException, AppException {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(1)
                .thenReturn(1);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_INSERT_RENT_BOOK_BY_USER))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_UPDATE_REMOVE_BOOK_COUNT_BY_BOOK_ID))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        boolean result = bookDAO.addBookToRent(1, 1, 30);
        verify(pstmt, times(2)).executeUpdate();
        assertTrue(result);
    }

    @Test
    void testChangeStartDateRentBookCorrect() throws SQLException, AppException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(1)
                .thenReturn(1);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_UPDATE_RENT_BOOK_START_DATE))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_UPDATE_REMOVE_BOOK_COUNT_BY_BOOK_ID))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_FIND_REND_BOOK_BY_ID))
                .thenReturn(pstmt);
        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        mockLoadRentBook(con, rs);
        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        RentBook rentBook = bookDAO.changeStartDateRentBook(1, 1,30);
        assertEquals(rentBook.getStartDate(), assertRentBook.getStartDate());
    }

    @Test
    void testChangePayStatusRentBook() throws SQLException, AppException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(1);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_UPDATE_RENT_BOOK_STATUS_PAY))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_FIND_REND_BOOK_BY_ID))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        mockLoadRentBook(con, rs);
        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        RentBook rentBook = bookDAO.changePayStatusRentBook(1);
        assertEquals(rentBook.getStatusPay(), assertRentBook.getStatusPay());
    }

    @Test
    void testFindRendBookByRentId() throws SQLException, AppException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_FIND_REND_BOOK_BY_ID))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        mockLoadRentBook(con, rs);
        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        RentBook rentBook = bookDAO.findRendBookByRentId(1);
        assertEquals(rentBook.getBook().getName(), assertRentBook.getBook().getName());
    }

    @Test
    void testDeleteRentBook() throws SQLException, AppException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("book_id")).thenReturn(1L);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);
        when(pstmt.executeUpdate())
                .thenReturn(1);
        when(pstmt.execute()).thenReturn(true);

        Connection con = mock(Connection.class);
        when(con.prepareStatement("select * from book_user where id = ?"))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_UPDATE_ADD_BOOK_COUNT_BY_BOOK_ID))
                .thenReturn(pstmt);
        when(con.prepareStatement(SQL_DELETE_REND_BOOK_BY_ID))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        boolean result = bookDAO.deleteRentBook(1);
        verify(pstmt, times(1)).execute();
        assertTrue(result);
    }

    @Test
    void testFindBooksWithSearch() throws SQLException, AppException {
        List<CountBook> assertList = new ArrayList<>();
        CountBook countBook = new CountBook();
        countBook.setId(1L);
        countBook.setBook(assertBook);
        countBook.setCount(2);
        assertList.add(countBook);
        PageSettings pageSettings = new PageSettings();
        pageSettings.setSort("isbn");
        pageSettings.setPage(1);
        pageSettings.setSize(5);
        ResultSet rs = mock(ResultSet.class);
        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getLong("cb_id")).thenReturn(1L);
        when(rs.getInt("count")).thenReturn(2);
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

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
                        "GROUP BY b.id order by isbn limit ?,?;";
        Connection con = mock(Connection.class);
        when(con.prepareStatement(query))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        mockLoadRow(con, rs);
        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        List<CountBook> result = bookDAO.findBooksWithSearch(pageSettings, "book");
        assertEquals(assertList.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(assertList.get(i).getId(), result.get(i).getId());
            assertEquals(assertList.get(i).getBook().getName(), result.get(i).getBook().getName());
            assertEquals(assertList.get(i).getCount(), result.get(i).getCount());
        }
    }

    @Test
    void testAddBookToRentByUser() throws SQLException, AppException {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate()).thenReturn(1);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_INSERT_RENT_BOOK_BY_USER_FROM_USER))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        boolean result = bookDAO.addBookToRentByUser(1, 1);
        assertTrue(result);
    }

    @Test
    void testUpdateImageBookById() throws SQLException, AppException {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate()).thenReturn(1);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_UPDATE_BOOK_IMAGE))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        boolean result = bookDAO.updateImageBookById(1, "book.jpg");
        assertTrue(result);
    }

    @Test
    void testCountFineByUser() throws SQLException, AppException {
        long assertCount = 5;
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("count")).thenReturn(5);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery()).thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(SQL_COUNT_FINE_USER))
                .thenReturn(pstmt);

        DBManager instance = mock(DBManager.class);
        when(instance.getConnection()).thenReturn(con);

        BookDAO bookDAO = new BookDAOImpl();
        bookDAO.init(instance);
        long result = bookDAO.countFineByUser("89000000", 1);
        assertEquals(assertCount, result);
    }
}