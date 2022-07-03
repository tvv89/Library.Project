package com.tvv.web.command.incognito;

import com.tvv.service.UserService;
import com.tvv.web.command.Command;
import com.tvv.web.util.IncognitoLevel;
import com.tvv.web.util.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 * Registration command
 */
@IncognitoLevel
public class RegistrationCommand extends Command {

    private static final Logger log = Logger.getLogger(RegistrationCommand.class);

    private UserService userService;

    public RegistrationCommand() {
        this.userService = new UserService();
    }

    public void init(UserService userService) {
        this.userService = userService;
    }

    /**
     * Load registration page for new user
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__REGISTRATION);
        disp.forward(request, response);
        log.trace("Forward to: " + Path.PAGE__REGISTRATION);
    }

    /**
     * Load registration page for new user
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Start get method in RegistrationCommand ");
        HttpSession session = request.getSession();
        long roleId = Long.parseLong(request.getServletContext().getInitParameter("UserRoleId"));
        session.setAttribute("number", userService.getUserFreeNumber(roleId));
        RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__REGISTRATION);
        disp.forward(request, response);
        log.trace("Forward to: " + Path.PAGE__REGISTRATION);
    }
}
