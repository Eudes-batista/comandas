function desabilitarBotoes() {
    var botoes = document.querySelectorAll("#itensRapidos a");
    botoes.forEach(botao => {
        var texto = botao.textContent;
        texto = texto.trim();
        botao.style.display = texto ? "block" : "none";
    });
}
function limitarCaracteres() {
    var nomeProdutos = document.querySelectorAll(".nome-produto");
    nomeProdutos.forEach(e => {
        var texto = e.textContent;
        if (texto.toString().length >= 19) {
            e.textContent = texto.toString().substring(0, 18);
        }
    });
}
function adicionarQuantidade(id, idInput) {
    var valor = parseInt(document.getElementById(id).textContent);
    valor += 1;
    if (tratarNumerosNegativos(valor))
        document.getElementById(id).textContent = valor;
    document.getElementById(idInput).value = valor;
}
function removerQuantidade(id, idInput) {
    var valor = parseInt(document.getElementById(id).textContent);
    valor -= 1;
    if (tratarNumerosNegativos(valor)) {
        document.getElementById(id).textContent = valor;
        document.getElementById(idInput).value = valor;
    }
}
function mandarFocu() {
    document.querySelectorAll(".zeraQuantidade").forEach(elemento => {
        elemento.textContent = "1";
    });
}
function tratarNumerosNegativos(valor) {
    if (valor < 1)
        return false;
    return true;
}
function selecionarCampo(id) {
    setTimeout(function () {
        document.getElementById(id).value = 0;
        document.getElementById(id).focus();
        document.getElementById(id).select();
    }, 500);
}

function mostrarItens() {
    if (document.getElementById('itensRapidos').style.display === "none") {
        document.getElementById('itensRapidos').style.display = "block";
        document.getElementById('frmProduto').style.display = "none";
        return;
    }
    document.getElementById('itensRapidos').style.display = "none";
    document.getElementById('frmProduto').style.display = "block";
}
function apenasNumeros(input) {
    var regExp = /[^0-9]/g;
    input.value = input.value.replace(regExp, "");
    input.value = ("0000" + input.value).slice(-4);
}
function fecharModal(modal){
    document.getElementById(modal).style.display= "none";
    document.querySelectorAll('.modal-backdrop').forEach(element => {
        element.style.display="none";
    });
}

function acionarEventoBotao(id){
    document.getElementById(id).click();
}