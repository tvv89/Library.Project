package com.tvv.web.command.admin;

import com.tvv.service.BookService;
import com.tvv.service.dto.BookDTO;
import com.tvv.service.exception.AppException;
import com.tvv.service.util.FieldsChecker;
import com.tvv.web.command.Command;
import com.tvv.web.util.security.AdminLevel;
import com.tvv.web.util.Path;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Update book command for single page application
 */
@AdminLevel
public class UpdateBookCommand extends Command {
    private static final Logger log = Logger.getLogger(UpdateBookCommand.class);

    private BookService bookService;

    public UpdateBookCommand() {
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
        BookDTO bookDTO = readParameters(request);
        log.debug("Read parameter: " + bookDTO);
        /**
         * Check parameters and update book
         */
        if (bookDTO != null && bookService.updateBook(bookDTO))
            response.sendRedirect(Path.COMMAND__LIST_ADMIN_BOOK);
        else response.sendRedirect(Path.PAGE__FAIL_BOOK);
        log.debug("Finish create book POST command " + this.getClass().getSimpleName());
    }

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request, response);
    }

    private BookDTO readParameters(HttpServletRequest request) {
        BookDTO result = new BookDTO();
        long id = 0;
        boolean checkFields = true;
        if (!request.getParameter("upd-current-id").equals(""))
            id = Long.parseLong(request.getParameter("upd-current-id"));
        result.setId(id);
        result.setIsbn(request.getParameter("upd-isbn"));
        checkFields = (checkFields && FieldsChecker.checkISBN(result.getIsbn()));
        result.setAuthor(request.getParameter("upd-author"));
        checkFields = (checkFields && FieldsChecker.checkAuthors(result.getAuthor()));
        result.setName(request.getParameter("upd-name"));
        checkFields = (checkFields && FieldsChecker.checkBookName(result.getName()));
        result.setPublisher(request.getParameter("upd-publisher"));
        checkFields = (checkFields && FieldsChecker.checkPublisher(result.getPublisher()));
        result.setYear(request.getParameter("upd-year"));
        checkFields = (checkFields && FieldsChecker.checkYear(result.getYear()));
        result.setGenre(request.getParameter("upd-genre"));
        checkFields = (checkFields && FieldsChecker.checkGenre(result.getGenre()));
        result.setCount(Integer.parseInt(request.getParameter("upd-count")));
        checkFields = (checkFields && FieldsChecker.checkCount(request.getParameter("upd-count")));
        /**
         * image item
         */
        result.setImage(null);
        if (!checkFields) return null;
        return result;
    }
}
