package com.tvv.web.command.admin;

import com.google.gson.JsonObject;
import com.tvv.service.BookService;
import com.tvv.web.command.Command;
import com.tvv.web.util.AdminLevel;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This command delete book and send JSON response
 */
@AdminLevel
public class DeleteBookCommand extends Command {

    private static final Logger log = Logger.getLogger(DeleteBookCommand.class);

    private BookService bookService;

    public DeleteBookCommand() {
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
        JsonObject innerObject;
        /**
         * Delete book
         */
        ResourceBundle message = UtilCommand.getLocale(request);
        long id;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            id = (Integer) jsonParameters.get("bookId");
            log.trace("Come back book to library: " + id);
            ServletContext servletContext = request.getServletContext();
            String absolutePath = servletContext.getRealPath("/images/books");
            innerObject = bookService.deleteBookById(id, absolutePath);
        } catch (Exception e) {
            innerObject = UtilCommand
                    .errorMessageJSON(message.getString("error.json.incorrect.request.data") + e.getMessage());
            log.error("Can't read correct data from request, because " + e.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST method " + this.getClass().getSimpleName());
    }

}
