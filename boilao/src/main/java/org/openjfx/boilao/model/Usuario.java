package org.openjfx.boilao.model;

public class Usuario {

    private String cnpj;
    private String fazenda;
    private String senha;
    private String tipo;

    public Usuario() {
    }

    public Usuario(String cnpj, String fazenda, String senha, String tipo) {
        this.cnpj = cnpj;
        this.fazenda = fazenda;
        this.senha = senha;
        this.tipo = tipo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getFazenda() {
        return fazenda;
    }

    public String getSenha() {
        return senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setFazenda(String fazenda) {
        this.fazenda = fazenda;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}