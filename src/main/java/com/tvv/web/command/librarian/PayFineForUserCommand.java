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
 * This command for pay fine by user
 */
@LibrarianLevel
public class PayFineForUserCommand extends Command {

    private static final Logger log = Logger.getLogger(PayFineForUserCommand.class);

    private BookService bookService;

    public PayFineForUserCommand() {
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
        JsonObject innerObject;
        long id;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            id = (Integer) jsonParameters.get("id");
            log.trace("User pay tax for: " + id);
            bookService.initLanguage(UtilCommand.getStringLocale(request));
            innerObject = bookService.payFineForBook(id);
        } catch (Exception e) {
            ResourceBundle message = UtilCommand.getLocale(request);
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
