package com.tvv.web.command.user;

import com.tvv.db.entity.User;
import com.tvv.service.UserService;
import com.tvv.service.dto.UserDTO;
import com.tvv.service.exception.AppException;
import com.tvv.service.util.FieldsChecker;
import com.tvv.web.command.Command;
import com.tvv.web.util.Path;
import com.tvv.web.util.UtilCommand;
import com.tvv.web.util.security.AdminLevel;
import com.tvv.web.util.security.LibrarianLevel;
import com.tvv.web.util.security.UserLevel;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Update book command for single page application
 */
@AdminLevel
@LibrarianLevel
@UserLevel
public class UpdateUserCommand extends Command {
    private static final Logger log = Logger.getLogger(UpdateUserCommand.class);

    private UserService userService;

    public UpdateUserCommand() {
        userService = new UserService();
    }

    public void init(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        log.debug("Start create book POST command " + this.getClass().getSimpleName());
        ResourceBundle message = UtilCommand.getLocale(request);
        HttpSession session = request.getSession();
        /**
         * Read parameter from request
         */
        UserDTO userDTO = readParameters(request);
        log.debug("Read parameter: " + userDTO);
        /**
         * Check parameters and update book
         */
        if (userDTO != null && userService.updateUser(userDTO)) {
            User user = userService.findUserById(userDTO.getId());
            session.setAttribute("currentUser", user);
            response.sendRedirect(Path.COMMAND__START_PAGE_USER);
        } else {
            session.setAttribute("errorHeader", message.getString("error.page.user.create"));
            session.setAttribute("errorMessage", message.getString("error.page.user.fields.message.bad_fields"));
            UtilCommand.goToErrorPage(request, response);
            log.trace("Can not update user");
        }
        log.debug("Finish create book POST command " + this.getClass().getSimpleName());
    }

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request, response);
    }

    private UserDTO readParameters(HttpServletRequest request) {
        UserDTO result = new UserDTO();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        boolean check = true;
        result.setId(user.getId());
        result.setNumber(user.getNumber());
        result.setFirstName(request.getParameter("update-user-first-name"));
        check = check && FieldsChecker.checkNameField(result.getFirstName());
        result.setLastName(request.getParameter("update-user-last-name"));
        check = check && FieldsChecker.checkNameField(result.getLastName());
        result.setDateOfBirth(request.getParameter("update-user-date-of-birth"));
        check = check && FieldsChecker.checkAge16YearsOld(LocalDate.parse(result.getDateOfBirth()));
        result.setPhone(request.getParameter("update-user-phone"));
        check = check && FieldsChecker.checkPhone(result.getPhone());
        if (!check) return null;
        return result;
    }
}
