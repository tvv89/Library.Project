package com.tvv.web.command.user;

import com.tvv.web.command.Command;
import com.tvv.web.util.Path;
import com.tvv.web.util.UserLevel;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@UserLevel
public class LoadListUserRentBooksCommand extends Command {

    private static final Logger log = Logger.getLogger(LoadListUserRentBooksCommand.class);

    /**
     * Function for GET request. This function redirect user to list Rent books
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        process(request, response);

    }

    /**
     * Function for POST request. This function redirect user to list Rent books
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        process(request, response);

    }

    /**
     * Main function is the same for POST and GET method. Forward user to list of Users. If user didn't
     * authorize, command redirect him to error page
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
        HttpSession session = request.getSession();
        session.setAttribute("currentPage","rentbooks");
        String page = Path.PAGE__USER_RENT_BOOKS;
        RequestDispatcher disp = request.getRequestDispatcher(page);
        disp.forward(request, response);
        log.trace("Finish load command with method" + request.getMethod());

    }
}
