let itemsPerPage = 5;
let currentPGPage = 1;
let sortBy = "name";

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
    fetch('librarian?command=updateRentBook', {
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

function giveBook(tableId) {
    UIkit.modal.confirm(javascript_librarian_rent_book_give_book).then(function () {
        fetch('librarian?command=giveBookForUser', {
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            method: 'POST',
            body: JSON.stringify({
                id: tableId,
            })
        }).then(response => response.json())
            .then(data => {
                if (data.status == 'OK') {
                    if ($('#tr_' + data.book.id).length) {
                        var row = rentBookRow(data.book);
                        $('#tr_' + data.book.id).html(row);
                        UIkit.notification({
                            message: `${data.message}`,
                            status: 'success',
                            timeout: 2000
                        })
                    }
                    ;
                } else callErrorAlert(data.message);
            })
            .catch(err => {
                callErrorAlert(err);
            });

        console.log('Book was updated')
    }, function () {
        console.log('Canceling')
    });
}

function payFine(tableId) {
    UIkit.modal.confirm(javascript_librarian_rent_book_pay_fine).then(function () {
        fetch('librarian?command=payFineBookForUser', {
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            method: 'POST',
            body: JSON.stringify({
                id: tableId,
            })
        }).then(response => response.json())
            .then(data => {
                if (data.status == 'OK') {
                    if ($('#tr_' + data.book.id).length) {
                        var row = rentBookRow(data.book);
                        $('#tr_' + data.book.id).html(row);
                    }
                    UIkit.notification({
                        message: `${data.message}`,
                        status: 'success',
                        timeout: 2000
                    });
                } else callErrorAlert(data.message);
            })
            .catch(err => {
                callErrorAlert(err);
            });

        console.log('Book was updated')
    }, function () {
        console.log('Canceling')
    });
}

function takeBook(tableId) {
    UIkit.modal.confirm(javascript_librarian_rent_book_return_button).then(function () {
        fetch('librarian?command=returnBookToLibrary', {
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            method: 'POST',
            body: JSON.stringify({
                id: tableId
            })
        }).then(response => response.json())
            .then(data => {
                if (data.status == 'OK') {
                    callPOSTRequest(1, 0);
                    UIkit.notification({
                        message: `${data.message}`,
                        status: 'success',
                        timeout: 2000
                    });
                } else callErrorAlert(data.message);
            })
            .catch(err => {
                callErrorAlert(err);
            });
        console.log('Payment is deleted')
    }, function () {
        console.log('Canceling enable')
    });
}

function rentBookRow(book) {
    const buttonPayStatus = book.status != "need pay" ? "uk-disabled" : "uk-enabled";
    const buttonGiveStatus = book.startDate != "" ? "uk-disabled" : "uk-enabled";
    let buttonTakeStatus = "uk-enabled";
    if (buttonPayStatus == "uk-enabled" || buttonGiveStatus == "uk-enabled") buttonTakeStatus = "uk-disabled";

    const buttonPayColor = buttonPayStatus == "uk-enabled" ? "color:red" : "color:#D3D3D3";
    const buttonGiveColor = buttonGiveStatus == "uk-enabled" ? "" : "color:#D3D3D3";
    const buttonTakeColor = buttonTakeStatus == "uk-enabled" ? "" : "color:#D3D3D3";
    let row = `<td><img class="uk-preserve-width uk-border-rectangle" 
                src="/images/books/${book.image}" width="40" alt=""></td>
                <td>${book.author}</td>
                <td>${book.name}</td>
                <td><span class="uk-label uk-label-success" 
                    onclick="userInfo(${book.number})" uk-toggle="target: #userinfo">
                    ${book.number}</span></td>
                <td>${book.startDate}</td>
                <td>${book.endDate}</td>
                <td><a uk-icon="icon: ${bookStatus(book.status)}; ratio: 1.5" style="${buttonPayColor}"</a></td>
                <td><a uk-icon="icon: ${payStatus(book.statusPay)}; ratio: 1.5"</a></td>
                <td><a uk-icon="icon: sign-out; ratio: 1.5" class="${buttonGiveStatus}" 
                    style="${buttonGiveColor}" onclick="giveBook(${book.id})"></a>
                <a uk-icon="icon: bell; ratio: 1.5" class="${buttonPayStatus}" 
                    style="${buttonPayColor}" onclick="payFine(${book.id})"></a>
                <a uk-icon="icon: future; ratio: 1.5" class="${buttonTakeStatus}"  
                    style="${buttonTakeColor}" onclick="continueBook(${book.id})"></a>
                <a uk-icon="icon: sign-in; ratio: 1.5" class="${buttonTakeStatus}" 
                    style="${buttonTakeColor}"  onclick="takeBook(${book.id})"></a>
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

function userInfo(id) {
    fetch('librarian?command=infoUser', {
        method: 'POST',
        body: JSON.stringify({userNumber: id.toString()})
    }).then(response => response.json())
        .then(data => {
            if (data.status == 'OK') {
                document.getElementById("user-photo").src = `images/users/${data.user.photo}`;
                document.getElementById("user-number").innerHTML = `${data.user.number}`;
                document.getElementById("user-fn").innerHTML = `${data.user.firstName}`;
                document.getElementById("user-ln").innerHTML = `${data.user.lastName}`;
                document.getElementById("user-dob").innerHTML = `${data.user.dateOfBirth}`;
                document.getElementById("user-phone").innerHTML = `${data.user.phone}`;
            } else callErrorAlert(data.message);
        });

}

window.addEventListener('DOMContentLoaded', (event) => {
    callPOSTRequest(1, 0);
});
