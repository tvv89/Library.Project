<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title><fmt:message key="fail.user.all.header"/></title>
    <link rel="stylesheet" href="css/uikit.css">
</head>
<body>
<div></div>
<div class="uk-align-center uk-position-center">
    <img class="uk-preserve-width uk-border-pill uk-align-center"
         src="images/fail.png"
         width="300" alt="">
    <h1 class="uk-align-center"><fmt:message key="fail.user.all.header"/></h1>
    <p class="uk-align-center"><fmt:message key="fail.user.all.message"/></p>
    <button class="uk-button uk-button-primary"
            onclick="window.location.href='/start'">
        <fmt:message key="fail.book.button"/></button>
</div>
</body>
</html>
