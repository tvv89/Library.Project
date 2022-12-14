package com.tvv.web.command;

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
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Map;

/**
 * Command for change language
 */
@AdminLevel
@LibrarianLevel
@UserLevel
@IncognitoLevel
public class LanguageCommand extends Command {

	private static final Logger log = Logger.getLogger(LanguageCommand.class);

	/**
	 * POST request function execute 'process'
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
	 * GET request function don't use
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	}

	/**
	 * Function change language for user
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	void process (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.debug("Command starts "+ request.getMethod());
		HttpSession session = request.getSession();
		Map<String, Object> jsonParameters = null;
		try {
			jsonParameters = UtilCommand.parseRequestJSON(request);
		} catch (AppException e) {
			e.printStackTrace();
		}
		String lang = (String) jsonParameters.get("language");
		log.debug("language "+ lang);
		Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", lang);
		session.setAttribute("currentLanguage", lang);
		session.setAttribute("langPack", UtilCommand.jsLanguagePack(lang));
		log.trace("Refresh page");
		log.debug("Command finished "+ request.getMethod());
	}
}