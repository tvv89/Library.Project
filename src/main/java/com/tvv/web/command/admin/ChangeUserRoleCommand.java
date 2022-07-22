package com.tvv.web.command.admin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.entity.User;
import com.tvv.service.UserService;
import com.tvv.service.dto.UserDTO;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.util.UtilCommand;
import com.tvv.web.util.security.AdminLevel;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This command show change user status by admin
 */
@AdminLevel
public class ChangeUserRoleCommand extends Command {
    private static final Logger log = Logger.getLogger(ChangeUserRoleCommand.class);

    private UserService userService;

    public ChangeUserRoleCommand() {
        userService = new UserService();
    }

    public void init(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        log.trace("Start POST command " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        long librarianRoleId = Long.parseLong(request.getServletContext().getInitParameter("LibrarianRoleId"));
        long adminRoleId = Long.parseLong(request.getServletContext().getInitParameter("AdminRoleId"));
        /*
         * Start JSON parsing request
         */
        ResourceBundle message = UtilCommand.getLocale(request);
        Map<String, Object> jsonParameters = null;
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        Integer userId = null;
        User userById = null;
        try {
            userId = (Integer) (jsonParameters != null ? jsonParameters.get("userId") : 0);
            userById = userService.findUserById(userId.longValue());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        try {
            if (userById != null && userById.getRole().getId() == librarianRoleId) {
                userById = userService.changeRoleUserById(userId.longValue(), adminRoleId);
                UserDTO user = new UserDTO(userById);
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("user", new Gson().toJsonTree(user));
            } else {
                innerObject = UtilCommand
                        .errorMessageJSON(message.getString("error.json.change.user.role"));
            }
        } catch (AppException ex) {
            log.error(ex.getMessage());
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        /*
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST command " + this.getClass().getSimpleName());
    }

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request, response);
    }
}
