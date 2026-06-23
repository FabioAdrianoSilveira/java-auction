/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.openjfx.boilao.model;

import org.openjfx.boilao.model.enums.PRENHEZ;
import org.openjfx.boilao.model.enums.RACA;

public class Femea extends Gado {

    private PRENHEZ prenhez;
    private int criasAnteriores;

    public Femea() {
    }

    public Femea(String nome, int idade, RACA raca, double peso, String nomePai, String nomeMae, String observacoes, PRENHEZ prenhez, int criasAnteriores) {
        super(nome, idade, raca, peso, nomePai, nomeMae, observacoes);
        this.prenhez = prenhez;
        this.criasAnteriores = criasAnteriores;
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

    @Override
    public String getTipoGadoStr() {
        return "FEMEA";
    }
}
