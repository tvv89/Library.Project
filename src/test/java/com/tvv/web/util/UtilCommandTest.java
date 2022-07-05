package com.tvv.web.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.service.exception.AppException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilCommandTest {

    @Test
    void testSendJSONData() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        String str = "JSON string";
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("string", new Gson().toJsonTree(str));
        String sendData = "{\"string\":\"JSON string\"}";

        UtilCommand.sendJSONData(response, jsonObject);
        verify(out).print(sendData);

    }

    @Test
    void testParseRequestJSON() throws IOException, AppException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        BufferedReader reader = new BufferedReader(
                new CharArrayReader("{\"id\":1,\"string1\":\"string1\",\"string2\":\"string2\"}".toCharArray()));
        when(request.getReader()).thenReturn(reader);

        JSONObject assertJson = new JSONObject("{\"id\":1,\"string1\":\"string1\",\"string2\":\"string2\"}");
        Map<String, Object> result = UtilCommand.parseRequestJSON(request);
        Map<String, Object> assertMap = assertJson.toMap();

        assertAll(
                ()->assertEquals(assertMap.size(), result.size()),
                ()->assertEquals(assertMap.get("id"), result.get("id")),
                ()->assertEquals(assertMap.get("string1"), result.get("string1")),
                ()->assertEquals(assertMap.get("string2"), result.get("string2")));

    }

    @Test
    void testErrorMessageJSON() {
        String assertJSON = "{\"status\":\"ERROR\",\"message\":\"my own message\"}";
        JsonObject result = UtilCommand.errorMessageJSON("my own message");
        assertEquals(assertJSON, result.toString());
    }

    @Test
    void testGetLocaleEN() throws IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("currentLanguage")).thenReturn("en");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        ResourceBundle resourceBundle = UtilCommand.getLocale(request);
        assertEquals("Add book", resourceBundle.getString("book_create_form.title"));
    }

    @Test
    void testGetLocaleUK() throws IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("currentLanguage")).thenReturn("uk");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        ResourceBundle resourceBundle = UtilCommand.getLocale(request);
        assertEquals("Додати книгу", resourceBundle.getString("book_create_form.title"));
    }

    @Test
    void testGetLocaleNull() throws IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("currentLanguage")).thenReturn(null);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        ResourceBundle resourceBundle = UtilCommand.getLocale(request);
        assertEquals("Add book", resourceBundle.getString("book_create_form.title"));
    }
}