const CHAVE_TOKEN = "token";


function obterToken() {
    return localStorage.getItem(CHAVE_TOKEN);
}


function salvarToken(token) {
    localStorage.setItem(CHAVE_TOKEN, token);
}


function removerToken() {
    localStorage.removeItem(CHAVE_TOKEN);
}


function usuarioEstaAutenticado() {
    return Boolean(obterToken());
}


function logout() {
    removerToken();
    window.location.href = "/index.html";
}


async function requisicaoAutenticada(url, opcoes = {}) {
    const token = obterToken();

    if (!token) {
        window.location.href = "/index.html";

        throw new Error(
            "Usuário não autenticado."
        );
    }

    const headers = new Headers(opcoes.headers || {});

    headers.set(
        "Authorization",
        `Bearer ${token}`
    );

    const resposta = await fetch(url, {
        ...opcoes,
        headers
    });

    if (resposta.status === 401 || resposta.status === 403) {
        removerToken();
        window.location.href = "/index.html";

        throw new Error(
            "Sua sessão expirou. Entre novamente."
        );
    }

    return resposta;
}