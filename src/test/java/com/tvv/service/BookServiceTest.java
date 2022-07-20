package com.tvv.service;

import com.google.gson.JsonObject;
import com.tvv.db.dao.BookDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.*;
import com.tvv.db.util.PageSettings;
import com.tvv.service.dto.BookDTO;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {
    private Book assertBook;
    private User assertUser;
    private RentBook assertRentBook;

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
        publisher.setCity(null);
        publisher.setAddress(null);
        publisher.setPhone(null);
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
    void testBookListPaginationCorrect() throws AppException {
        Map<String, Object> jsonParameters = new HashMap<>();
        jsonParameters.put("currentPage", 1);
        jsonParameters.put("items", 5);
        jsonParameters.put("sorting", "isbn");
        PageSettings pageSettings = new PageSettings();
        pageSettings.setSize(5);
        pageSettings.setPage(1);
        pageSettings.setSort("isbn");

        List<CountBook> assertBookList = new ArrayList<>();
        CountBook countBook = new CountBook();
        countBook.setId(1);
        countBook.setBook(assertBook);
        countBook.setCount(2);
        assertBookList.add(countBook);

        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.findAllBooks(pageSettings))
                .thenReturn(assertBookList);
        when(bookDAO.bookCount()).thenReturn(1L);
        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        JsonObject result = bookService.bookListPagination(jsonParameters);

        String assertSendData = "{\"status\":\"OK\",\"page\":1,\"pages\":1,\"list\":[{\"id\":1,\"isbn\"" +
                ":\"1234567890123\",\"author\":\"AuthorFirstName AuthorLastName\",\"name\":\"The best book\"" +
                ",\"publisher\":\"Publisher books\",\"year\":\"2022\",\"image\":\"1234567890123.jpg\",\"genre\"" +
                ":\"BookGenre\",\"count\":2}]}";
        assertEquals(result.toString(), assertSendData);
    }

    @Test
    void testBookListPaginationWithSearch() throws AppException {
        Map<String, Object> jsonParameters = new HashMap<>();
        jsonParameters.put("currentPage", 1);
        jsonParameters.put("items", 5);
        jsonParameters.put("sorting", "isbn");
        jsonParameters.put("searching", "book");
        PageSettings pageSettings = new PageSettings();
        pageSettings.setSize(5);
        pageSettings.setPage(1);
        pageSettings.setSort("isbn");

        List<CountBook> assertBookList = new ArrayList<>();
        CountBook countBook = new CountBook();
        countBook.setId(1);
        countBook.setBook(assertBook);
        countBook.setCount(2);
        assertBookList.add(countBook);

        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.findBooksWithSearch(pageSettings, "book"))
                .thenReturn(assertBookList);
        when(bookDAO.bookCount("book")).thenReturn(1L);
        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        JsonObject result = bookService.bookListPaginationWithSearch(jsonParameters);

        String assertSendData = "{\"status\":\"OK\",\"page\":1,\"pages\":1,\"list\":[{\"id\":1,\"isbn\"" +
                ":\"1234567890123\",\"author\":\"AuthorFirstName AuthorLastName\",\"name\":\"The best book\"" +
                ",\"publisher\":\"Publisher books\",\"year\":\"2022\",\"image\":\"1234567890123.jpg\",\"genre\"" +
                ":\"BookGenre\",\"count\":2}]}";
        assertEquals(result.toString(), assertSendData);
    }

    @Test
    void testBookRentListPagination() throws AppException {
        Map<String, Object> jsonParameters = new HashMap<>();
        jsonParameters.put("currentPage", 1);
        jsonParameters.put("items", 5);
        jsonParameters.put("sorting", "isbn");
        PageSettings pageSettings = new PageSettings();
        pageSettings.setSize(5);
        pageSettings.setPage(1);
        pageSettings.setSort("isbn");

        List<RentBook> assertBookList = new ArrayList<>();
        RentBook rentBook = new RentBook();
        rentBook.setId(1);
        rentBook.setBook(assertBook);
        rentBook.setUser(assertUser);
        rentBook.setStartDate(LocalDate.parse("2022-07-01"));
        rentBook.setEndDate(LocalDate.parse("2022-08-01"));
        rentBook.setStatus("reading");
        rentBook.setStatusPay("");
        assertBookList.add(rentBook);

        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.findAllUserBooks(1, pageSettings))
                .thenReturn(assertBookList);
        when(bookDAO.bookCount(1)).thenReturn(1L);
        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        JsonObject result = bookService.bookRentListPagination(jsonParameters, 1);
        String assertSendData = "{\"status\":\"OK\",\"page\":1,\"pages\":1," +
                "\"list\":[{\"id\":1,\"image\":\"1234567890123.jpg\",\"author\":" +
                "\"AuthorFirstName AuthorLastName\",\"name\":\"The best book\"," +
                "\"number\":\"89000000\",\"startDate\":\"2022-07-01\",\"endDate\":" +
                "\"2022-08-01\",\"status\":\"reading\",\"statusPay\":\"\"}]}";
        assertEquals(result.toString(), assertSendData);
    }

    @Test
    void testStartRentBook() throws AppException {
        RentBook rentBook = new RentBook();
        rentBook.setId(1);
        rentBook.setBook(assertBook);
        rentBook.setUser(assertUser);
        rentBook.setStartDate(LocalDate.parse("2022-07-01"));
        rentBook.setEndDate(LocalDate.parse("2022-08-01"));
        rentBook.setStatus("reading");
        rentBook.setStatusPay("");
        CountBook countBook = new CountBook();
        countBook.setId(1);
        countBook.setBook(assertBook);
        countBook.setCount(1);
        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.findRendBookByRentId(1))
                .thenReturn(rentBook);
        when(bookDAO.findByIdWithCount(1)).thenReturn(countBook);
        when(bookDAO.countFineByUser("",1))
                .thenReturn(0);
        when(bookDAO.changeStartDateRentBook(1, 1, 30))
                .thenReturn(rentBook);

        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        JsonObject result = bookService.startRentBook(1, 30);
        String assertSendData = "{\"status\":\"OK\",\"book\":{\"id\":1,\"image\":\"1234567890123.jpg\"," +
                "\"author\":\"AuthorFirstName AuthorLastName\",\"name\":\"The best book\",\"number\":\"89000000\"," +
                "\"startDate\":\"2022-07-01\",\"endDate\":\"2022-08-01\",\"status\":\"reading\",\"statusPay\":\"\"}," +
                "\"message\":\"Rent book was updated\"}";
        assertEquals(result.toString(), assertSendData);
    }

    @Test
    void testPayFineForBook() throws AppException {
        RentBook rentBook = new RentBook();
        rentBook.setId(1);
        rentBook.setBook(assertBook);
        rentBook.setUser(assertUser);
        rentBook.setStartDate(LocalDate.parse("2022-07-01"));
        rentBook.setEndDate(LocalDate.parse("2022-08-01"));
        rentBook.setStatus("reading");
        rentBook.setStatusPay("");
        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.changePayStatusRentBook(1))
                .thenReturn(rentBook);

        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        JsonObject result = bookService.payFineForBook(1);
        String assertSendData = "{\"status\":\"OK\",\"book\":{\"id\":1,\"image\":\"1234567890123.jpg\"," +
                "\"author\":\"AuthorFirstName AuthorLastName\",\"name\":\"The best book\",\"number\":\"89000000\"," +
                "\"startDate\":\"2022-07-01\",\"endDate\":\"2022-08-01\",\"status\":\"reading\",\"statusPay\":\"\"}" +
                ",\"message\":\"Fine was paid\"}";
        assertEquals(result.toString(), assertSendData);
    }

    @Test
    void testDeleteBookFromRent() throws AppException {
        RentBook rentBook = new RentBook();
        rentBook.setId(1);
        rentBook.setBook(assertBook);
        rentBook.setUser(assertUser);
        rentBook.setStartDate(LocalDate.parse("2022-07-01"));
        rentBook.setEndDate(LocalDate.parse("2022-08-01"));
        rentBook.setStatus("reading");
        rentBook.setStatusPay("");
        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.findRendBookByRentId(1))
                .thenReturn(rentBook);
        when(bookDAO.deleteRentBook(1))
                .thenReturn(true);

        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        JsonObject result = bookService.deleteBookFromRent(1);
        String assertSendData = "{\"status\":\"OK\",\"message\":\"Book was returned to library\"}";
        assertEquals(result.toString(), assertSendData);
    }

    @Test
    void testStartRentBookByUserNumber() throws AppException {
        CountBook countBook = new CountBook();
        countBook.setId(1);
        countBook.setBook(assertBook);
        countBook.setCount(1);
        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.findByIdWithCount(1)).thenReturn(countBook);
        when(bookDAO.countFineByUser("89000000",1))
                .thenReturn(0);
        when(bookDAO.addBookToRentByUser(1, 1))
                .thenReturn(true);

        UserDAO userDAO = mock(UserDAO.class);
        when(userDAO.findUserByNumber("89000000")).thenReturn(assertUser);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        JsonObject result = bookService.startRentBookByUserNumber(1, "89000000");
        String assertSendData = "{\"status\":\"OK\",\"message\":\"Booking was rented\"}";
        assertEquals(result.toString(), assertSendData);
    }

    @Test
    void testCreateBook() throws AppException {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);
        bookDTO.setIsbn("1234567890123");
        bookDTO.setName("The best book");
        bookDTO.setPublisher("Publisher books");
        bookDTO.setYear("2022");
        bookDTO.setAuthor("AuthorFirstName AuthorLastName");
        bookDTO.setGenre("BookGenre");
        bookDTO.setImage("1.jpg");
        bookDTO.setCount(1);

        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.create(assertBook, 1))
                .thenReturn(true);

        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        boolean result = bookService.createBook(bookDTO);
        assertTrue(result);

    }

    @Test
    void testUpdateBook() throws AppException {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);
        bookDTO.setIsbn("1234567890123");
        bookDTO.setName("The best book");
        bookDTO.setPublisher("Publisher books");
        bookDTO.setYear("2022");
        bookDTO.setAuthor("AuthorFirstName AuthorLastName");
        bookDTO.setGenre("BookGenre");
        bookDTO.setImage("1.jpg");
        bookDTO.setCount(1);

        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.update(assertBook, 1))
                .thenReturn(true);

        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        boolean result = bookService.updateBook(bookDTO);
        assertTrue(result);
    }

    @Test
    void testFindBookById() throws AppException {
        CountBook countBook = new CountBook();
        countBook.setId(1);
        countBook.setBook(assertBook);
        countBook.setCount(1);
        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.findByIdWithCount(1))
                .thenReturn(countBook);

        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        JsonObject result = bookService.findBookById(1);
        String assertString = "{\"status\":\"OK\",\"book\":{\"id\":1,\"isbn\":\"1234567890123\"," +
                "\"author\":\"AuthorFirstName AuthorLastName\",\"name\":\"The best book\"," +
                "\"publisher\":\"Publisher books\",\"year\":\"2022\",\"image\":\"1234567890123.jpg\"," +
                "\"genre\":\"BookGenre\",\"count\":1}}";
        assertEquals(result.toString(), assertString);
    }

    @Test
    void testDeleteBookById() throws AppException {
        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.findById(1))
                .thenReturn(assertBook);
        when(bookDAO.delete(assertBook))
                .thenReturn(true);
        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        JsonObject result = bookService.deleteBookById(1, "");
        String assertString = "{\"status\":\"OK\"}";
        assertEquals(result.toString(), assertString);
    }

    @Test
    void testUpdateImage() throws AppException {
        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.updateImageBookById(1, "1.jpg"))
                .thenReturn(true);

        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);

        boolean result = bookService.updateImage(1, "1.jpg");
        assertTrue(result);
    }

    @Test
    void testCancelBookingByUserId() throws AppException {
        BookDAO bookDAO = mock(BookDAO.class);
        when(bookDAO.findRendBookByRentId(1))
                .thenReturn(assertRentBook);
        when(bookDAO.deleteRentBookById(1))
                .thenReturn(true);
        UserDAO userDAO = mock(UserDAO.class);
        BookService bookService = new BookService();
        bookService.init(bookDAO, userDAO);
        bookService.initLanguage("en");
        JsonObject result = bookService.cancelBookingByUserId(1, 1);
        String assertString = "{\"status\":\"OK\",\"message\":\"Booking was canceled\"}";
        assertEquals(result.toString(), assertString);
    }
}