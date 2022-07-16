package com.tvv.web.command;

import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.UserService;
import com.tvv.service.exception.AppException;
import com.tvv.web.util.*;
import com.tvv.web.util.security.AdminLevel;
import com.tvv.web.util.security.IncognitoLevel;
import com.tvv.web.util.security.LibrarianLevel;
import com.tvv.web.util.security.UserLevel;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Logout user command for authorisation user
 */
@AdminLevel
@LibrarianLevel
@UserLevel
@IncognitoLevel
public class LogoutCommand extends Command {

	private static final Logger log = Logger.getLogger(LogoutCommand.class);

	private UserService userService;

	public LogoutCommand() {
		this.userService = new UserService();
	}

	private void init(UserService userService){
		this.userService = userService;
	}
	/**
	 * POST request function is same GET request function. Execute 'process'
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		process(request,response);
	}

	/**
	 * GET request function is same POST request function. Execute 'process'
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		process(request,response);
	}

	/**
	 * Function invalidate session and redirect to start page
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	void process (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.trace("Command starts "+ request.getMethod());
		HttpSession session = request.getSession();
		Role userRole = (Role) session.getAttribute("userRole");
		User currentUser = (User) session.getAttribute("currentUser");
		String currentLanguage = (String) session.getAttribute("currentLanguage");
		if (userRole==null)
		{
			response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE_USER);
			log.debug("User role is not correct");
			return;
		}
		try {
			userService.updateLocalUserById(currentUser.getId(), currentLanguage);
			log.debug("Save currentLanguage to user");
		}
		catch (AppException ex)
		{
			log.error("Can not save currentLanguage to user");
		}

		session = request.getSession(false);
		if (session != null)
			session.invalidate();

		response.sendRedirect(request.getContextPath()+Path.COMMAND__START_PAGE_USER);
		log.trace("Forward to start page: " + request.getContextPath()+Path.COMMAND__START_PAGE_USER);
		log.debug("Command finished "+ request.getMethod());
	}
}