package com.tvv.web.command.librarian;

import com.google.gson.JsonObject;
import com.tvv.db.entity.Role;
import com.tvv.service.BookService;
import com.tvv.service.UserService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.util.LibrarianLevel;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@LibrarianLevel
public class UpdateListLibrarianUsersCommand extends Command {
    private static final Logger log = Logger.getLogger(UpdateListLibrarianUsersCommand.class);
    private UserService userService;

    public UpdateListLibrarianUsersCommand() {
        userService = new UserService();
    }

    public void init(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        log.trace("Start POST command " + this.getClass().getName());
        JsonObject innerObject;
        Map<String, Object> jsonParameters = new HashMap<>();
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
        }
        /**
         * Create pagination and sorting
         */
        long roleId = Long.parseLong(request.getServletContext().getInitParameter("UserRoleId"));
        innerObject = userService.usersListPagination(jsonParameters, roleId);
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
