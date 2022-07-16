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
    fetch('start?command=updateListLibrarianBooks', {
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
    var table = document.getElementById('table')
    table.innerHTML = "";
    for (var i = 0; i < tx.length; i++) {
        var countStatus = tx[i].count>0 ? "" : "color: red";
        var row = `<div>
        <div class="uk-card uk-card-default uk-card-body uk-align-center" id="tr_${tx[i].id}">
            <img class="uk-preserve-width uk-border-rectangle" 
                src="/images/books/${tx[i].image}" width="100" alt=""><br>
                <p class="uk-article-meta">${tx[i].isbn}</p>
            <h3>${tx[i].name}</h3><br>
            <label class="uk-text-bold uk-text-uppercase">${tx[i].author}</label><br>
            <label>${tx[i].publisher}</label><br>
            <label>${tx[i].year}</label>
        </div>
    </div>`
        table.innerHTML += row;
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
