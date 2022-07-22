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
    fetch('librarian?command=updateListLibrarianUsers', {
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
                <td><img class="uk-preserve-width uk-border-circle" 
                src="/images/users/${tx[i].photo}" width="40" alt=""></td>
                <td>${tx[i].number}</td>
                <td>${tx[i].firstName}</td>
                <td>${tx[i].lastName}</td>
                <td>${tx[i].dateOfBirth}</td>
                <td>${tx[i].phone}</td>
                </tr>`
        table.innerHTML += row;
    }
}

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
