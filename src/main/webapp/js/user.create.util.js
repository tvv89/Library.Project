function enableSubmitButton(e) {
    const form = e.target;
    const submitButton = form.querySelector('input[type="submit"]');
    submitButton.disabled = true;
    const hasError = formFieldsRegexCheck(form);
    if (hasError) {
        submitButton.disabled = false;
    }
    return !hasError;
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

window.addEventListener('DOMContentLoaded', (event) => {
    callPOSTRequest(1,0);
    document.getElementById("form-create-user").onsubmit = enableSubmitButton;
});
