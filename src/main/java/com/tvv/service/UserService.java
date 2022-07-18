package com.tvv.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.RoleDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.db.impl.RoleDAOImpl;
import com.tvv.db.impl.UserDAOImpl;
import com.tvv.db.util.PageSettings;
import com.tvv.service.exception.AppException;
import com.tvv.service.dto.UserDTO;
import com.tvv.service.util.StringHash;
import com.tvv.web.util.UtilCommand;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class);
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private String local;
    private ResourceBundle message;

    public UserService() {
        this.userDAO = new UserDAOImpl();
        this.roleDAO = new RoleDAOImpl();
        this.local = "en";
        Locale locale = new Locale(local);
        this.message = ResourceBundle.getBundle("resources", locale);
    }

    public void init(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    public void initLanguage(String local) {
        this.local = local;
        Locale locale = new Locale(this.local);
        this.message = ResourceBundle.getBundle("resources", locale);
    }

    public JsonObject usersListPagination(Map<String, Object> jsonParameters, long role) {
        log.trace("Start usersListPagination method in " + this.getClass().getName());
        JsonObject innerObject = new JsonObject();
        Optional<Integer> currentPage = null;
        Optional<Integer> itemPerPage = null;
        Optional<String> sorting = null;
        currentPage = Optional.of((Integer) jsonParameters.get("currentPage"));
        itemPerPage = Optional.of((Integer) jsonParameters.get("items"));
        sorting = Optional.of((String) jsonParameters.get("sorting"));
        PageSettings pageSettings = new PageSettings();
        if (itemPerPage.orElse(5) <= 0) {
            pageSettings.setPage(1);
            pageSettings.setSize(Integer.MAX_VALUE);
        } else {
            pageSettings.setPage(currentPage.orElse(1));
            pageSettings.setSize(itemPerPage.orElse(5));
        }

        try {
            String sortColumn;
            switch (sorting.orElse("number")) {
                case "lastName":
                    sortColumn = "lastName";
                    break;
                default:
                    sortColumn = "number";
                    break;
            }
            pageSettings.setSort(sortColumn);
            log.debug("Pagination parameter: " + pageSettings.toString());
            List<User> userList = userDAO.findAllUsers(pageSettings, role);
            List<UserDTO> list = userList.stream()
                    .map(user -> new UserDTO(user))
                    .collect(Collectors.toList());
            /**
             * Select and show user list
             */
            long pages = (long) Math.ceil((double) userDAO.userCount(role) / pageSettings.getSize());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(pageSettings.getPage()));
            innerObject.add("pages", new Gson().toJsonTree(pages));
            innerObject.add("list", new Gson().toJsonTree(list));

        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        log.debug("JSON to send: " + innerObject);
        log.trace("End usersListPagination method");
        return innerObject;
    }

    public JsonObject getUserInfoByNumber(String number) {
        JsonObject innerObject = new JsonObject();
        try {
            log.debug("Start user info function");
            User user = userDAO.findUserByNumber(number);
            if (user != null) {
                UserDTO userDTO = new UserDTO(user);
                /**
                 * Select and show user
                 */
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("user", new Gson().toJsonTree(userDTO));
            } else {
                innerObject = UtilCommand.errorMessageJSON(message.getString("error.json.user_service.not_found"));
            }
        } catch (AppException e) {
            innerObject = UtilCommand.errorMessageJSON(e.getMessage());
        }
        log.debug("End user info function");
        return innerObject;
    }

    public User findUserById(long id) throws AppException {
        return userDAO.findById(id);
    }

    public User changeStatusUserById(long id) throws AppException {
        User user = findUserById(id);
        String newStatus = user.getStatus().equalsIgnoreCase("enabled") ? "disabled" : "enabled";
        user.setStatus(newStatus);
        userDAO.update(user);
        return user;
    }

    public User createUser(Map<String, String> userData, String role, String currentLanguage) throws AppException {
        User user = new User();
        user.setId(0);
        user.setNumber(userData.get("login"));
        String hashPassword = StringHash.getHashString(userData.get("password"));
        user.setPassword(hashPassword);
        user.setFirstName(userData.get("firstName"));
        user.setLastName(userData.get("lastName"));
        user.setDateOfBirth(LocalDate.parse(userData.get("dateOfBirth")));
        user.setPhone(userData.get("phone"));
        user.setStatus("enabled");
        user.setPhoto(userData.get("photoFile"));
        Role roleForUser = roleDAO.findById(Long.parseLong(role));
        user.setRole(roleForUser);
        user.setLocale(userData.get("locale"));
        try {
            if (!userDAO.create(user)) return null;
        } catch (AppException e) {
            throw new AppException("Fail to create new user", e);
        }
        return user;
    }

    public void updateLocalUserById(long id, String currentLanguage) throws AppException {
        userDAO.updateLocale(id, currentLanguage);
    }

    public String getUserFreeNumber(long roleId) {
        String result;
        try {
            result = userDAO.findFirstFreeNumber(roleId);
        } catch (AppException e) {
            result = "";
        }
        return result;
    }

    public boolean updateUser(UserDTO userDTO) throws AppException {
        User user = userDAO.findById(userDTO.getId());
        if (user.getFirstName()!=userDTO.getFirstName())
            user.setFirstName(userDTO.getFirstName());
        if (user.getLastName()!=userDTO.getLastName())
            user.setLastName(userDTO.getLastName());
        if (user.getDateOfBirth().toString()!=userDTO.getDateOfBirth())
            user.setDateOfBirth(LocalDate.parse(userDTO.getDateOfBirth()));
        if (user.getPhone()!=userDTO.getPhone())
            user.setPhone(userDTO.getPhone());

        return userDAO.update(user);
    }

    public boolean updateUserPassword(long id, String password) throws AppException {
        User user = userDAO.findById(id);
        user.setPassword(StringHash.getHashString(password));
        return userDAO.update(user);
    }

    public boolean updateImage(long id, String image) {
        if (id <= 0 || image == null) return false;
        boolean result;
        try {
            result = userDAO.updateImageBookById(id, image);
        } catch (AppException ex) {
            log.error("Can not update image " + ex.getMessage());
            result = false;
        }
        return result;
    }
}
