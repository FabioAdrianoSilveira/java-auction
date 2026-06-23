package org.openjfx.boilao.util;

public class SessaoUsuario {
    private static String cnpjLogado;

    // Construtor privado para evitar instanciação
    private SessaoUsuario() {}

    public static String getCnpjLogado() {
        return cnpjLogado;
    }

    public static void setCnpjLogado(String cnpj) {
        SessaoUsuario.cnpjLogado = cnpj;
    }

    public static void limparSessao() {
        cnpjLogado = null;
    }
}