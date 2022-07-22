package com.tvv.service;

import com.google.gson.JsonObject;
import com.tvv.db.dao.RoleDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.db.util.PageSettings;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private Map<String, Object> inputJSONParameter;
    private User assertUser;

    @BeforeEach
    void init() {
        inputJSONParameter = new HashMap<>();
        inputJSONParameter.put("currentPage", 1);
        inputJSONParameter.put("items", 5);
        inputJSONParameter.put("sorting", "number");

        assertUser = new User();
        assertUser.setId(1);
        assertUser.setNumber("87000001");
        assertUser.setPassword("ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
        assertUser.setFirstName("FirstName");
        assertUser.setLastName("LastName");
        assertUser.setDateOfBirth(LocalDate.parse("2000-01-01"));
        assertUser.setPhone("+380990000001");
        assertUser.setStatus("enabled");
        assertUser.setLocale("en");
        Role assertRole = new Role();
        assertRole.setId(3);
        assertRole.setName("user");
        assertRole.setStatus("enabled");
        assertUser.setRole(assertRole);


    }

    @Test
    void testUsersListPagination() throws AppException {
        List<User> assertUserList = new ArrayList<>();
        assertUserList.add(assertUser);
        UserDAO userDAO = mock(UserDAO.class);
        RoleDAO roleDAO = mock(RoleDAO.class);
        PageSettings pageSettings = new PageSettings();
        pageSettings.setPage(1);
        pageSettings.setSize(5);
        pageSettings.setSort("number");
        when(userDAO.findAllUsers(pageSettings,3))
                .thenReturn(assertUserList);

        UserService userService = new UserService();
        userService.init(userDAO, roleDAO);

        JsonObject result = userService.usersListPagination(inputJSONParameter, 3);
        String assertString = "{\"status\":\"OK\",\"page\":1,\"pages\":0,\"list\":[{\"id\":1,\"number\":\"87000001\"," +
                "\"firstName\":\"FirstName\",\"lastName\":\"LastName\",\"dateOfBirth\":\"2000-01-01\"," +
                "\"phone\":\"+380990000001\",\"status\":\"enabled\",\"role\":\"user\"}]}";
        assertEquals(result.toString(), assertString);

    }

    @Test
    void testGetUserInfoByNumber() throws AppException {
        UserDAO userDAO = mock(UserDAO.class);
        RoleDAO roleDAO = mock(RoleDAO.class);
        when(userDAO.findUserByNumber("87000001"))
                .thenReturn(assertUser);

        UserService userService = new UserService();
        userService.init(userDAO, roleDAO);

        JsonObject result = userService.getUserInfoByNumber("87000001");
        String assertString = "{\"status\":\"OK\",\"user\":{\"id\":1,\"number\":\"87000001\"," +
                "\"firstName\":\"FirstName\",\"lastName\":\"LastName\",\"dateOfBirth\":\"2000-01-01\"," +
                "\"phone\":\"+380990000001\",\"status\":\"enabled\",\"role\":\"user\"}}";
        assertEquals(result.toString(), assertString);
    }

    @Test
    void testFindUserById() throws AppException {
        User result = new User();
        result.setId(1);
        result.setNumber("87000001");
        result.setPassword("ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
        result.setFirstName("FirstName");
        result.setLastName("LastName");
        result.setDateOfBirth(LocalDate.parse("2000-01-01"));
        result.setPhone("+380990000001");
        result.setStatus("enabled");
        result.setLocale("en");
        Role assertRole = new Role();
        assertRole.setId(3);
        assertRole.setName("user");
        assertRole.setStatus("enabled");
        result.setRole(assertRole);

        UserDAO userDAO = mock(UserDAO.class);
        RoleDAO roleDAO = mock(RoleDAO.class);
        when(userDAO.findById(1))
                .thenReturn(result);

        UserService userService = new UserService();
        userService.init(userDAO, roleDAO);

        User testResult = userService.findUserById(1);
        assertEquals(testResult, assertUser);
    }

    @Test
    void testChangeStatusUserById() throws AppException {
        User result = new User();
        result.setId(1);
        result.setNumber("87000001");
        result.setPassword("ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
        result.setFirstName("FirstName");
        result.setLastName("LastName");
        result.setDateOfBirth(LocalDate.parse("2000-01-01"));
        result.setPhone("+380990000001");
        result.setStatus("enabled");
        result.setLocale("en");
        Role assertRole = new Role();
        assertRole.setId(3);
        assertRole.setName("user");
        assertRole.setStatus("disabled");
        result.setRole(assertRole);

        UserDAO userDAO = mock(UserDAO.class);
        RoleDAO roleDAO = mock(RoleDAO.class);
        when(userDAO.findById(1))
                .thenReturn(assertUser)
                .thenReturn(result);
        when(userDAO.update(assertUser))
                .thenReturn(true);

        UserService userService = new UserService();
        userService.init(userDAO, roleDAO);

        User testResult = userService.changeStatusUserById(1);
        assertEquals(testResult, result);
    }

    @Test
    void testChangeRoleUserById() throws AppException {
        User result = new User();
        result.setId(1);
        result.setNumber("87000001");
        result.setPassword("ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
        result.setFirstName("FirstName");
        result.setLastName("LastName");
        result.setDateOfBirth(LocalDate.parse("2000-01-01"));
        result.setPhone("+380990000001");
        result.setStatus("enabled");
        result.setLocale("en");
        Role assertRole = new Role();
        assertRole.setId(1);
        assertRole.setName("admin");
        assertRole.setStatus("enabled");
        result.setRole(assertRole);

        User assertUserLibrarian = new User();
        assertUserLibrarian.setId(1);
        assertUserLibrarian.setNumber("87000001");
        assertUserLibrarian.setPassword("ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
        assertUserLibrarian.setFirstName("FirstName");
        assertUserLibrarian.setLastName("LastName");
        assertUserLibrarian.setDateOfBirth(LocalDate.parse("2000-01-01"));
        assertUserLibrarian.setPhone("+380990000001");
        assertUserLibrarian.setStatus("enabled");
        assertUserLibrarian.setLocale("en");
        Role assertRoleLibrarian = new Role();
        assertRoleLibrarian.setId(2);
        assertRoleLibrarian.setName("librarian");
        assertRoleLibrarian.setStatus("enabled");
        assertUserLibrarian.setRole(assertRoleLibrarian);

        UserDAO userDAO = mock(UserDAO.class);
        RoleDAO roleDAO = mock(RoleDAO.class);
        when(roleDAO.findById(1))
                .thenReturn(assertRole);
        when(userDAO.findById(1))
                .thenReturn(assertUserLibrarian)
                .thenReturn(result);
        when(userDAO.update(result))
                .thenReturn(true);

        UserService userService = new UserService();
        userService.init(userDAO, roleDAO);

        User testResult = userService.changeRoleUserById(1, 1);
        assertEquals(testResult, result);
    }

    @Test
    void testGetUserFreeNumber() throws AppException {
        UserDAO userDAO = mock(UserDAO.class);
        RoleDAO roleDAO = mock(RoleDAO.class);
        when(userDAO.findFirstFreeNumber(3))
                .thenReturn("87000001");

        UserService userService = new UserService();
        userService.init(userDAO, roleDAO);

        String result = userService.getUserFreeNumber(3);
        String assertString = "87000001";
        assertEquals(result, assertString);
    }

}