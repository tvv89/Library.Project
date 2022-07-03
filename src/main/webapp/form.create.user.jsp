<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html class="uk-height-1-1">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="form_create_user.title"/></title>
    <link rel="stylesheet" href="css/uikit.min.css">
    <script src="js/uikit.min.js"></script>
    <script src="js/uikit.js"></script>
    <script src="js/uikit-icons.js"></script>
    <script src="js/user.create.util.js"></script>
</head>

<body class="uk-height-1-2 uk-align-center">
<div class="uk-align-left@m uk-text-center uk-height-1-1">
    <h1><fmt:message key="form_create_user.user.title"/></h1>
    <form class="uk-form-horizontal" id="form-create-user" method="post" action="start" enctype="multipart/form-data">
        <input type="hidden" name="command" value="createUser"/>

        <div uk-form-custom="target: true">
            <input type="file" id="photo-file" name="photo-file" accept="image/jpeg">
            <input class="uk-input uk-form-width-large" type="text"
                   placeholder="<fmt:message key="form_create_user.user.photo"/>" disabled>
        </div>
        <div class="uk-margin">
            <label class="uk-form-label" for="login"><fmt:message key="form_create_user.user.login"/></label>
            <div class="uk-form-controls">
                <input class="uk-input uk-form-width-large uk-disabled"
                       id="login" name="login" type="text" value="${number}" form-field>
            </div>
        </div>
        <div class="uk-margin">
            <label class="uk-form-label" for="password"><fmt:message key="form_create_user.user.password"/></label>
            <div class="uk-form-controls">
                <input class="uk-input uk-form-width-large" id="password" name="password" type="password" form-field
                       form-field-regex="^[A-Za-z0-9]+$"
                       form-field-regex-error="Password must have only characters and numbers">
                <p id="password-error"></p>
            </div>
            <div class="uk-margin">
                <label class="uk-form-label" for="first-name"><fmt:message
                        key="form_create_user.user.first_name"/></label>
                <div class="uk-form-controls">
                    <input class="uk-input uk-form-width-large" id="first-name" name="first-name" type="text" form-field
                           form-field-regex="^[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюяёЁъЪ]+$"
                           form-field-regex-error="Name must have only characters">
                    <p id="first-name-error"></p>
                </div>
            </div>
            <div class="uk-margin">
                <label class="uk-form-label" for="last-name"><fmt:message
                        key="form_create_user.user.last_name"/></label>
                <div class="uk-form-controls">
                    <input class="uk-input uk-form-width-large" id="last-name" name="last-name" type="text" form-field
                           form-field-regex="^[0-9A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюяёЁъЪ]+$"
                           form-field-regex-error="Name must have only characters">
                    <p id="last-name-error"></p>
                </div>
            </div>
            <div class="uk-margin">
                <label class="uk-form-label" for="date-of-birth"><fmt:message
                        key="form_create_user.user.date_of_birth"/></label>
                <div class="uk-form-controls">
                    <input type="date" id="date-of-birth" name="date-of-birth" form-field>
                </div>
            </div>
            <div class="uk-margin">
                <label class="uk-form-label" for="phone"><fmt:message key="form_create_user.user.phone"/></label>
                <div class="uk-form-controls">
                    <input class="uk-input uk-form-width-large" id="phone" name="phone" type="text" form-field
                           form-field-regex="^[+]{1}([0-9]){12}$"
                           form-field-regex-error="Incorrect phone">
                    <p id="year-error"></p>
                </div>
            </div>
            <div class="uk-margin">
                <label class="uk-form-label" for="locale"><fmt:message key="form_create_user.user.locale"/></label>
                <div class="uk-form-controls">
                    <select class="uk-select" id="locale" name="locale" form-field>
                        <option value="en"><fmt:message key="form_create_user.user.locale.english"/></option>
                        <option value="uk"><fmt:message key="form_create_user.user.locale.ukrainian"/></option>
                    </select>
                </div>
            </div>

            <div>
                <input class="uk-width-1-1 uk-button uk-button-primary uk-button-default" type="submit"
                       id="create-user-submit" name="create-user-submit"
                       value="<fmt:message key="form_create_user.user.submit"/>"/>
            </div>
    </form>
</div>
</div>

</body>
<%@ include file="/WEB-INF/jspf/javascript.language.pack.jspf" %>
</html>