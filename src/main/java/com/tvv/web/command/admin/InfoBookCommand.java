package com.tvv.web.command.admin;

import com.google.gson.JsonObject;
import com.tvv.service.BookService;
import com.tvv.web.command.Command;
import com.tvv.web.util.security.AdminLevel;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This command show user information via send JSON response
 */
@AdminLevel
public class InfoBookCommand extends Command {

    private static final Logger log = Logger.getLogger(InfoBookCommand.class);

    private BookService bookService;

    public InfoBookCommand() {
        bookService = new BookService();
    }

    public void setUp(BookService bookService) {
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
        JsonObject innerObject = new JsonObject();
        bookService.initLanguage(UtilCommand.getStringLocale(request));
        /**
         * User info can read only ADMIN
         */
        ResourceBundle message = UtilCommand.getLocale(request);
        long bookId;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            bookId = (Integer) jsonParameters.get("id");
            log.trace("Read book for info: " + bookId);
            innerObject = bookService.findBookById(bookId);
        } catch (Exception e) {
            innerObject = UtilCommand.
                    errorMessageJSON(message.getString("error.json.incorrect.request.data") + e.getMessage());
            log.error("Can't read correct data from request, because " + e.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST method " + this.getClass().getSimpleName());
    }

}
