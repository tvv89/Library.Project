function changeLanguage(user) {
    var language = $('#languageSelect').val();

    fetch(user+'?command=language', {
        method: 'POST',
        body: JSON.stringify({language: language})
    })
        .then(data =>  {
            UIkit.notification({message: 'Language is changed',
                status: 'success',
                timeout: 2000});
            window.location.reload();
        })
        .catch(err => {
            callErrorAlert(err.toString());
        });
}

function createPagination(page,pages) {
    var paginat = document.getElementById('pagination')
    paginat.innerHTML = "";
    var row = `<li class="uk-margin-small-right ${page == 1 ? 'uk-disabled' : ''}" id="previous-page" onclick="callPOSTRequest(2,-1)">
                            <a><span class="uk-margin-small-right" uk-pagination-previous></span>-----</a>
                        </li>
                        <li class="uk-margin-small uk-align-center"> ${page} / ${pages} </li>
                        <li class="uk-margin-small-left ${page == pages ? 'uk-disabled' : ''}" id="next-page" onclick="callPOSTRequest(2,1)">
                            <a>-----<span class="uk-margin-small-left" uk-pagination-next></span></a>
                        </li>`
    paginat.innerHTML += row;
}

function bookStatus(statusOfBook) {
    let iconStatus;
    switch (statusOfBook){
        case "reading":
            iconStatus = "future";
            break;
        case "booked":
            iconStatus = "cart";
            break;
        case "need pay":
            iconStatus = "credit-card";
            break;
        default:
            iconStatus = "warning";
            break;
    }
    return iconStatus;
}

function payStatus(statusOfBook) {
    return statusOfBook=="paid" ? "check" : "";
}

function enableSubmitButton(e) {
    const form = e.target;
    const submitButton = form.querySelector('input[type="submit"]');
    submitButton.disabled = true;
    try {
        const hasError = formFieldsRegexCheck(form);
        if (hasError) {
            submitButton.disabled = false;
        }
        return !hasError;
    } catch {
        return false;
    }
}

function formFieldsRegexCheck(form) {
    const fieldsForRegexCheck = form.querySelectorAll('[form-field-regex]');
    let hasError = false;
    fieldsForRegexCheck.forEach(function(input) {
        const errorMessageElement = input.parentElement.querySelector(`#${input.name}-error`);
        const regexToCheck = new RegExp(input.getAttribute('form-field-regex'));
        if (!regexToCheck.test(input.value)) {
            input.classList.add('uk-form-danger');
            hasError = true;
            errorMessageElement.innerText = input.getAttribute('form-field-regex-error');
        } else {
            input.classList.remove('uk-form-danger');
            errorMessageElement.innerText = '';
        }
    });
    return hasError;
}

function callErrorAlert(message){
    console.log(message);
    UIkit.modal.alert(message);
}
