package com.tvv.web.command.admin;

import com.tvv.db.entity.User;
import com.tvv.service.UserService;
import com.tvv.service.exception.AppException;
import com.tvv.service.util.FieldsChecker;
import com.tvv.web.command.Command;
import com.tvv.web.util.security.AdminLevel;
import com.tvv.web.util.Path;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@AdminLevel
public class CreateLibrarianCommand extends Command {
    private static final Logger log = Logger.getLogger(CreateLibrarianCommand.class);

    private UserService userService;

    public CreateLibrarianCommand() {
        userService = new UserService();
    }

    public void init(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        log.debug("Start create librarian POST command " + this.getClass().getSimpleName());
        /**
         * Read parameter from request
         */
        log.debug("Read parameter: ");
        Map<String, String> librarian = readParameters(request);
        /**
         * Check parameters and create account
         */
        String page;
        if (librarian != null) {
            String role = request.getServletContext().getInitParameter("LibrarianRoleId");
            User user = userService.createUser(librarian, role, librarian.get("locale"));
            page = Path.PAGE__SUCCESS_LIBRARIAN;
            log.trace("Create librarian: " + user);
        } else {
            page = Path.PAGE__FAIL_LIBRARIAN;
        }
        log.debug("Redirect to: " + page);
        response.sendRedirect(page);
        log.debug("Finish create librarian POST command " + this.getClass().getSimpleName());
    }

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request, response);
    }

    /**
     * Function for reading user's parameter from request
     *
     * @param request request from servlet with parameters
     * @return Map<String, String> key - parameter name, value - value of parameter
     */
    private Map<String, String> readParameters(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        boolean check = true;
        result.put("login", request.getParameter("number"));
        check = check && FieldsChecker.checkNameField(result.get("login"));
        result.put("password", request.getParameter("password"));
        check = check && FieldsChecker.checkPasswordField(result.get("password"));
        result.put("firstName", request.getParameter("first-name"));
        check = check && FieldsChecker.checkNameField(result.get("firstName"));
        result.put("lastName", request.getParameter("last-name"));
        check = check && FieldsChecker.checkNameField(result.get("lastName"));
        result.put("dateOfBirth", request.getParameter("date-of-birth"));
        check =
                check && FieldsChecker.checkAge16YearsOld(LocalDate.parse(result.get("dateOfBirth")));
        result.put("phone", request.getParameter("phone"));
        check = check && FieldsChecker.checkPhone(result.get("phone"));
        HttpSession session = request.getSession();
        session.getAttribute("currentLanguage");
        result.put("locale", (String) session.getAttribute("currentLanguage"));
        if (!request.getServletContext().getInitParameter("locales").contains(result.get("locale")))
            result.put("locale", "en");
        String photo;
        if (request.getParameter("photo-file")!=null) photo = request.getParameter("photoFile");
        else photo = "_blank.png";
        result.put("photoFile", photo);
        if (!check) return null;
        return result;
    }
}
