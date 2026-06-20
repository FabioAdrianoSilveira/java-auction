package org.openjfx.boilao.model;

import org.openjfx.boilao.model.enums.TIPO;

public class Usuario {
    private String cnpj;
    private String fazenda;
    private String senha;
    private TIPO tipo;

    // Construtor completo
    public Usuario(String cnpj, String fazenda, String senha, TIPO tipo) {
        this.cnpj = cnpj;
        this.fazenda = fazenda;
        this.senha = senha;
        this.tipo = tipo;
    }

    // Construtor vazio
    public Usuario() {}

    // --- MÉTODOS DE FÁBRICA ESTÁTICOS (Solução para o erro de construtor) ---

    public static Usuario criarParaLoginCnpj(String cnpj, String senha) {
        Usuario u = new Usuario();
        u.setCnpj(cnpj);
        u.setSenha(senha);
        return u;
    }

    public static Usuario criarParaLoginFazenda(String fazenda, String senha) {
        Usuario u = new Usuario();
        u.setFazenda(fazenda);
        u.setSenha(senha);
        return u;
    }
    
    // -----------------------------------------------------------------------

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getFazenda() { return fazenda; }
    public void setFazenda(String fazenda) { this.fazenda = fazenda; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public TIPO getTipo() { return tipo; }
    public void setTipo(TIPO tipo) { this.tipo = tipo; }
}