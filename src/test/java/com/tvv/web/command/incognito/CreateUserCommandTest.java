package com.tvv.web.command.incognito;

import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.UserService;
import com.tvv.service.exception.AppException;
import com.tvv.service.util.FieldsChecker;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.ws.rs.ApplicationPath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateUserCommandTest {

    @Test
    void testExecutePostCorrect() throws ServletException, IOException, AppException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("currentLanguage")).thenReturn("en");

        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("89000001");
        when(request.getParameter("password")).thenReturn("1111");
        when(request.getParameter("first-name")).thenReturn("First");
        when(request.getParameter("last-name")).thenReturn("Last");
        when(request.getParameter("date-of-birth")).thenReturn("2000-01-01");
        when(request.getParameter("phone")).thenReturn("+380991234567");
        when(request.getParameter("locale")).thenReturn("en");

        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getRealPath("/images/users"))
                .thenReturn("C:\\Users\\tymch\\IdeaProjects\\Library\\target\\Library-1.0-SNAPSHOT\\images\\users");
        when(servletContext.getInitParameter("UserRoleId")).thenReturn("3");
        when(servletContext.getInitParameter("locales")).thenReturn("en uk");
        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getContextPath()).thenReturn("");
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        Part filePart = mock(Part.class);
        when(filePart.getInputStream()).thenReturn(inputStream);
        when(request.getPart("photo-file")).thenReturn(filePart);
        UserService userService = mock(UserService.class);
        Map<String, String> userData = new HashMap<>();
        userData.put("login", "89000001");
        userData.put("password", "1111");
        userData.put("firstName", "First");
        userData.put("lastName", "Last");
        userData.put("dateOfBirth", "2000-01-01");
        userData.put("phone", "+380991234567");
        userData.put("locale", "en");
        userData.put("photoFile", "_blank.png");

        when(userService.createUser(userData, "3", "en")).thenReturn(new User());

        CreateUserCommand command = new CreateUserCommand();
        command.init(userService);
        command.executePost(request,response);
        verify(response,times(1)).sendRedirect("/start");

    }

    @Test
    void testExecutePostException() throws ServletException, IOException, AppException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("currentLanguage")).thenReturn("en");

        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("89000001");
        when(request.getParameter("password")).thenReturn("1111");
        when(request.getParameter("first-name")).thenReturn("First");
        when(request.getParameter("last-name")).thenReturn("Last");
        when(request.getParameter("date-of-birth")).thenReturn("2000-01-01");
        when(request.getParameter("phone")).thenReturn("+380991234567");
        when(request.getParameter("locale")).thenReturn("en");

        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getRealPath("/images/users"))
                .thenReturn("C:\\Users\\tymch\\IdeaProjects\\Library\\target\\Library-1.0-SNAPSHOT\\images\\users");
        when(servletContext.getInitParameter("UserRoleId")).thenReturn("3");
        when(servletContext.getInitParameter("locales")).thenReturn("en uk");
        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getContextPath()).thenReturn("");
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        Part filePart = mock(Part.class);
        when(filePart.getInputStream()).thenReturn(inputStream);
        when(request.getPart("photo-file")).thenReturn(filePart);
        UserService userService = mock(UserService.class);
        Map<String, String> userData = new HashMap<>();
        userData.put("login", "89000001");
        userData.put("password", "1111");
        userData.put("firstName", "First");
        userData.put("lastName", "Last");
        userData.put("dateOfBirth", "2000-01-01");
        userData.put("phone", "+380991234567");
        userData.put("locale", "en");
        userData.put("photoFile", "_blank.png");

        //when(userService.createUser(userData, "3", "en")).thenReturn(new User());
        when(userService.createUser(userData, "3", "en"))
                .thenThrow(new AppException("error", new IllegalArgumentException()));

        CreateUserCommand command = new CreateUserCommand();
        command.init(userService);
        command.executePost(request,response);
        verify(response,times(1)).sendRedirect("/error.page.jsp");

    }

    @Test
    void testExecutePostBadField() throws ServletException, IOException, AppException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("currentLanguage")).thenReturn("en");

        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("89000001");
        when(request.getParameter("password")).thenReturn("1111");
        when(request.getParameter("first-name")).thenReturn("First1");
        when(request.getParameter("last-name")).thenReturn("Last");
        when(request.getParameter("date-of-birth")).thenReturn("2000-01-01");
        when(request.getParameter("phone")).thenReturn("+380991234567");
        when(request.getParameter("locale")).thenReturn("en");

        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getRealPath("/images/users"))
                .thenReturn("C:\\Users\\tymch\\IdeaProjects\\Library\\target\\Library-1.0-SNAPSHOT\\images\\users");
        when(servletContext.getInitParameter("UserRoleId")).thenReturn("3");
        when(servletContext.getInitParameter("locales")).thenReturn("en uk");
        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getContextPath()).thenReturn("");
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        Part filePart = mock(Part.class);
        when(filePart.getInputStream()).thenReturn(inputStream);
        when(request.getPart("photo-file")).thenReturn(filePart);
        UserService userService = mock(UserService.class);
        Map<String, String> userData = new HashMap<>();
        userData.put("login", "89000001");
        userData.put("password", "1111");
        userData.put("firstName", "First");
        userData.put("lastName", "Last");
        userData.put("dateOfBirth", "2000-01-01");
        userData.put("phone", "+380991234567");
        userData.put("locale", "en");
        userData.put("photoFile", "_blank.png");

        when(userService.createUser(userData, "3", "en")).thenReturn(new User());

        CreateUserCommand command = new CreateUserCommand();
        command.init(userService);
        command.executePost(request,response);
        verify(response,times(1)).sendRedirect("/error.page.jsp");

    }

}