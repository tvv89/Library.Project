var itemsPerPage = 5;
var currentPGPage = 1;
var sortBy = "name";
window.addEventListener('DOMContentLoaded', (event) => {
    callPOSTRequest(1, 0);
});

function callPOSTRequest(option, parameter) {
    var items = parseInt($('#itemsPerPage').val());
    switch (option) {
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
    fetch('user?command=updateUserRentBook', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        body: JSON.stringify({
            currentPage: currentPGPage,
            items: itemsPerPage,
            sorting: sortBy
        })
    }).then(response => response.json())
        .then(data => {
            if (data.status == 'OK') {
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
        var row = `<tr id="tr_${tx[i].id}">` +
            rentBookRow(tx[i]) +
            `</tr>`
        table.innerHTML += row;
    }
}

function rentBookRow(book) {
        var buttonPayColor = book.status == "need pay" ? "color:red" : "color:#D3D3D3";
        var row = `<td><img class="uk-preserve-width uk-border-rectangle" 
                src="/images/books/${book.image}" width="40" alt=""></td>
                <td>${book.author}</td>
                <td>${book.name}</td>
                <td>${book.startDate}</td>
                <td>${book.endDate}</td>
                <td>${book.status}</td>
                <td>
                    <a uk-icon="icon: bell; ratio: 1.5" style="${buttonPayColor}"></a>
                </td>`
        return row;
}


function changeSort() {
    sortBy = $('#sortBooksOption').val();
    currentPGPage = 1;
    callPOSTRequest(1, 0);
}

function callErrorAlert(message) {
    UIkit.modal.alert(message);
}