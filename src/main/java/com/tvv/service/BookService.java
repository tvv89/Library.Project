package com.tvv.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.*;
import com.tvv.db.entity.*;
import com.tvv.db.impl.*;
import com.tvv.db.util.PageSettings;
import com.tvv.service.exception.AppException;
import com.tvv.service.dto.BookDTO;
import com.tvv.service.dto.RentBookDTO;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import java.io.File;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class BookService {
    private static final Logger log = Logger.getLogger(BookService.class);
    private BookDAO bookDAO;
    private UserDAO userDAO;
    private String local;
    private ResourceBundle message;

    public BookService() {
        this.userDAO = new UserDAOImpl();
        this.bookDAO = new BookDAOImpl();
        this.local = "en";
        Locale locale = new Locale(local);
        this.message = ResourceBundle.getBundle("resources",locale);
    }

    public void init(BookDAO bookDAO, UserDAO userDAO) {
        this.bookDAO = bookDAO;
        this.userDAO = userDAO;
    }

    public void initLanguage(String local) {
        this.local = local;
        Locale locale = new Locale(this.local);
        this.message = ResourceBundle.getBundle("resources",locale);
    }

    public JsonObject bookListPagination(Map<String, Object> jsonParameters) {
        log.trace("Start booksListPagination method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        if (jsonParameters == null) return UtilCommand.errorMessageJSON("Illegal argument from JSON");
        Optional<Integer> currentPage;
        Optional<Integer> itemPerPage;
        Optional<String> sorting;
        currentPage = Optional.ofNullable((Integer) jsonParameters.get("currentPage"));
        itemPerPage = Optional.ofNullable((Integer) jsonParameters.get("items"));
        sorting = Optional.ofNullable((String) jsonParameters.get("sorting"));
        PageSettings pageSettings = new PageSettings();
        if (itemPerPage.orElse(5) <= 0) {
            pageSettings.setPage(1);
            pageSettings.setSize(Integer.MAX_VALUE);
        } else {
            pageSettings.setPage(currentPage.orElse(1));
            pageSettings.setSize(itemPerPage.orElse(5));
        }
        pageSettings.setSort(sorting.orElse("name"));
        try {
            String sortColumn = pageSettings.getSort();
            pageSettings.setSort(sortColumn);
            log.debug("Pagination parameter: " + pageSettings);
            List<CountBook> bookList = bookDAO.findAllBooks(pageSettings);
            List<BookDTO> list = bookList.stream()
                    .map(book -> new BookDTO(book))
                    .collect(Collectors.toList());
            /**
             * Select and show user list
             */
            long pages = (long) Math.ceil((double) bookDAO.bookCount() / pageSettings.getSize());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(pageSettings.getPage()));
            innerObject.add("pages", new Gson().toJsonTree(pages));
            innerObject.add("list", new Gson().toJsonTree(list));
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End usersListPagination method");
        return innerObject;
    }

    public JsonObject bookListPaginationWithSearch(Map<String, Object> jsonParameters) {
        log.trace("Start booksListPagination method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        Optional<Integer> currentPage;
        Optional<Integer> itemPerPage;
        Optional<String> sorting;
        Optional<String> searching;
        currentPage = Optional.ofNullable((Integer) jsonParameters.get("currentPage"));
        itemPerPage = Optional.ofNullable((Integer) jsonParameters.get("items"));
        sorting = Optional.ofNullable((String) jsonParameters.get("sorting"));
        searching = Optional.ofNullable((String) jsonParameters.get("searching"));
        PageSettings pageSettings = new PageSettings();
        if (itemPerPage.orElse(5) <= 0) {
            pageSettings.setPage(1);
            pageSettings.setSize(Integer.MAX_VALUE);
        } else {
            pageSettings.setPage(currentPage.orElse(1));
            pageSettings.setSize(itemPerPage.orElse(5));
        }
        pageSettings.setSort(sorting.orElse("name"));
        try {
            String sortColumn = pageSettings.getSort();
            pageSettings.setSort(sortColumn);
            String search = searching.orElse("");
            log.debug("Pagination parameter: " + pageSettings);
            List<CountBook> bookList = bookDAO.findBooksWithSearch(pageSettings, search);
            List<BookDTO> list = bookList.stream()
                    .map(book -> new BookDTO(book))
                    .collect(Collectors.toList());
            /**
             * Select and show user list
             */
            long pages = (long) Math.ceil((double) bookDAO.bookCount(search) / pageSettings.getSize());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(pageSettings.getPage()));
            innerObject.add("pages", new Gson().toJsonTree(pages));
            innerObject.add("list", new Gson().toJsonTree(list));
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End usersListPagination method");
        return innerObject;
    }

    public JsonObject bookRentListPagination(Map<String, Object> jsonParameters, long userId) {
        log.trace("Start booksRentListPagination method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        Optional<Integer> currentPage;
        Optional<Integer> itemPerPage;
        Optional<String> sorting;
        currentPage = Optional.ofNullable((Integer) jsonParameters.get("currentPage"));
        itemPerPage = Optional.ofNullable((Integer) jsonParameters.get("items"));
        sorting = Optional.ofNullable((String) jsonParameters.get("sorting"));
        PageSettings pageSettings = new PageSettings();
        if (itemPerPage.orElse(5) <= 0) {
            pageSettings.setPage(1);
            pageSettings.setSize(Integer.MAX_VALUE);
        } else {
            pageSettings.setPage(currentPage.orElse(1));
            pageSettings.setSize(itemPerPage.orElse(5));
        }
        pageSettings.setSort(sorting.orElse("name"));
        try {
            String sortColumn = pageSettings.getSort();
            pageSettings.setSort(sortColumn);
            log.debug("Pagination parameter: " + pageSettings);
            List<RentBook> bookList = bookDAO.findAllUserBooks(userId, pageSettings);
            List<RentBookDTO> list = bookList.stream()
                    .map(book -> new RentBookDTO(book))
                    .collect(Collectors.toList());
            /**
             * Select and show user list
             */
            double bookCount = (double) bookDAO.bookCount(userId);
            long pages = (long) Math.ceil(bookCount / pageSettings.getSize());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(pageSettings.getPage()));
            innerObject.add("pages", new Gson().toJsonTree(pages));
            innerObject.add("list", new Gson().toJsonTree(list));
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End bookRentListPagination method");
        return innerObject;
    }

    public JsonObject startRentBook(long id) {
        log.trace("Start startRentBook method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        try {
            RentBook rentBook = bookDAO.findRendBookByRentId(id);
            if (bookDAO.findByIdWithCount(rentBook.getBook().getId()).getCount() <= 0)
                return UtilCommand.errorMessageJSON(message.getString("error.json.book_service.no_book"));
            int countFire = bookDAO.countFineByUser("", rentBook.getUser().getId());
            if (countFire > 0) return UtilCommand.errorMessageJSON(message.getString("error.json.book_service.need_pay"));
            RentBook book = bookDAO.changeStartDateRentBook(id, rentBook.getBook().getId());
            RentBookDTO rentBookDTO = new RentBookDTO(book);
            /**
             * Select and show user list
             */
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("book", new Gson().toJsonTree(rentBookDTO));
            innerObject.add("message",
                    new Gson().toJsonTree(message.getString("message.json.book_service.update_rent_book.success")));
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End startRentBook method");
        return innerObject;
    }

    public JsonObject payFineForBook(long id) {
        log.trace("Start payTaxForBook method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        try {
            RentBook book = bookDAO.changePayStatusRentBook(id);
            RentBookDTO rentBookDTO = new RentBookDTO(book);
            /**
             * Select and show user list
             */
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("book", new Gson().toJsonTree(rentBookDTO));
            innerObject.add("message",
                    new Gson().toJsonTree(message.getString("message.json.book_service.pay_fine.success")));

        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End payTaxForBook method");
        return innerObject;
    }

    public JsonObject deleteBookFromRent(long id) {
        log.trace("Start deleteBookFromRent method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        try {
            RentBook book = bookDAO.findRendBookByRentId(id);
            boolean result = false;
            if (book.getStatus() != "need pay") result = bookDAO.deleteRentBook(id);
            /**
             * Select and show user list
             */
            if (result) {
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("message",
                        new Gson().toJsonTree(message.getString("message.json.book_service.delete_rent.success")));
            }
            else innerObject = UtilCommand.errorMessageJSON(message.getString("error.json.book_service.no_return"));
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End deleteBookFromRent method");
        return innerObject;
    }

    public JsonObject startRentBookByUserNumber(long bookId, String number) {
        log.trace("Start startRentBookByUserNumber method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        if (bookId <= 0) return UtilCommand.errorMessageJSON(message.getString("error.json.book_service.no_book"));
        try {
            CountBook book = bookDAO.findByIdWithCount(bookId);
            if (book != null && book.getCount() > 0) {
                User user = userDAO.findUserByNumber(number);
                int countFire = bookDAO.countFineByUser(number, 0L);
                if (countFire > 0) return UtilCommand.errorMessageJSON(message.getString("error.json.book_service.need_pay"));
                if (user != null && bookDAO.addBookToRentByUser(bookId, user.getId())) {
                    innerObject.add("status", new Gson().toJsonTree("OK"));
                } else innerObject = UtilCommand.errorMessageJSON(message.getString("error.json.book_service.no_user"));
            } else innerObject = UtilCommand.errorMessageJSON(message.getString("error.json.book_service.no_book"));
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End deleteBookFromRent method");
        return innerObject;
    }

    public boolean createBook(BookDTO bookDTO) {
        log.trace("Start createBook method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        String checkBookFields = "";
        boolean result = false;
        if (!checkBookFields.isEmpty()) return false;
        try {
            Book book = readBookFromRequest(bookDTO);
            if (book != null && bookDAO.create(book, bookDTO.getCount())) {
                result = true;
                innerObject.add("status", new Gson().toJsonTree("OK"));
            } else innerObject = UtilCommand.errorMessageJSON(message.getString("error.json.book_service.no_create"));
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
            result = false;
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End createBook method");
        return result;
    }

    public boolean updateBook(BookDTO bookDTO) {
        log.trace("Start updateBook method in " + this.getClass().getName());
        String checkBookFields = "";
        boolean result;
        if (!checkBookFields.isEmpty()) return false;
        try {
            Book book = readBookFromRequest(bookDTO);
            if (book != null && bookDAO.update(book, bookDTO.getCount())) {
                result = true;
            } else result = false;
        } catch (AppException ex) {
            result = false;
            log.error("Error in update book" + ex.getMessage());
        }
        log.debug("Result of update: " + result);
        log.trace("End updateBook method");
        return result;
    }

    private Book readBookFromRequest(BookDTO bookDTO) {
        Book book = new Book();
        if (bookDTO==null) return null;
        book.setId(bookDTO.getId());
        book.setIsbn(bookDTO.getIsbn());
        Set<Author> authors = new HashSet<>();
        Set<String> stringsSet = Arrays.stream(bookDTO.getAuthor().split(","))
                .map(s -> s.trim())
                .collect(Collectors.toSet());
        for (String s : stringsSet) {
            int lastIndex = s.lastIndexOf(" ");
            String firstName = s.substring(0, lastIndex).trim();
            String lastName = s.substring(lastIndex).trim();
            Author author = new Author();
            author.setId(0);
            author.setFirstName(firstName);
            author.setLastName(lastName);
            authors.add(author);
        }
        book.setAuthors(authors);
        book.setName(bookDTO.getName());
        Publisher publisher = new Publisher();
        publisher.setId(0);
        publisher.setName(bookDTO.getPublisher());
        book.setPublisher(publisher);
        book.setYear(Year.parse(bookDTO.getYear()));
        Set<Genre> genres = new HashSet<>();
        stringsSet = Arrays.stream(bookDTO.getGenre().split(","))
                .map(s -> s.trim())
                .collect(Collectors.toSet());
        for (String s : stringsSet) {
            Genre genre = new Genre();
            genre.setId(0);
            genre.setName(s);
            genres.add(genre);
        }
        //Add image
        book.setGenres(genres);
        book.setImage(bookDTO.getImage());

        return book;
    }

    public JsonObject findBookById(long bookId) {
        log.trace("Start findBookById method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        try {

            CountBook book = bookDAO.findByIdWithCount(bookId);
            BookDTO bookDTO = new BookDTO(book);
            /**
             * Select and show user list
             */
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("book", new Gson().toJsonTree(bookDTO));

        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End findBookById method");
        return innerObject;

    }

    public JsonObject deleteBookById(long id, String path) {
        log.trace("Start deleteBookById method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        try {
            Book book = bookDAO.findById(id);
            boolean result = false;
            if (book != null) result = bookDAO.delete(book);
            /**
             * Select and show user list
             */
            if (result) {
                innerObject.add("status", new Gson().toJsonTree("OK"));
                if (!book.getImage().equals("_blank.png")) {
                    File file = new File(path, book.getImage());
                    file.delete();
                }
            } else innerObject = UtilCommand.errorMessageJSON(message.getString("error.json.book_service.no_delete"));
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End deleteBookById method");
        return innerObject;

    }

    public JsonObject cancelBookingByUserId(long id, long userId) {
        log.trace("Start cancelBookingByUserId method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        try {
            RentBook book = bookDAO.findRendBookByRentId(id);
            boolean result = false;
            if (book.getUser().getId() == userId) result = bookDAO.deleteRentBookById(id);
            /**
             * Select and show user list
             */
            if (result) {
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("message",
                        new Gson().toJsonTree(message.getString("message.json.book_service.rent.success")));
            } else innerObject = UtilCommand.errorMessageJSON(message.getString("error.json.book_service.no_delete_rent"));
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End cancelBookingByUserId method");
        return innerObject;

    }

    public boolean updateImage(long bookId, String image) {
        if (bookId <= 0 || image == null) return false;
        boolean result;
        try {
            result = bookDAO.updateImageBookById(bookId, image);
        } catch (AppException ex) {
            log.error("Can not update image " + ex.getMessage());
            result = false;
        }
        return result;
    }
}
