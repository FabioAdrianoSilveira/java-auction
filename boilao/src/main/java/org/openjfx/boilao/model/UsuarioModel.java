package org.openjfx.boilao.model;

import org.openjfx.boilao.model.enums.TIPO;

public class UsuarioModel {
    private String cnpj;
    private String fazenda;
    private String senha;
    private TIPO tipo;

    // Construtor completo do diagrama
    public UsuarioModel(String cnpj, String fazenda, String senha, TIPO tipo) {
        this.cnpj = cnpj;
        this.fazenda = fazenda;
        this.senha = senha;
        this.tipo = tipo;
    }

    // Construtor com apenas CNPJ do diagrama
    public UsuarioModel(String cnpj) {
        this.cnpj = cnpj;
    }

    // --- CORREÇÃO DA MODELAGEM: Métodos de Fábrica no lugar dos construtores conflitantes ---
    
    public static UsuarioModel criarComCnpjESenha(String cnpj, String senha) {
        UsuarioModel um = new UsuarioModel(cnpj);
        um.setSenha(senha);
        return um;
    }

    public static UsuarioModel criarComFazendaESenha(String fazenda, String senha) {
        UsuarioModel um = new UsuarioModel(null); // cnpj nulo
        um.setFazenda(fazenda);
        um.setSenha(senha);
        return um;
    }

    // Getters e Setters
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getFazenda() { return fazenda; }
    public void setFazenda(String fazenda) { this.fazenda = fazenda; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public TIPO getTipo() { return tipo; }
    public void setTipo(TIPO tipo) { this.tipo = tipo; }
}