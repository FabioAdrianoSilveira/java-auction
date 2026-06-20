package org.openjfx.boilao.model.enums;

public enum TIPO {
    ADMINISTRADOR("ADMINISTRADOR"),
    LICITANTE("LICITANTE");

    private final String valor;

    TIPO(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}