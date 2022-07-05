package com.tvv.web.command.admin;

import com.google.gson.JsonObject;
import com.tvv.service.BookService;
import com.tvv.service.UserService;
import com.tvv.service.dto.BookDTO;
import com.tvv.service.exception.AppException;
import com.tvv.service.util.FieldsChecker;
import com.tvv.web.command.Command;
import com.tvv.web.util.AdminLevel;
import com.tvv.web.util.Path;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command for Create book. Use annotation for
 */
@AdminLevel
public class CreateBookCommand extends Command {
    private static final Logger log = Logger.getLogger(CreateBookCommand.class);

    private BookService bookService;

    public CreateBookCommand() {
        bookService = new BookService();
    }

    public void init(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        log.debug("Start create book POST command " + this.getClass().getSimpleName());
        /**
         * Read parameter from request
         */
        log.debug("Read parameter: ");
        BookDTO bookDTO = readParameters(request);
        /**
         * Check parameters and create account
         */
        String page;
        if (bookDTO != null) {
            if (bookService.createBook(bookDTO)) {
                page = Path.PAGE__SUCCESS_BOOK;
            } else {
                page = Path.PAGE__FAIL_BOOK;
            }
            log.debug("Redirect to: " + page);
        } else page = Path.PAGE__FAIL_BOOK;
        response.sendRedirect(page);
        log.debug("Finish create book POST command " + this.getClass().getSimpleName());
    }

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request, response);
    }

    private BookDTO readParameters(HttpServletRequest request) {
        BookDTO result = new BookDTO();
        result.setId(0);
        boolean checkFields = true;
        result.setIsbn(request.getParameter("isbn"));
        checkFields = (checkFields && FieldsChecker.checkISBN(result.getIsbn()));
        result.setAuthor(request.getParameter("author"));
        checkFields = (checkFields && FieldsChecker.checkAuthors(result.getAuthor()));
        result.setGenre(request.getParameter("genre"));
        checkFields = (checkFields && FieldsChecker.checkGenre(result.getGenre()));
        result.setName(request.getParameter("name"));
        checkFields = (checkFields && FieldsChecker.checkBookName(result.getName()));
        result.setPublisher(request.getParameter("publisher"));
        checkFields = (checkFields && FieldsChecker.checkPublisher(result.getPublisher()));
        result.setYear(request.getParameter("year"));
        checkFields = (checkFields && FieldsChecker.checkYear(result.getYear()));
        result.setCount(Integer.parseInt(request.getParameter("count")));
        checkFields = (checkFields && FieldsChecker.checkCount(request.getParameter("count")));

        if (!checkFields) return null;
        /**
         * image item is blank by default
         */
        result.setImage("_blank.png");
        return result;
    }
}
