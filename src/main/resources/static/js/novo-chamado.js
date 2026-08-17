const formularioChamado =
    document.querySelector("#formulario-chamado");

const campoTitulo =
    document.querySelector("#titulo");

const campoCategoria =
    document.querySelector("#categoria");

const campoPrioridade =
    document.querySelector("#prioridade");

const campoDescricao =
    document.querySelector("#descricao");

const mensagemFormulario =
    document.querySelector("#mensagem-formulario");

const botaoEnviar =
    document.querySelector("#botao-enviar");

const botaoLogout =
    document.querySelector("#botao-logout");


document.addEventListener(
    "DOMContentLoaded",
    carregarCategorias
);


botaoLogout.addEventListener(
    "click",
    logout
);


formularioChamado.addEventListener(
    "submit",
    criarChamado
);


async function carregarCategorias() {
    try {
        const resposta = await requisicaoAutenticada(
            "/api/v1/categoria"
        );

        if (!resposta.ok) {
            throw new Error(
                "Não foi possível carregar as categorias."
            );
        }

        const categorias = await resposta.json();

        campoCategoria.innerHTML =
            '<option value="">Selecione uma categoria</option>';

        categorias.forEach(function (categoria) {
            const opcao = document.createElement("option");

            opcao.value = categoria.id;
            opcao.textContent = categoria.nome;

            campoCategoria.appendChild(opcao);
        });

        campoCategoria.disabled = false;

    } catch (erro) {
        console.error(
            "Erro ao carregar categorias:",
            erro
        );

        campoCategoria.innerHTML =
            '<option value="">Categorias indisponíveis</option>';

        mostrarMensagem(
            erro.message,
            "erro"
        );
    }
}


async function criarChamado(evento) {
    evento.preventDefault();

    limparMensagem();
    alterarCarregamento(true);

    const novoChamado = {
        titulo: campoTitulo.value.trim(),
        descricao: campoDescricao.value.trim(),
        prioridade: campoPrioridade.value,
        categoriaId: Number(campoCategoria.value)
    };

    try {
        const resposta = await requisicaoAutenticada(
            "/api/v1/chamados",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(novoChamado)
            }
        );

        if (!resposta.ok) {
            const mensagemErro =
                await obterMensagemErro(resposta);

            throw new Error(mensagemErro);
        }

        const chamadoCriado = await resposta.json();

        console.log(
            "Chamado criado:",
            chamadoCriado
        );

        formularioChamado.reset();

        mostrarMensagem(
            `Chamado #${chamadoCriado.id} criado com sucesso!`,
            "sucesso"
        );

        setTimeout(function () {
            window.location.href = "/chamados.html";
        }, 1200);

    } catch (erro) {
        console.error(
            "Erro ao criar chamado:",
            erro
        );

        mostrarMensagem(
            erro.message ||
            "Não foi possível criar o chamado.",
            "erro"
        );

    } finally {
        alterarCarregamento(false);
    }
}


function alterarCarregamento(carregando) {
    botaoEnviar.disabled = carregando;

    botaoEnviar.textContent =
        carregando
            ? "Abrindo chamado..."
            : "Abrir chamado";
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
            || "Não foi possível criar o chamado.";

    } catch {
        return "Não foi possível criar o chamado.";
    }
}