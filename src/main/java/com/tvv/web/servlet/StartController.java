package com.tvv.web.servlet;

import com.tvv.db.entity.Role;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.CommandCollection;
import com.tvv.web.command.LoginCommand;
import com.tvv.web.util.security.IncognitoLevel;
import com.tvv.web.util.Path;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Main servlet controller. Application uses one controller and different command.
 */
@WebServlet(name = "Start", value = "/start")
@MultipartConfig
public class StartController extends HttpServlet {

    private static final Logger log = Logger.getLogger(StartController.class);

    /**
     * Function for GET request. Use command GET function
     *
     * @param request http servlet request
     * @param response http servlet response
     * @throws ServletException exception
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        log.debug("StartController  started GET");
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole != null) {
            String forward = LoginCommand.getForwardPage(userRole);
            response.sendRedirect(forward);
            log.debug("StartController finished GET with " + forward);
            return;
        }
        process(request, response);
    }

    /**
     * Function for POST request. Use command POST function
     *
     * @param request http servlet request
     * @param response http servlet response
     * @throws ServletException exception
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
         * Read command name
         */
        log.debug("StartController Incognito started");
        String commandName = request.getParameter("command");
        log.trace("Request parameter command: " + commandName);
        if (commandName == null) commandName = "listIncognitoAllBooks";
        Command command = CommandCollection.get(commandName);
        log.trace("Command is " + command);
        try {
            /*
             * GET/POST executed function
             */
            Annotation[] annotations = command.getClass().getAnnotations();
            if (Arrays.stream(annotations).anyMatch(a -> a instanceof IncognitoLevel)) {
                if (request.getMethod().equals("GET")) command.executeGet(request, response);
                if (request.getMethod().equals("POST")) command.executePost(request, response);
            } else {
                RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ACCESS_DENIED);
                disp.forward(request, response);
            }
        } catch (ServletException | NullPointerException e) {
            /*
             * Show error page
             */
            request.getSession().setAttribute("errorHeader", "404");
            request.getSession().setAttribute("errorMessage", "Page not found");
            UtilCommand.goToErrorPage(request, response);
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
        log.debug("StartController Incognito finished with " + commandName);
    }

}