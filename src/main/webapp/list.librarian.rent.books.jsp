<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title><fmt:message key="list_books.title"/></title>
    <script src="js/librarian.rentbooks.util.js"></script>

</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.librarian.jspf" %>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortBooksOption" name="items" form-field onchange="changeSort()">
            <option value="isbn"><fmt:message key="list_books.sort.by_isbn"/></option>
            <option value="startDate"><fmt:message key="list_books.sort.by_start_date"/></option>
            <option value="user"><fmt:message key="list_books.sort.by_user_number"/></option>
            <option value="name" selected><fmt:message key="list_books.sort.by_name"/></option>
        </select>

        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>

    </div>
    <div>
        <ul class="uk-pagination" id="pagination">
        </ul>
    </div>
    <div class="uk-grid" data-uk-grid-margin>
        <div class="uk-width-medium-1-1">
            <table class="uk-table uk-table-hover uk-table-middle uk-table-divider uk-table-striped">
                <thead>
                <tr>
                    <th class="uk-table-shrink"><fmt:message key="list_books.table.header.photo"/></th>
                    <th><fmt:message key="list_books.table.header.author"/></th>
                    <th><fmt:message key="list_books.table.header.name"/></th>
                    <th><fmt:message key="list_books.table.header.user_number"/></th>
                    <th><fmt:message key="list_books.table.header.start_date"/></th>
                    <th><fmt:message key="list_books.table.header.end_date"/></th>
                    <th><fmt:message key="list_books.table.header.status"/></th>
                    <th><fmt:message key="list_books.table.header.status_pay"/></th>
                    <th><fmt:message key="list_books.table.header.action"/></th>
                </tr>
                </thead>
                <tbody id="table">

                </tbody>
            </table>
        </div>
    </div>
    <div id="userinfo" uk-modal>

    </div>
</div>

</body>
<%@ include file="/WEB-INF/jspf/javascript.language.pack.jspf" %>
</html>