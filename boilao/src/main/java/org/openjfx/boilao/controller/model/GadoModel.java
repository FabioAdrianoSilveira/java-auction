package org.openjfx.boilao.controller.model;

import org.openjfx.boilao.model.enums.PRENHEZ;
import org.openjfx.boilao.model.enums.RACA;

public class GadoModel {

    private int id;
    private String nome;
    private int idade;
    private RACA raca;
    private double peso;
    private String nomePai;
    private String nomeMae;
    private String observacoes;
    private double taxaSucessoFertilizacao;
    private int inseminacoesRealizadas;
    private PRENHEZ prenhez;
    private int criasAnteriores;

    public GadoModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public RACA getRaca() {
        return raca;
    }

    public void setRaca(RACA raca) {
        this.raca = raca;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public double getTaxaSucessoFertilizacao() {
        return taxaSucessoFertilizacao;
    }

    public void setTaxaSucessoFertilizacao(double taxa) {
        this.taxaSucessoFertilizacao = taxa;
    }

    public int getInseminacoesRealizadas() {
        return inseminacoesRealizadas;
    }

    public void setInseminacoesRealizadas(int ins) {
        this.inseminacoesRealizadas = ins;
    }

    public PRENHEZ getPrenhez() {
        return prenhez;
    }

    public void setPrenhez(PRENHEZ prenhez) {
        this.prenhez = prenhez;
    }

    public int getCriasAnteriores() {
        return criasAnteriores;
    }

    public void setCriasAnteriores(int crias) {
        this.criasAnteriores = crias;
    }
}
