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
    UIkit.modal.confirm("Book will be give for user. Are you sure?").then(function () {
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
                            message: "Rent book was updated",
                            status: 'success',
                            timeout: 2000
                        })
                    };
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
    UIkit.modal.confirm("Did user pay fine?").then(function () {
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
                        message: "Rent book was updated",
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
    UIkit.modal.confirm("User return this book to library").then(function () {
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
                        message: 'Book was returned to Library',
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
        var buttonPayStatus = book.status != "need pay" ? "uk-disabled" : "uk-enabled";
        var buttonGiveStatus = book.startDate != "" ? "uk-disabled" : "uk-enabled";
        var buttonTakeStatus = "uk-enabled";
        if (buttonPayStatus == "uk-enabled" || buttonGiveStatus == "uk-enabled") buttonTakeStatus = "uk-disabled";

        var buttonPayColor = buttonPayStatus == "uk-enabled" ? "color:red" : "color:#D3D3D3";
        var buttonGiveColor = buttonGiveStatus == "uk-enabled" ? "" : "color:#D3D3D3";
        var buttonTakeColor = buttonTakeStatus == "uk-enabled" ? "" : "color:#D3D3D3";
        var row = `<td><img class="uk-preserve-width uk-border-rectangle" 
                src="/images/books/${book.image}" width="40" alt=""></td>
                <td>${book.author}</td>
                <td>${book.name}</td>
                <td><span class="uk-label uk-label-success" 
                    onclick="userInfo(${book.number})" uk-toggle="target: #userinfo">
                    ${book.number}</span></td>
                <td>${book.startDate}</td>
                <td>${book.endDate}</td>
                <td>${book.status}</td>
                <td>${book.statusPay}</td>
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
    var usermodal = document.getElementById('userinfo')
    usermodal.innerHTML = "";
    fetch('librarian?command=infoUser', {
        method: 'POST',
        body: JSON.stringify({userNumber: id.toString()})
    }).then(response => response.json())
        .then(data => {
            if (data.status == 'OK') {
                var row = `<div class="uk-modal-dialog uk-modal-body">
        <h2 class="uk-modal-title">Information</h2>
        <button class="uk-modal-close-default" type="button" uk-close></button>
        <img class="uk-preserve-width uk-border-circle" 
        src="images/users/${data.user.photo}" width="100" alt=""><br>
        <span class="uk-label">User</span><br>
        <label>Number: ${data.user.number}</label><br>
        <label>First name: ${data.user.firstName}</label><br>
        <label>Last name: ${data.user.lastName}</label><br>
        <label>Date of birth: ${data.user.dateOfBirth}</label><br>
        <label>Phone: ${data.user.phone}</label><br>
    </div>`
                usermodal.innerHTML += row;
            } else callErrorAlert(data.message);
        });

};
