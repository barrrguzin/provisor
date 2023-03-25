function responseHandler(formID, formURL, formMethod) {
    // Get the form element
    const form = document.querySelector('#'+formID);

    // Handle the form submission event
    form.addEventListener('submit', event => {
        // Prevent the default form submission behavior
        event.preventDefault();

        // Send the POST request using the FormData API
        const formData = new FormData(form);
        fetch(formURL, {
            method: formMethod,
            body: formData
        })
            .then(response => response.text())
            .then(responseText => {
                if (responseText != null && responseText != '') {
                    alert(responseText);
                }
            })
            .catch(error => console.error(error));
    });
}


function reloadConfig(ip, mac, model, id) {

    let form = document.createElement('form');
    form.method = 'post';
    form.action = '/reload';
    form.target = 'update';
    form.id = model+mac;

    let methodInput = document.createElement('input');
    methodInput.type = 'hidden';
    methodInput.name = '_method';
    methodInput.value = 'patch';
    form.appendChild(methodInput);

    let ipInput = document.createElement('input');
    ipInput.type = 'hidden';
    ipInput.name = 'ip';
    ipInput.value = ip;
    form.appendChild(ipInput);

    let macInput = document.createElement('input');
    macInput.type = 'hidden';
    macInput.name = 'mac';
    macInput.value = mac;
    form.appendChild(macInput);

    let modelInput = document.createElement('input');
    modelInput.type = 'hidden';
    modelInput.name = 'model';
    modelInput.value = model;
    form.appendChild(modelInput);

    let button = document.createElement('button');
    button.type = 'submit';
    button.className = "btn btn-outline-warning";
    button.innerText = 'Залить конфиг';
    form.appendChild(button);

    let handler = document.createElement('script');
    handler.textContent = 'responseHandler(\'' + form.id + '\', \'' + form.action + '\', \'' + form.method + '\');';

    document.getElementById(id).appendChild(form);
    document.getElementById(id).appendChild(handler);
}


function generateConfig(number, id) {
    let form = document.createElement('form');
    form.method = 'post';
    form.action = '/config/make';
    form.target = 'update';
    form.id = form.method + number;

    let numberInput = document.createElement('input');
    numberInput.type = 'hidden';
    numberInput.name = 'number';
    numberInput.value = number;
    form.appendChild(numberInput);

    let button = document.createElement('button');
    button.type = 'submit';
    button.className = "btn btn-outline-warning";
    button.innerText = 'Сгенерировать конфиг';
    form.appendChild(button);

    let handler = document.createElement('script');
    handler.textContent = 'responseHandler(\'' + form.id + '\', \'' + form.action + '\', \'' + form.method + '\');';

    document.getElementById(id).appendChild(form);
    document.getElementById(id).appendChild(handler);
}