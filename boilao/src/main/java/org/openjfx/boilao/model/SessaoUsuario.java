package org.openjfx.boilao.model;

import org.openjfx.boilao.model.enums.TIPO;

public class SessaoUsuario {
    private static String cnpjLogado;
    private static TIPO tipoLogado; // Adicionado aqui

    private SessaoUsuario() {}

    public static String getCnpjLogado() {
        return cnpjLogado;
    }

    public static void setCnpjLogado(String cnpj) {
        SessaoUsuario.cnpjLogado = cnpj;
    }

    public static TIPO getTipoLogado() { // Adicionado
        return tipoLogado;
    }

    public static void setTipoLogado(TIPO tipo) { // Adicionado
        SessaoUsuario.tipoLogado = tipo;
    }

    public static void limparSessao() {
        cnpjLogado = null;
        tipoLogado = null; // Adicionado
    }
}