package com.tvv.web.command.librarian;

import com.google.gson.JsonObject;
import com.tvv.service.BookService;
import com.tvv.web.command.Command;
import com.tvv.web.util.LibrarianLevel;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This command give book for user who booked before
 */
@LibrarianLevel
public class GiveLibrarianBookCommand extends Command {

    private static final Logger log = Logger.getLogger(GiveLibrarianBookCommand.class);

    private BookService bookService;

    public GiveLibrarianBookCommand() {
        bookService = new BookService();
    }

    public void init(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Function for GET request. This command class don't use GET method
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST method " + this.getClass().getSimpleName());
        ResourceBundle message = UtilCommand.getLocale(request);
        bookService.initLanguage(UtilCommand.getStringLocale(request));
        JsonObject innerObject;
        String number;
        Integer bookId;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            bookId = (Integer) jsonParameters.get("bookId");
            number = (String) jsonParameters.get("user");
            log.trace("Give book " + bookId + "to user: " + number);
            if (bookId==null || number==null || number.isEmpty()) innerObject = UtilCommand
                    .errorMessageJSON(message.getString("error.page.user.create"));
            else innerObject = bookService.startRentBookByUserNumber(bookId, number);
        } catch (Exception e) {
            innerObject = UtilCommand
                    .errorMessageJSON(message.getString("error.page.user.create") + e.getMessage());
            log.error("Can't read correct data from request, because " + e.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST method " + this.getClass().getSimpleName());
    }

}
