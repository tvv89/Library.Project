package com.tvv.web.command.user;

import com.tvv.db.entity.User;
import com.tvv.service.UserService;
import com.tvv.web.command.Command;
import com.tvv.web.util.Path;
import com.tvv.web.util.UtilCommand;
import com.tvv.web.util.security.AdminLevel;
import com.tvv.web.util.security.LibrarianLevel;
import com.tvv.web.util.security.UserLevel;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Command for add image user.
 */
@AdminLevel
@LibrarianLevel
@UserLevel
public class ImageUserLoadCommand extends Command {

    private static final Logger log = Logger.getLogger(ImageUserLoadCommand.class);

    private UserService userService;

    public ImageUserLoadCommand() {
        userService = new UserService();
    }

    public void setUp(UserService userService) {
        this.userService = userService;
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
        log.debug("Start save user image POST command " + this.getClass().getSimpleName());
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        Part filePart = request.getPart("image-user-update-file");
        InputStream fileContent = filePart.getInputStream();
        String fileName = (fileContent.available() > 0) ? user.getNumber() + ".jpg" : user.getPhoto();
        long userId = user.getId();
        try {
            if (userService.updateImage(userId, fileName)) {
                /**
                 * Save photo file
                 */
                if (fileContent.available() > 0) {
                    ServletContext servletContext = request.getServletContext();
                    String absolutePath = servletContext.getRealPath("/images/users");
                    File file = new File(absolutePath, fileName);
                    Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } else response.sendRedirect(Path.PAGE__FAIL_USER);
            log.debug("image was added: " + fileName);
            response.sendRedirect(Path.COMMAND__START_PAGE_USER);
        } catch (Exception e) {
            log.trace(e.getMessage());
            ResourceBundle message = UtilCommand.getLocale(request);
            session.setAttribute("errorHeader", message.getString("error.page.user.image"));
            session.setAttribute("errorMessage", message.getString("error.page.user.image.message") +e.getMessage());
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

        result.put("login", request.getParameter("login"));
        result.put("password", request.getParameter("password"));
        result.put("firstName", request.getParameter("first-name"));
        result.put("lastName", request.getParameter("last-name"));
        result.put("dateOfBirth", request.getParameter("date-of-birth"));
        result.put("phone", request.getParameter("phone"));
        result.put("locale", request.getParameter("locale"));

        return result;
    }

}