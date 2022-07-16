var itemsPerPage=5;
var currentPGPage=1;
var sortBy="name";

function callPOSTRequest(option, parameter) {
    var items = parseInt($('#itemsPerPage').val());
    switch (option){
        case 1:
            break;
        case 2:
            currentPGPage = currentPGPage + parameter;
            break;
        case 3:
            currentPGPage = 1;
            itemsPerPage = items;
            break;
    }
    fetch('admin?command=updateListBook', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        body: JSON.stringify({currentPage: currentPGPage,
            items: itemsPerPage,
            sorting: sortBy})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                createPagination(data.page, data.pages);
                createTable(data.list);
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });
}

function createTable(tx) {
    var table = document.getElementById('table')
    table.innerHTML = "";
    for (var i = 0; i < tx.length; i++) {
        var row = `<tr id="tr_${tx[i].id}">
                <td><img class="uk-preserve-width uk-border-rectangle" 
                src="/images/books/${tx[i].image}" width="40" alt=""></td>
                <td>${tx[i].isbn}</td>
                <td>${tx[i].author}</td>
                <td>${tx[i].name}</td>
                <td>${tx[i].publisher}</td>
                <td>${tx[i].year}</td>
                <td>${tx[i].count}</td>
                <td><a uk-icon="icon: pencil; ratio: 1.5" onclick="editBookButton(${tx[i].id})"></a>
                <a uk-icon="icon: image; ratio: 1.5" onclick="editImageButton(${tx[i].id})"></a>
                <a uk-icon="icon: trash; ratio: 1.5" onclick="deleteBookButton(${tx[i].id})"></a>
                </td>
                </tr>`
        table.innerHTML += row;
    }
}

function editBookButton(bookId) {
    fetch('admin?command=infoBook', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        body: JSON.stringify({id: bookId})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                document.getElementById("upd-current-id").value = data.book.id;
                document.getElementById("upd-image-book").value = data.book.image;
                document.getElementById("upd-isbn").value = data.book.isbn;
                document.getElementById("upd-author").value = data.book.author;
                document.getElementById("upd-name").value = data.book.name;
                document.getElementById("upd-publisher").value = data.book.publisher;
                document.getElementById("upd-year").value = data.book.year;
                document.getElementById("upd-genre").value = data.book.genre;
                document.getElementById("upd-count").value = data.book.count;
                document.getElementById("updateDialogBook").click();
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });

}

function editImageButton(bookId) {
    document.getElementById("book-image-id").value = bookId;
    document.getElementById("updateDialogImage").click();
}

function deleteBookButton(id) {
    UIkit.modal.confirm("Book will be deleted. Are you sure?").then(function () {
        fetch('admin?command=deleteBook', {
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            method: 'POST',
            body: JSON.stringify({
                bookId: id
            })
        }).then(response => response.json())
            .then(data => {
                if (data.status == 'OK') {
                    callPOSTRequest(1, 0);
                    UIkit.notification({
                        message: "Book was deleted",
                        status: 'success',
                        timeout: 2000
                    });
                } else callErrorAlert(data.message);
            })
            .catch(err => {
                callErrorAlert(err);
            });
        console.log('Book was deleted')
    }, function () {
        console.log('Canceling enable')
    });
};

function changeSort(){
    sortBy = $('#sortBooksOption').val();
    currentPGPage=1;
    callPOSTRequest(1,0);
}

function callErrorAlert(message){
    UIkit.modal.alert(message);
}

window.addEventListener('DOMContentLoaded', (event) => {
    callPOSTRequest(1,0);
    document.getElementById("bookForm").onsubmit = enableSubmitButton;
    document.getElementById("updateBookForm").onsubmit = enableSubmitButton;
});
