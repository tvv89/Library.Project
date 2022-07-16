package com.tvv.web.filter;

import com.tvv.db.entity.Role;
import com.tvv.web.util.Path;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter for character encoding
 */
@WebFilter("/librarian")
public class LibrarianAccessRightFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    /**
     * Function doFilter
     * @param request servlet request
     * @param response servlet response
     * @param chain filter chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        /**
         * Access for Librarian
         */
        long librarianRoleId = Long.parseLong(request.getServletContext().getInitParameter("LibrarianRoleId"));
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        Role role = (Role) session.getAttribute("userRole");
        if (role != null && role.getId()==librarianRoleId) {
            chain.doFilter(request, response);
        } else {
            RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ACCESS_DENIED);
            disp.forward(request, response);
        }

    }
}
