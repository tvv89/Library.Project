<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title><fmt:message key="list_books.title"/></title>
    <script src="js/admin.books.util.js"></script>

</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.admin.jspf" %>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortBooksOption" name="items" form-field onchange="changeSort()">
            <option value="isbn"><fmt:message key="list_books.sort.by_isbn"/></option>
            <option value="publisher"><fmt:message key="list_books.sort.by_publisher"/></option>
            <option value="author"><fmt:message key="list_books.sort.by_author"/></option>
            <option value="name" selected><fmt:message key="list_books.sort.by_name"/></option>
        </select>

        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>
        <button class="uk-button uk-button-primary" id="openDialogBook" href="#modal-add-book"
                uk-toggle><fmt:message key="list_books.add_book"/>
        </button>
        <button class="uk-hidden" id="updateDialogBook" href="#modal-update-book"
                uk-toggle>
        </button>
        <button class="uk-hidden" id="updateDialogImage" href="#modal-update-image"
                uk-toggle>
        </button>
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
                    <th><fmt:message key="list_books.table.header.isbn"/></th>
                    <th><fmt:message key="list_books.table.header.author"/></th>
                    <th><fmt:message key="list_books.table.header.name"/></th>
                    <th><fmt:message key="list_books.table.header.publisher"/></th>
                    <th><fmt:message key="list_books.table.header.year"/></th>
                    <th><fmt:message key="list_books.table.header.count"/></th>
                    <th><fmt:message key="list_books.table.header.action"/></th>
                </tr>
                </thead>
                <tbody id="table">

                </tbody>
            </table>
        </div>
    </div>
    <%@ include file="/WEB-INF/jspf/create.form.book.jspf" %>
    <%@ include file="/WEB-INF/jspf/update.form.book.jspf" %>
    <%@ include file="/WEB-INF/jspf/update.form.image.jspf" %>
</div>
</body>
<%@ include file="/WEB-INF/jspf/javascript.language.pack.jspf" %>
</html>