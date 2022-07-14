package com.tvv.web.command;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.db.impl.UserDAOImpl;
import com.tvv.service.exception.AppException;
import com.tvv.service.util.StringHash;
import com.tvv.web.util.security.IncognitoLevel;
import com.tvv.web.util.Path;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Login user command for authorisation user (check fields and user data)
 */
@IncognitoLevel
public class LoginCommand extends Command {

	private static final Logger log = Logger.getLogger(LoginCommand.class);
	private UserDAO userDAO;
	public LoginCommand() {
		userDAO = new UserDAOImpl();
	}
	public void setUp(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	/**
	 * Function for POST request. Check user  login and password, redirect to different page for USER and ADMIN
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		
		log.debug("Command starts "+ this.getClass().getSimpleName());
		HttpSession session = request.getSession();
		ResourceBundle message = UtilCommand.getLocale(request);
		String login = request.getParameter("login");
		log.trace("Request parameter: login " + login);
		String password = request.getParameter("password");
		String forward;

		/**
		 * Check blank login and password input
		 */
		if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
			session.setAttribute("errorHeader", message.getString("error.page.user.login"));
			session.setAttribute("errorMessage", message.getString("error.page.user.login.message.password_empty"));
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage " + message.getString("error.page.user.login.message.password_empty"));
			return;
		}
		/**
		 * Read user by login
		 */
		User currentUser;
		try {
			currentUser = userDAO.findUserByNumber(login);
		} catch (AppException e) {
			session.setAttribute("errorHeader", message.getString("error.page.user.login"));
			session.setAttribute("errorMessage", message.getString("error.page.user.login.message.password_empty"));
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage " + message.getString("error.page.user.login.message.password_empty"));
			return;
		}
		log.trace("Load from DB: user " + currentUser);
		/**
		 * Check user: exist or not
		 */
		if (currentUser == null) {
			session.setAttribute("errorHeader", message.getString("error.page.user.login"));
			session.setAttribute("errorMessage", message.getString("error.page.user.login.message.user_incorrect"));
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage: " + message.getString("error.page.user.login.message.user_incorrect"));
			return;
		} else if (!currentUser.getStatus().equals("enabled")) {
			session.setAttribute("errorHeader", message.getString("error.page.user.login"));
			session.setAttribute("errorMessage", message.getString("error.page.user.login.message.user_locked"));
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage: " + message.getString("error.page.user.login.message.user_locked"));
			return;
		}
		/**
		 * Check user: password
		 */
		else if (!StringHash.getHashString(password).equals(currentUser.getPassword())) {
			session.setAttribute("errorHeader", message.getString("error.page.user.login"));
			session.setAttribute("errorMessage", message.getString("error.page.user.login.message.user_password"));
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage: " + message.getString("error.page.user.login.message.user_password"));
			return;
		}
		/**
		 * Check user: all parameters are correct
		 */
		else {
			Role userRole = currentUser.getRole();
			log.trace("userRole: " + userRole);
			forward = getForwardPage(userRole);
			session.setAttribute("currentUser", currentUser);
			log.trace("Set the session attribute: user " + currentUser);
			session.setAttribute("userRole", userRole);
			log.trace("Set the session attribute: userRole " + userRole);
			session.setAttribute("currentPage", "users");
			log.trace("Set the session attribute: currentPage " + "users");

			String lang;
			try {
				lang = currentUser.getLocale();
			} catch (Exception e) {
				lang = "en";
				log.error("Can not read user locale");
			}
			Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", lang);
			session.setAttribute("currentLanguage", lang);
		}
		log.debug("Command finished");
		response.sendRedirect(forward);
	}

	public static String getForwardPage(Role userRole) {
		String forward;
		switch (userRole.getName()){
			case "admin":
				forward = Path.COMMAND__LIST_ADMIN_BOOK;
				break;
			case "librarian":
				forward = Path.COMMAND__LIST_LIBRARIAN_BOOK;
				break;
			case "user":
				forward = Path.COMMAND__LIST_USERS_BOOK;
				break;
			default:
				forward = Path.COMMAND__START_PAGE_USER;
				break;
		}
		return forward;
	}

	/**
	 * Execute GET function for StartController. This function doesn't have GET request, and redirect to error page
	 * @param request servlet request
	 * @param response servlet response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.trace("Start login get method method" + request.getMethod());
		String page = Path.PAGE__USER_LOGIN;
		RequestDispatcher disp = request.getRequestDispatcher(page);
		disp.forward(request, response);
		log.trace("Finish load command with method" + request.getMethod());
	}

}