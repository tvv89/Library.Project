package com.tvv.web.command.librarian;

import com.tvv.web.command.Command;
import com.tvv.web.util.LibrarianLevel;
import com.tvv.web.util.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@LibrarianLevel
public class LoadListLibrarianBooksCommand extends Command {

    private static final Logger log = Logger.getLogger(LoadListLibrarianBooksCommand.class);

    /**
     * Function for GET request. This function redirect user to list Books
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
     * Function for POST request. This function redirect user to list Books
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
     * Main function is the same for POST and GET method. Forward user to list of Books. If user didn't
     * authorize, command redirect him to start page
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
        HttpSession session = request.getSession();
        session.setAttribute("currentPage", "books");
        String page = Path.PAGE__LIBRARIAN_ALL_BOOKS;
        RequestDispatcher disp = request.getRequestDispatcher(page);
        disp.forward(request, response);
        log.trace("Finish load command with method" + request.getMethod());

    }
}
