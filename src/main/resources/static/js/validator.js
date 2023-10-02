// Open menu mobile
function toggleMenuMobile() {
    $('.electroplus__menu__navbar').toggleClass('show');
}

function numberDecimal(evt, enteros, decimal, negative) {
    // Backspace = 8, Enter = 13, ‘0′ = 48, ‘9′ = 57, ‘.’ = 46, ‘-’ = 45
    var key = (evt.which) ? evt.which : evt.keyCode;
    var input = evt.target;
    const txt = input.value;
    const isCero = txt === 0 || txt === "0";
    if (isCero) input.select();
    const indexof = txt.indexOf(".");
    enteros = enteros ? enteros : 10;
    decimal = decimal ? decimal : 2;
    var preg = new RegExp(`^${negative === true ? '-?' : ''}\\d{0,${enteros}}(\\.\\d{0,${decimal}})?$`);
    var chark = String.fromCharCode(key);
    if (getSelectedText(input)) return soloenteros(chark);
    if (soloenteros(chark) || key === 46 || (negative === true && key === 45)) {
        var temp = txt.split("");
        temp = insert(temp, input.selectionStart, chark);
        var valtemp = temp.join("");
        if (key === 46) {
            if (!(indexof > -1)) {
                return true;
            }
        } else {
            return preg.test(valtemp);
        }
        return !(key > 31 && (key < 48 || key > 57));
    }
    return false;
}


function getSelectedText(input) {
    var startPos = input.selectionStart;
    var endPos = input.selectionEnd;
    var doc = document.selection;
    const val1 = doc && doc.createRange().text.length !== 0;
    const val2 = !doc && input.value.substring(startPos, endPos).length !== 0;
    return val1 || val2;
}

function soloenteros(value) {
    return /^[0-9]+$/.test(value);
}

function insert(arr, index, newItem) {
    return [...arr.slice(0, index), newItem, ...arr.slice(index)]
}

/**
 * @Event event
 * */
function getImage(event) {
    const file = event.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
        $('#preview').attr('src', e.target.result);
        document.getElementById("foto").value = e.target.result;
    }

    reader.readAsDataURL(file);
    document.getElementById("nombreimagen").value = file.name;
    document.getElementById("labelfoto").innerHTML = 'C:\\fakepath\\' + file.name;
}

const formatDate = (date) => {
    console.log(data)
    return date;
}

const change = (id) => {
    var cantidad = Number($("#" + id).val());
    cantidad > 0 ? rest(id, cantidad) : null;
}

const aumentar = (id) => {
    var cantidad = Number($("#" + id).val()) + 1;
    cantidad > 0 ? rest(id, cantidad) : null;
}

const disminuir = (id) => {
    var cantidad = Number($("#" + id).val()) - 1;
    cantidad > 0 ? rest(id, cantidad) : null;
}

const rest = (id, cantidad) => {
    $.ajax({
        url: "carrito/update/" + id + "/" + cantidad
    }).done((resp) => {
        if (resp === "OK") location.reload();
    }).fail((jq, text, err) => {
        console.log(text + ' - ' + err);
    });
}

(function () {
    'use strict';
    window.addEventListener('load', function () {
        var forms = document.getElementsByClassName('needs-validation');
        var validation = Array.prototype.filter.call(forms, function (form) {
            form.addEventListener('submit', function (event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();

function generarPdf(id) {
    var fecha = document.getElementById('fecha' + id).innerText;
    var opt = {
        margin: 0,
        filename: 'pedido-' + fecha + '.pdf',
        image: {type: 'jpeg', quality: 1},
        pagebreak: [
            {mode: ['avoid-all']}
        ],
        html2canvas: {scale: 2},
        jsPDF: {
            unit: 'mm',
            format: [245, 315],
            orientation: 'portrait',
            floatPrecision: 'smart',
            precision: 100
        }
    };
    var element = document.getElementById('reporte' + id);
    html2pdf(element, opt);
}
