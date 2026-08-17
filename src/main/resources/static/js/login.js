const formularioLogin =
    document.querySelector("#formulario-login");

const campoEmail =
    document.querySelector("#email");

const campoSenha =
    document.querySelector("#senha");

const mensagemFormulario =
    document.querySelector("#mensagem-formulario");

const botaoEntrar =
    formularioLogin.querySelector("button[type='submit']");

if (localStorage.getItem("token")) {
    window.location.href = "/painel.html";
}

formularioLogin.addEventListener("submit", async function (evento) {
    evento.preventDefault();

    limparMensagem();
    alterarCarregamento(true);

    const dadosLogin = {
        email: campoEmail.value.trim(),
        senha: campoSenha.value
    };

    try {
        const resposta = await fetch("/api/v1/auth/login", {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(dadosLogin)
        });

        if (!resposta.ok) {
            const mensagemErro =
                await obterMensagemErro(resposta);

            throw new Error(mensagemErro);
        }

        const dados = await resposta.json();

        if (!dados.token) {
            throw new Error(
                "O servidor não retornou o token de autenticação."
            );
        }

        localStorage.setItem("token", dados.token);

        mostrarMensagem(
            "Login realizado! Redirecionando...",
            "sucesso"
        );

        console.log("Login realizado com sucesso.");

        setTimeout(function () {
            window.location.href = "/painel.html";
        }, 800);

    } catch (erro) {
        console.error("Erro ao realizar login:", erro);

        mostrarMensagem(
            erro.message || "Não foi possível realizar o login.",
            "erro"
        );

    } finally {
        alterarCarregamento(false);
    }
});


function alterarCarregamento(carregando) {
    botaoEntrar.disabled = carregando;

    botaoEntrar.textContent =
        carregando ? "Entrando..." : "Entrar";
}


function mostrarMensagem(texto, tipo) {
    mensagemFormulario.textContent = texto;

    mensagemFormulario.className =
        `mensagem-formulario ${tipo}`;
}


function limparMensagem() {
    mensagemFormulario.textContent = "";

    mensagemFormulario.className =
        "mensagem-formulario";
}


async function obterMensagemErro(resposta) {
    try {
        const erro = await resposta.json();

        return erro.message
            || erro.mensagem
            || erro.error
            || "E-mail ou senha inválidos.";

    } catch {
        return "E-mail ou senha inválidos.";
    }
}