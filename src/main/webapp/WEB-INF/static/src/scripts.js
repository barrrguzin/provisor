function reloadConfig(ip, mac, model, id) {
    let form = document.createElement('form');
    form.method = 'post';
    form.action = '/reload';

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
    button.class = "btn btn-outline-warning";
    button.innerText = 'Залить конфиг';
    form.appendChild(button);

    document.getElementById(id).appendChild(form);
}