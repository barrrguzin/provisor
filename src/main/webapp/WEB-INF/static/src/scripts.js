function reloadConfig(ip, mac, model, id) {
    var form = document.createElement('form');
    form.method = 'post';
    form.action = '/reload';

    var methodInput = document.createElement('input');
    methodInput.type = 'hidden';
    methodInput.name = '_method';
    methodInput.value = 'patch';
    form.appendChild(methodInput);

    var ipInput = document.createElement('input');
    ipInput.type = 'hidden';
    ipInput.name = 'ip';
    ipInput.value = ip;
    form.appendChild(ipInput);

    var macInput = document.createElement('input');
    macInput.type = 'hidden';
    macInput.name = 'mac';
    macInput.value = mac;
    form.appendChild(macInput);

    var modelInput = document.createElement('input');
    modelInput.type = 'hidden';
    modelInput.name = 'model';
    modelInput.value = model;
    form.appendChild(modelInput);

    var button = document.createElement('button');
    button.type = 'submit';
    button.class = "btn btn-outline-warning";
    button.innerText = 'Залить конфиг';
    form.appendChild(button);

    document.getElementById(id).appendChild(form);
}