package com.tvv.web.command.librarian;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.service.BookService;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateListLibrarianBooksCommandTest {

    @Test
    void testExecutePost() throws IOException, ServletException, AppException {
        JsonObject assertJSON = new JsonObject();
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        HttpServletRequest request = mock(HttpServletRequest.class);
        BufferedReader reader = new BufferedReader(
                new CharArrayReader("{\"sorting\":\"name\",\"currentPage\":1,\"items\":5,\"searching\":\"book\"}".toCharArray()));
        when(request.getReader()).thenReturn(reader);
        Map<String, Object> jsonParameters = new HashMap<>();
        jsonParameters.put("sorting", "name");
        jsonParameters.put("currentPage", 1);
        jsonParameters.put("items", 5);
        jsonParameters.put("searching", "book");

        BookService bookService = mock(BookService.class);
        when(bookService.bookListPaginationWithSearch(jsonParameters)).thenReturn(new JsonObject());

        UpdateListLibrarianBooksCommand command = new UpdateListLibrarianBooksCommand();
        command.init(bookService);
        command.executePost(request,response);
        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);

    }
}