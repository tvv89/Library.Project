package com.tvv.web.command.user;

import com.google.gson.JsonObject;
import com.tvv.db.entity.User;
import com.tvv.service.BookService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.util.UserLevel;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@UserLevel
public class UpdateListUserRentBooksCommand extends Command {
    private static final Logger log = Logger.getLogger(UpdateListUserRentBooksCommand.class);
    private BookService bookService;

    public UpdateListUserRentBooksCommand() {
        bookService = new BookService();
    }

    public void init(BookService bookService){
        this.bookService = bookService;
    }
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        log.trace("Start POST command " + this.getClass().getName());
        JsonObject innerObject;
        /**
         * Start JSON parsing request
         */
        Map<String, Object> jsonParameters = new HashMap<>();
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
        }
        /**
         * Check user
         */
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        /**
         * Create pagination and sorting
         */
        long userId = currentUser.getId();
        innerObject = bookService.bookRentListPagination(jsonParameters, userId);

        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST command " + this.getClass().getName());
    }

    /**
     * Execute GET function for StartController. This function doesn't have GET request, and redirect to error page
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
}
