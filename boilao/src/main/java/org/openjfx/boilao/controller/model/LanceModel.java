package org.openjfx.boilao.controller.model;

public class LanceModel {

    private double valor;
    private int idLeilao;
    private String cnpjLicitante;

    public LanceModel() {
    }

    public LanceModel(double valor, int idLeilao, String cnpjLicitante) {
        this.valor = valor;
        this.idLeilao = idLeilao;
        this.cnpjLicitante = cnpjLicitante;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getIdLeilao() {
        return idLeilao;
    }

    public void setIdLeilao(int idLeilao) {
        this.idLeilao = idLeilao;
    }

    public String getCnpjLicitante() {
        return cnpjLicitante;
    }

    public void setCnpjLicitante(String cnpjLicitante) {
        this.cnpjLicitante = cnpjLicitante;
    }
}
