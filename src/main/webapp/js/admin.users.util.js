let itemsPerPage=5;
let currentPGPage=1;
let sortBy="number";

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
    fetch('admin?command=updateListUser', {
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
        var row = `<tr id="tr_${tx[i].id}">
                <td><img class="uk-preserve-width uk-border-circle" 
                src="/images/users/${tx[i].photo}" width="40" alt=""></td>
                <td>${tx[i].number}</td>
                <td>${tx[i].firstName}</td>
                <td>${tx[i].lastName}</td>
                <td>${tx[i].dateOfBirth}</td>
                <td>${tx[i].phone}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="user"
                                    value="${tx[i].id}" onclick="changeStatusButton(${tx[i].id})">${userStatusButton}</button>
                </td>
                </tr>`
        table.innerHTML += row;
    }
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
                    var userStatusButton = data.user.status;
                    var newHtml = `
                <td><img class="uk-preserve-width uk-border-circle" src="images/users/${data.user.photo}" width="40"
                                 alt=""></td>
                <td>${data.user.number}</td>
                <td>${data.user.firstName}</td>
                <td>${data.user.lastName}</td>
                <td>${data.user.dateOfBirth}</td>
                <td>${data.user.phone}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="user"
                                    value="${data.user.id}" onclick="changeStatusButton(${data.user.id})">${userStatusButton}</button>
                `
                    $('#tr_' + data.user.id).html(newHtml);
                }
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });

}

function changeStatusButton(e) {
    UIkit.modal.confirm(javascript_admin_user_status_button).then(function () {
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
});
