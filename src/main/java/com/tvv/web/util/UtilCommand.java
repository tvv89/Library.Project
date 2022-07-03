package com.tvv.web.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.CommandCollection;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class UtilCommand {

    private static final Logger log = Logger.getLogger(UtilCommand.class);

    public static void sendJSONData(HttpServletResponse response, JsonObject innerObject) throws IOException {
        String sendData = new Gson().toJson(innerObject);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(sendData);
        out.flush();
    }

    /**
     * Function for parsing JSON request
     * @param request servlet request
     * @return Map: key - string, value - object from JSON data
     * @throws IOException
     * @throws AppException
     */
    public static Map<String, Object> parseRequestJSON(HttpServletRequest request) throws IOException, AppException {
        Map<String, Object> result = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        String line = null;

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) sb.append(line);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(sb.toString());
            log.trace("Parse JSON object in POST method: " + jsonObject.toString());
        }
        catch (Exception ex){
            log.error("Bad parse JSON object in POST method because: " + ex.getMessage());
            throw new AppException("Bad parse JSON object in POST method",ex);
        }

        result = jsonObject.toMap();

        return result;
    }

    /**
     * Show Access denied page for user
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    public static void bedGETRequest (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
        RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ACCESS_DENIED);
        disp.forward(request, response);
        log.trace("Forward to: " + Path.PAGE__ACCESS_DENIED);
    }

    /**
     * Redirect to Error Page (with parameters)
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    public static void goToErrorPage (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start error page with method " + request.getMethod());
        response.sendRedirect(request.getContextPath() + Path.PAGE__ERROR_PAGE);
        log.trace("Forward to: " + Path.PAGE__ERROR_PAGE);
    }

    /**
     * Generate JSON object with Error message (for POST response)
     * @param message String error
     * @return JSON object (usually use for 'sendJSONData')
     */
    public static JsonObject errorMessageJSON (String message) {
        JsonObject innerObject = new JsonObject();
        innerObject.add("status", new Gson().toJsonTree("ERROR"));
        innerObject.add("message", new Gson().toJsonTree(message));
        return  innerObject;
    }

    public static ResourceBundle getLocale(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        String local = (String) session.getAttribute("currentLanguage");
        if (local==null || local.isEmpty()) local="";
        Locale locale = new Locale(local);
        ResourceBundle message = ResourceBundle.getBundle("resources",locale);
        return message;
    }
}
