package com.tvv.web.servlet;

import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.LoginCommand;
import com.tvv.web.command.LogoutCommand;
import com.tvv.web.command.incognito.RegistrationCommand;
import com.tvv.web.util.AdminLevel;
import com.tvv.web.util.Path;
import com.tvv.web.util.UserLevel;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServletTest {

    @Test
    void testDoGetAdminNullCommand() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("command")).thenReturn("listUsersX");
        when(request.getContextPath()).thenReturn("");
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        RequestDispatcher disp = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/access.denied.jsp"))
                .thenReturn(disp);

        AdminServlet servlet = new AdminServlet();

        servlet.doGet(request,response);

        verify(request.getSession()).setAttribute("errorHeader", "404");
    }

    @Test
    void testDoPostNullCommand() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("command")).thenReturn("listUsersX");
        when(request.getContextPath()).thenReturn("");
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        RequestDispatcher disp = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/access.denied.jsp"))
                .thenReturn(disp);

        AdminServlet servlet = new AdminServlet();

        servlet.doPost(request,response);

        verify(request.getSession()).setAttribute("errorHeader", "404");
    }

}