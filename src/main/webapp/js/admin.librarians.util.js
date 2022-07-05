var itemsPerPage=5;
var currentPGPage=1;
var sortBy="number";

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
    fetch('admin?command=updateListLibrarians', {
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
        var userStatusButton = tx[i].status;
        var row = `<tr id="tr_${tx[i].id}">`
                + loadRow(tx[i]) +
                `</tr>`
        table.innerHTML += row;
    }
}

function loadRow(data) {
    var userStatusButton = data.status;
    var row = `
                <td><img class="uk-preserve-width uk-border-circle" 
                src="/images/users/${data.photo}" width="40" alt=""></td>
                <td>${data.number}</td>
                <td>${data.firstName}</td>
                <td>${data.lastName}</td>
                <td>${data.dateOfBirth}</td>
                <td>${data.phone}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="user"
                                    value="${data.id}" onclick="changeStatusButton(${data.id})">${userStatusButton}</button>
                </td>
                `
    return row;

}

function changeUserStatus(id) {
    fetch('admin?command=statusUser', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({userId: id})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                if ($('#tr_' + data.user.id).length) {
                    let newHtml = loadRow(data.user);
                    $('#tr_' + data.user.id).html(newHtml);
                }
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });

}

function changeStatusButton(e) {
    UIkit.modal.confirm("Are you sure to change user status?").then(function () {
        changeUserStatus(e);
        console.log('User is enabled')
    }, function () {
        console.log('Canceling enable')
    });
};


function changeSort(){
    sortBy = $('#sortUsersOption').val();
    currentPGPage=1;
    callPOSTRequest(1,0);
}

function callErrorAlert(message){
    UIkit.modal.alert(message);
}


window.addEventListener('DOMContentLoaded', (event) => {
    callPOSTRequest(1,0);
    document.getElementById("librarianForm").onsubmit = enableSubmitButton;
});


