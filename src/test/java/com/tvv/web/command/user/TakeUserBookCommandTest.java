package com.tvv.web.command.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.BookService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.librarian.UpdateListLibrarianBooksCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class TakeUserBookCommandTest {

    private User assertUser;

    @BeforeEach
    void init() {
        assertUser = new User();
        assertUser.setId(1L);
        assertUser.setNumber("89000000");
        assertUser.setPassword("ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
        assertUser.setFirstName("UserFirstName");
        assertUser.setLastName("UserLastName");
        assertUser.setDateOfBirth(LocalDate.parse("2000-01-01"));
        assertUser.setPhone("+380638900000");
        assertUser.setPhoto("89000000.jpg");
        assertUser.setStatus("enabled");
        assertUser.setLocale("en");
        Role role = new Role();
        role.setId(3);
        role.setName("UserRole");
        role.setStatus("enabled");
        assertUser.setRole(role);
    }

    @Test
    void testExecutePost() throws IOException, ServletException, AppException {
        JsonObject assertJSON = new JsonObject();
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("currentUser")).thenReturn(assertUser);
        HttpServletRequest request = mock(HttpServletRequest.class);
        BufferedReader reader = new BufferedReader(
                new CharArrayReader("{\"bookId\":1}".toCharArray()));
        when(request.getSession()).thenReturn(session);
        when(request.getReader()).thenReturn(reader);
        Map<String, Object> jsonParameters = new HashMap<>();
        jsonParameters.put("bookId", 1);

        BookService bookService = mock(BookService.class);
        when(bookService.startRentBookByUserNumber(1,"89000000")).thenReturn(new JsonObject());

        TakeUserBookCommand command = new TakeUserBookCommand();
        command.init(bookService);
        command.executePost(request,response);
        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);
    }
}