package com.tvv.web.servlet;

import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.CommandCollection;
import com.tvv.web.util.LibrarianLevel;
import com.tvv.web.util.Path;
import com.tvv.web.util.UserLevel;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * servlet controller for user. Application uses one controller and different command.
 */
@WebServlet(name = "Librarian", value = "/librarian")
@MultipartConfig
public class LibrarianServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(LibrarianServlet.class);

    /**
     * Function for GET request. Use command GET function
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        log.debug("StartController Librarian started GET");
        String commandName = request.getParameter("command");
        log.trace("Request parameter command GET: " + commandName);
        Command command = CommandCollection.get(commandName);
        log.trace("Command is " + command);
        try {
            /**
             * GET executed function
             */
            Annotation[] annotations = command.getClass().getAnnotations();
            if (Arrays.stream(annotations).anyMatch(a -> a instanceof LibrarianLevel)) {
                command.executeGet(request, response);
            } else {
                RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ACCESS_DENIED);
                disp.forward(request, response);
            }
        } catch (ServletException | NullPointerException e) {
            /**
             * Show error page
             */
            request.getSession().setAttribute("errorHeader", "404");
            request.getSession().setAttribute("errorMessage", "Page not found");
            UtilCommand.goToErrorPage(request, response);
        }
        log.debug("StartController Librarian finished GET with " + commandName);
    }

    /**
     * Function for POST request. Use command POST function
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        log.debug("StartController Librarian finished POST");
        /**
         * Read command name
         */
        String commandName = request.getParameter("command");
        log.trace("Request parameter command POST: " + commandName);
        Command command = CommandCollection.get(commandName);
        log.trace("Command is " + command);
        try {
            /**
             * POST executed function
             */
            Annotation[] annotations = command.getClass().getAnnotations();
            if (Arrays.stream(annotations).anyMatch(a -> a instanceof LibrarianLevel)) {
                command.executePost(request, response);
            } else {
                RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ACCESS_DENIED);
                disp.forward(request, response);
            }
        } catch (ServletException | NullPointerException | AppException e) {
            /**
             * Show error page
             */
            request.getSession().setAttribute("errorHeader", "404");
            request.getSession().setAttribute("errorMessage", "Page not found " + e.getMessage());
            UtilCommand.goToErrorPage(request, response);
        }
        log.debug("StartController Librarian finished POST with " + commandName);
    }

}