package com.tvv.web.command.librarian;

import com.google.gson.JsonObject;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.UserService;
import com.tvv.web.command.Command;
import com.tvv.web.util.LibrarianLevel;
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
 * This command show user information via send JSON response
 */
@LibrarianLevel
public class InfoUserCommand extends Command {

    private static final Logger log = Logger.getLogger(InfoUserCommand.class);

    private UserService userService;

    public InfoUserCommand() {
        userService = new UserService();
    }

    public void setUp(UserService userService) {
        this.userService = userService;
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
        String userNumber;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            userNumber = (String) jsonParameters.get("userNumber");
            log.trace("Read user number for info: " + userNumber);
            userService.initLanguage(UtilCommand.getStringLocale(request));
            innerObject = userService.getUserInfoByNumber(userNumber);
        } catch (Exception e) {
            ResourceBundle message = UtilCommand.getLocale(request);
            innerObject = UtilCommand.errorMessageJSON(message.getString("error.json.incorrect.request.data") + e.getMessage());
            log.error("Can't read correct data from request, because " + e.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST method " + this.getClass().getSimpleName());
    }

}
