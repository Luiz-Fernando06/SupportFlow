const nomeCabecalho =
    document.querySelector("#nome-cabecalho");

const perfilCabecalho =
    document.querySelector("#perfil-cabecalho");

const tituloBoasVindas =
    document.querySelector("#titulo-boas-vindas");

const nomeUsuario =
    document.querySelector("#nome-usuario");

const emailUsuario =
    document.querySelector("#email-usuario");

const papelUsuario =
    document.querySelector("#papel-usuario");

const orientacaoUsuario =
    document.querySelector("#orientacao-usuario");

const mensagemPainel =
    document.querySelector("#mensagem-painel");

const linkAdministracao =
    document.querySelector("#link-administracao");

const botaoLogout =
    document.querySelector("#botao-logout");


console.log("painel.js carregado");


document.addEventListener(
    "DOMContentLoaded",
    carregarUsuario
);


botaoLogout.addEventListener(
    "click",
    logout
);


async function carregarUsuario() {
    console.log("Iniciando carregamento do usuário");

    if (!usuarioEstaAutenticado()) {
        console.log("Token não encontrado");

        window.location.href = "/index.html";
        return;
    }

    console.log("Token encontrado");

    try {
        console.log("Chamando /api/v1/auth/me");

        const resposta = await requisicaoAutenticada(
            "/api/v1/auth/me"
        );

        console.log(
            "Resposta recebida:",
            resposta.status
        );

        if (!resposta.ok) {
            throw new Error(
                "Não foi possível carregar o usuário."
            );
        }

        const usuario = await resposta.json();

        console.log(
            "Usuário recebido:",
            usuario
        );

        exibirUsuario(usuario);

    } catch (erro) {
        console.error(
            "Erro ao carregar usuário:",
            erro
        );

        mensagemPainel.textContent =
            erro.message;

        mensagemPainel.className =
            "mensagem-pagina erro";
    }
}


function exibirUsuario(usuario) {
    nomeCabecalho.textContent =
        usuario.nome;

    perfilCabecalho.textContent =
        traduzirPerfil(usuario.role);

    tituloBoasVindas.textContent =
        `Olá, ${usuario.nome}!`;

    nomeUsuario.textContent =
        usuario.nome;

    emailUsuario.textContent =
        usuario.email;

    papelUsuario.textContent =
        traduzirPerfil(usuario.role);

    if (usuario.role === "ADMIN") {
        configurarPainelAdmin();
    } else {
        configurarPainelUsuario();
    }
}


function configurarPainelUsuario() {
    orientacaoUsuario.textContent =
        "Você poderá abrir um chamado e acompanhar " +
        "seus atendimentos por este painel.";
}


function configurarPainelAdmin() {
    linkAdministracao.classList.remove("oculto");

    orientacaoUsuario.textContent =
        "Como administrador, você poderá visualizar " +
        "todos os chamados e gerenciar os atendimentos.";
}


function traduzirPerfil(role) {
    if (role === "ADMIN") {
        return "Administrador";
    }

    return "Usuário";
}