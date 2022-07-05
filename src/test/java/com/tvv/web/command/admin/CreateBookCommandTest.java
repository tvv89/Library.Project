package com.tvv.web.command.admin;

import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.BookService;
import com.tvv.service.dto.BookDTO;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateBookCommandTest {

    @Test
    void testExecutePostCorrect() throws IOException, ServletException, AppException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("isbn")).thenReturn("1234567890123");
        when(request.getParameter("author")).thenReturn("AuthorFirstName AuthorLastName");
        when(request.getParameter("genre")).thenReturn("BookGenre");
        when(request.getParameter("name")).thenReturn("The best book");
        when(request.getParameter("publisher")).thenReturn("Publisher books");
        when(request.getParameter("year")).thenReturn("2022");
        when(request.getParameter("count")).thenReturn("1");

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(0);
        bookDTO.setIsbn("1234567890123");
        bookDTO.setName("The best book");
        bookDTO.setPublisher("Publisher books");
        bookDTO.setYear("2022");
        bookDTO.setAuthor("AuthorFirstName AuthorLastName");
        bookDTO.setGenre("BookGenre");
        bookDTO.setImage("_blank.png");
        bookDTO.setCount(1);

        BookService bookService = mock(BookService.class);
        when(bookService.createBook(bookDTO)).thenReturn(true);

        CreateBookCommand command = new CreateBookCommand();
        command.init(bookService);
        command.executePost(request,response);
        verify(response, times(1)).sendRedirect("/admin.book.create.success.jsp");
    }

    @Test
    void testExecutePostIncorrect() throws IOException, ServletException, AppException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("isbn")).thenReturn("123456789012a");
        when(request.getParameter("author")).thenReturn("AuthorFirstName AuthorLastName");
        when(request.getParameter("genre")).thenReturn("BookGenre");
        when(request.getParameter("name")).thenReturn("The best book");
        when(request.getParameter("publisher")).thenReturn("Publisher books");
        when(request.getParameter("year")).thenReturn("2022");
        when(request.getParameter("count")).thenReturn("1");

        BookDTO bookDTO = null;

        BookService bookService = mock(BookService.class);
        when(bookService.createBook(bookDTO)).thenReturn(true);

        CreateBookCommand command = new CreateBookCommand();
        command.init(bookService);
        command.executePost(request,response);
        verify(response, times(1)).sendRedirect("admin.book.create.fail.jsp");
    }

    @Test
    void executeGet() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/access.denied.jsp")).thenReturn(dispatcher);
        new CreateBookCommand().executeGet(request,response);
        verify(request, times(1)).getRequestDispatcher("/access.denied.jsp");

    }
}