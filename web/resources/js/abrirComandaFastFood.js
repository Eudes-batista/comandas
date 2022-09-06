let numeroComanda = document.getElementById("numeroComanda");
let mensagem = document.querySelector(".hidden");
let buttonAbrirComanda = document.getElementById("abrirComanda");
let buttonFecharMensagem = document.getElementById("fecharMensagem");
let mensagemTexto = document.getElementById("mensagem");

buttonAbrirComanda.addEventListener("click", () => {
    abrirComanda();
});

buttonFecharMensagem.addEventListener("click", () => {
    esconderMensagem();
});

numeroComanda.addEventListener("input", () => {
    if (!numeroComanda.value) return;
    numeroComanda.value = numeroComanda.value.replace(/\D/g, '');
});

numeroComanda.addEventListener("keypress", (event) => {
    if (event.keyCode === 13)
        abrirComanda();
});

function abrirComanda() {
    if (!numeroComanda.value) {
        mostrarMensagem("Número da comanda é Obrigatorio.");
        return;
    }
    if (numeroComanda.value === "0000") {
        mostrarMensagem("Não pode ser zero.");
        return;
    }
    location.href = "fastfood.jsf?comanda=" + ("0000" + numeroComanda.value).slice(-4);
}

function mostrarMensagem(texto) {
    mensagem.classList.remove("hidden");
    numeroComanda.focus();
    numeroComanda.select();
    mensagemTexto.textContent = texto;
}

function esconderMensagem() {
    mensagem.classList.add("hidden");
    numeroComanda.focus();
}



