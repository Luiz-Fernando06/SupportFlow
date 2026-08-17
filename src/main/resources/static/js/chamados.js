const listaChamados =
    document.querySelector("#lista-chamados");

const estadoCarregamento =
    document.querySelector("#estado-carregamento");

const estadoVazio =
    document.querySelector("#estado-vazio");

const mensagemPagina =
    document.querySelector("#mensagem-pagina");

const botaoLogout =
    document.querySelector("#botao-logout");

const botaoNovoChamado =
    document.querySelector("#botao-novo-chamado");

const linkNovoChamado =
    document.querySelector("#link-novo-chamado");


document.addEventListener(
    "DOMContentLoaded",
    carregarChamados
);


botaoLogout.addEventListener(
    "click",
    logout
);


botaoNovoChamado.addEventListener(
    "click",
    function () {
        window.location.href = "/novo-chamado.html";
    }
);


linkNovoChamado.addEventListener(
    "click",
    function (evento) {
        evento.preventDefault();

        window.location.href =
            "/novo-chamado.html";
    }
);


async function carregarChamados() {
    try {
        const resposta = await requisicaoAutenticada(
            "/api/v1/chamados/meu"
        );

        if (!resposta.ok) {
            throw new Error(
                "Não foi possível carregar seus chamados."
            );
        }

        const chamados = await resposta.json();

        estadoCarregamento.classList.add("oculto");

        if (chamados.length === 0) {
            estadoVazio.classList.remove("oculto");
            return;
        }

        chamados.forEach(criarCartaoChamado);

    } catch (erro) {
        console.error(
            "Erro ao carregar chamados:",
            erro
        );

        estadoCarregamento.classList.add("oculto");

        mostrarErro(erro.message);
    }
}


function criarCartaoChamado(chamado) {
    const cartao = document.createElement("article");

    cartao.className = "cartao-chamado";


    const cabecalho = document.createElement("div");

    cabecalho.className = "chamado-cabecalho";


    const identificacao = document.createElement("div");


    const numero = document.createElement("span");

    numero.className = "chamado-numero";
    numero.textContent = `Chamado #${chamado.id}`;


    const titulo = document.createElement("h2");

    titulo.textContent =
        chamado.titulo || "Chamado sem título";


    const status = document.createElement("span");

    status.className =
        `status status-${normalizarClasse(chamado.status)}`;

    status.textContent =
        formatarTexto(chamado.status);


    identificacao.append(numero, titulo);
    cabecalho.append(identificacao, status);


    const informacoes = document.createElement("div");

    informacoes.className = "chamado-informacoes";


    const prioridade = document.createElement("span");

    prioridade.textContent =
        `Prioridade: ${formatarTexto(chamado.prioridade)}`;


    const categoria = document.createElement("span");

    categoria.textContent =
        `Categoria: ${obterNomeCategoria(chamado.categoria)}`;


    const data = document.createElement("span");

    data.textContent =
        `Criado em: ${formatarData(chamado.criadoEm)}`;


    informacoes.append(
        prioridade,
        categoria,
        data
    );


    const botaoDetalhes = document.createElement("button");

    botaoDetalhes.className = "botao-detalhes";
    botaoDetalhes.type = "button";
    botaoDetalhes.textContent = "Ver detalhes";

    botaoDetalhes.addEventListener(
        "click",
        function () {
            alert(
                "Os detalhes serão implementados na Etapa 5C."
            );
        }
    );


    cartao.append(
        cabecalho,
        informacoes,
        botaoDetalhes
    );

    listaChamados.appendChild(cartao);
}


function obterNomeCategoria(categoria) {
    if (!categoria) {
        return "Não informada";
    }

    if (typeof categoria === "string") {
        return formatarTexto(categoria);
    }

    return categoria.nome || "Não informada";
}


function formatarData(data) {
    if (!data) {
        return "Não informada";
    }

    const dataConvertida = new Date(data);

    if (Number.isNaN(dataConvertida.getTime())) {
        return data;
    }

    return dataConvertida.toLocaleString(
        "pt-BR",
        {
            dateStyle: "short",
            timeStyle: "short"
        }
    );
}


function formatarTexto(valor) {
    if (!valor) {
        return "Não informado";
    }

    return valor
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(
            /\b\w/g,
            letra => letra.toUpperCase()
        );
}


function normalizarClasse(valor) {
    if (!valor) {
        return "desconhecido";
    }

    return valor
        .toLowerCase()
        .replaceAll("_", "-");
}

function mostrarErro(texto) {
    mensagemPagina.textContent = texto;
    mensagemPagina.className = "mensagem-pagina erro";
}