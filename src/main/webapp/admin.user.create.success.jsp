<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title><fmt:message key="success.user.header"/></title>
    <link rel="stylesheet" href="css/uikit.css">
</head>
<body>
<div></div>
<div class="uk-align-center uk-position-center">
    <img class="uk-preserve-width uk-border-pill uk-align-center"
         src="images/success.png"
         width="300" alt="">
    <h1 class="uk-align-center"><fmt:message key="success.user.header"/></h1>
    <p class="uk-align-center"><fmt:message key="success.user.message"/></p>
    <button class="uk-button uk-button-primary"
            onclick="window.location.href='/admin?command=listLibrarians'">
        <fmt:message key="success.user.button"/></button>
</div>
</body>
</html>
