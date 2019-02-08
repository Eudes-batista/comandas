function desabilitarBotoes() {
    var botoes = document.querySelectorAll(".botoes-rapidos a");
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
        if (texto.toString().length >= 21)
            e.textContent = texto.toString().substring(0, 20);
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
    var botoes = document.querySelectorAll(".focu");
    botoes.forEach(b => {
        b.focus();
    });
}
function tratarNumerosNegativos(valor) {
    if (valor < 1)
        return false;
    return true;
}
function selecionarCampo(id){
    document.getElementById(id).select();
    document.getElementById(id).focus();
}

