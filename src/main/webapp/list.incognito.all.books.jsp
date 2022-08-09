<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title><fmt:message key="list_books.title"/></title>
    <script src="js/incognito.books.util.js"></script>

</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.incognito.jspf" %>
    <div>
        <input class="uk-input uk-width-1-4@s" type="text" id="searchingText" onchange="changeSearching()">
        <select class="uk-select uk-width-1-4@s" id="sortBooksOption" name="items" form-field onchange="changeSort()">
            <option value="isbn"><fmt:message key="list_books.sort.by_isbn"/></option>
            <option value="publisher"><fmt:message key="list_books.sort.by_publisher"/></option>
            <option value="author"><fmt:message key="list_books.sort.by_author"/></option>
            <option value="name" selected><fmt:message key="list_books.sort.by_name"/></option>
        </select>
        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>
    </div>
    <div>
        <ul class="uk-pagination" id="pagination">
        </ul>
    </div>
    <div class="uk-child-width-1-5@m uk-grid-small uk-grid-match" id="table" uk-grid>
        <div class="uk-align-center" uk-spinner></div>
    </div>
    <div id="userinfo" uk-modal>

    </div>
</div>

</body>
</html>