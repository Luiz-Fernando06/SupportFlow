const formularioCadastro =
    document.querySelector("#formulario-cadastro");

const campoNome =
    document.querySelector("#nome");

const campoEmail =
    document.querySelector("#email");

const campoSenha =
    document.querySelector("#senha");

const campoConfirmacaoSenha =
    document.querySelector("#confirmacao-senha");

const mensagemFormulario =
    document.querySelector("#mensagem-formulario");

const botaoCadastrar =
    formularioCadastro.querySelector("button[type='submit']");


formularioCadastro.addEventListener(
    "submit",
    async function (evento) {
        evento.preventDefault();

        limparMensagem();

        if (campoSenha.value !== campoConfirmacaoSenha.value) {
            mostrarMensagem(
                "As duas senhas precisam ser iguais.",
                "erro"
            );

            return;
        }

        alterarCarregamento(true);

        const novoUsuario = {
            nome: campoNome.value.trim(),
            email: campoEmail.value.trim(),
            senha: campoSenha.value
        };

        try {
            const resposta = await fetch(
                "/api/v1/auth/registrar",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(novoUsuario)
                }
            );

            if (!resposta.ok) {
                const mensagemErro =
                    await obterMensagemErro(resposta);

                throw new Error(mensagemErro);
            }

            const usuarioCriado = await resposta.json();

            console.log("Usuário criado:", usuarioCriado);

            formularioCadastro.reset();

            mostrarMensagem(
                "Conta criada com sucesso! Você já pode entrar.",
                "sucesso"
            );

            setTimeout(function () {
                window.location.href = "/index.html";
            }, 2000);

        } catch (erro) {
            console.error("Erro ao cadastrar:", erro);

            mostrarMensagem(
                erro.message
                    || "Não foi possível criar a conta.",
                "erro"
            );

        } finally {
            alterarCarregamento(false);
        }
    }
);


function alterarCarregamento(carregando) {
    botaoCadastrar.disabled = carregando;

    botaoCadastrar.textContent =
        carregando ? "Criando conta..." : "Criar conta";
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
            || "Não foi possível criar a conta.";

    } catch {
        return "Não foi possível criar a conta.";
    }
}