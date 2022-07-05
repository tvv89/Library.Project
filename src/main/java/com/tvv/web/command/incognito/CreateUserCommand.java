package com.tvv.web.command.incognito;

import com.tvv.db.entity.User;
import com.tvv.service.UserService;
import com.tvv.service.exception.AppException;
import com.tvv.service.util.FieldsChecker;
import com.tvv.web.command.Command;
import com.tvv.web.util.IncognitoLevel;
import com.tvv.web.util.Path;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Command for create user. Use UserService for add and check user data
 */
@IncognitoLevel
public class CreateUserCommand extends Command {

    private static final Logger log = Logger.getLogger(CreateUserCommand.class);

    private UserService userService;

    public CreateUserCommand() {
        userService = new UserService();
    }

    public void init(UserService service) {
        this.userService = service;
    }

    /**
     * Execute POST function for StartController. This function use request data and send response to
     * page user list. Create user with photo file in new page
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request,
                            HttpServletResponse response) throws IOException, ServletException {
        log.debug("Start registration POST command " + this.getClass().getSimpleName());
        ResourceBundle message = UtilCommand.getLocale(request);
        HttpSession session = request.getSession();
        String currentLanguage = (String) session.getAttribute("currentLanguage");
        if (currentLanguage == null) currentLanguage = "";
        Map<String, String> userData = readParameters(request);
        /**
         * Create user with parameter
         */
        try {
            /**
             * Create stream for read loaded photo
             */
            Part filePart = request.getPart("photo-file");
            String fileName = request.getParameter("login") + ".jpg";
            InputStream fileContent = filePart.getInputStream();
            ServletContext servletContext = request.getServletContext();
            String absolutePathToIndexJSP = servletContext.getRealPath("/images/users");
            /**
             * Save photo file
             */
            if (fileContent.available() > 0) {
                File file = new File(absolutePathToIndexJSP, fileName);
                Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else
                fileName = "_blank.png";
            if (userData == null) {
                log.trace("Bag user data");
                session.setAttribute("errorHeader", message.getString("error.page.user.create"));
                session.setAttribute("errorMessage", message.getString("error.page.user.fields.message.bad_fields"));
                UtilCommand.goToErrorPage(request, response);
                log.debug("Finish registration POST command " + this.getClass().getSimpleName());
                return;
            }
            userData.put("photoFile", fileName);
            String userRole = request.getServletContext().getInitParameter("UserRoleId");
            User user = userService.createUser(userData, userRole, currentLanguage);
            log.debug("User was added: " + user);
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE_USER);
        } catch (AppException e) {
            log.trace(e.getMessage());
            session.setAttribute("errorHeader", message.getString("error.page.user.create"));
            session.setAttribute("errorMessage", e.getMessage());
            UtilCommand.goToErrorPage(request, response);
        }

        log.debug("Finish registration POST command " + this.getClass().getSimpleName());
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

    /**
     * Function for reading user's parameter from request
     *
     * @param request request from servlet with parameters
     * @return Map<String, String> key - parameter name, value - value of parameter
     */
    private Map<String, String> readParameters(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        boolean check = true;
        result.put("login", request.getParameter("login"));
        //check = check && FieldsChecker.checkNameField(result.get("login"));
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
        result.put("locale", request.getParameter("locale"));
        if (!request.getServletContext().getInitParameter("locales").contains(result.get("locale")))
            result.put("locale", "en");
        if (!check) return null;
        return result;
    }

}