/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.openjfx.boilao.model;

import org.openjfx.boilao.model.enums.RACA;

public class Macho extends Gado {

    private double taxaSucessoFertilizacao;
    private int inseminacoesRealizadas;

    public Macho() {
    }

    public Macho(String nome, int idade, RACA raca, double peso, String nomePai, String nomeMae, String observacoes, double taxaSucessoFertilizacao, int inseminacoesRealizadas) {
        super(nome, idade, raca, peso, nomePai, nomeMae, observacoes);
        this.taxaSucessoFertilizacao = taxaSucessoFertilizacao;
        this.inseminacoesRealizadas = inseminacoesRealizadas;
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

    public void setInseminacoesRealizadas(int inseminacoes) {
        this.inseminacoesRealizadas = inseminacoes;
    }

    @Override
    public String getTipoGadoStr() {
        return "MACHO";
    }
}
