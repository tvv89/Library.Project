package com.tvv.web.command.user;

import com.google.gson.JsonObject;
import com.tvv.db.entity.User;
import com.tvv.service.BookService;
import com.tvv.web.command.Command;
import com.tvv.web.util.UserLevel;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This command for cancel booking item by user send JSON response
 */
@UserLevel
public class CancelBookingCommand extends Command {

    private static final Logger log = Logger.getLogger(CancelBookingCommand.class);

    private BookService bookService;

    public CancelBookingCommand() {
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
        JsonObject innerObject = new JsonObject();
        bookService.initLanguage(UtilCommand.getStringLocale(request));
        /**
         * Check user
         */
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        long bookId;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            bookId = (Integer) jsonParameters.get("id");
            long userId = currentUser.getId();
            log.trace("Give book " + bookId + "to user: " + userId);
            innerObject = bookService.cancelBookingByUserId(bookId, userId);
        } catch (Exception e) {
            ResourceBundle message = UtilCommand.getLocale(request);
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
