var itemsPerPage = 5;
var currentPGPage = 1;
var sortBy = "name";
var searchText = "";
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
    fetch('user?command=updateListUserBooks', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        body: JSON.stringify({
            currentPage: currentPGPage,
            items: itemsPerPage,
            sorting: sortBy,
            searching: searchText
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
    const table = document.getElementById('table')
    table.innerHTML = "";
    for (let i = 0; i < tx.length; i++) {
        const countStatus = tx[i].count>0 ? "" : "color: red";
        let row = `<div class="uk-animation-toggle">
        <div class="uk-card uk-card-default uk-card-body uk-align-center uk-animation-scale-up" id="tr_${tx[i].id}">
            <label>${tx[i].isbn}</label><br>
            <img class="uk-preserve-width uk-border-rectangle" 
                src="/images/books/${tx[i].image}" width="100" alt=""><br>
            <h3>${tx[i].name}</h3><br>
            <label class="uk-text-bold uk-text-uppercase">${tx[i].author}</label><br>
            <label>${tx[i].publisher}</label><br>
            <label>${tx[i].year}</label><br>
            <a uk-icon="icon: plus-circle; ratio: 1.5" style="${countStatus}" onclick="takeBookButton(${tx[i].id})"></a>
        </div>
        </div>`
        table.innerHTML += row;
    }
}

function takeBookButton(id) {
    UIkit.modal.confirm('This book will be booked. Are you sure?').then(function () {
        fetch('user?command=takeUserBook', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            body: JSON.stringify({
                bookId: id
            })
        }).then(response => response.json())
            .then(data => {
                if (data.status == 'OK') {
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
    }), function () {
        console.log('Canceling booking')
    }
}


function changeSort() {
    sortBy = $('#sortBooksOption').val();
    currentPGPage = 1;
    callPOSTRequest(1, 0);
}

function changeSearching() {
    searchText = $('#searchingText').val();
    currentPGPage = 1;
    callPOSTRequest(1, 0);
}

function callErrorAlert(message) {
    UIkit.modal.alert(message);
}
