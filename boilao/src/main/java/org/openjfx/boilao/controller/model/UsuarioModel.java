package org.openjfx.boilao.controller.model;

import org.openjfx.boilao.model.enums.TIPO;

public class UsuarioModel {

    private String cnpj;
    private String razao;
    private String senha;
    private TIPO tipo;

    public UsuarioModel() {
    }

    public UsuarioModel(String cnpj, String senha) {
        this.cnpj = cnpj;
        this.senha = senha;
    }

    public UsuarioModel(String cnpj, String razao, TIPO tipo) {
        this.cnpj = cnpj;
        this.razao = razao;
        this.tipo = tipo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TIPO getTipo() {
        return tipo;
    }

    public void setTipo(TIPO tipo) {
        this.tipo = tipo;
    }
}
