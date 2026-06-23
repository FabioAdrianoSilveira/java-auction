/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.openjfx.boilao.model;

import java.time.LocalDateTime;

public class Leilao {

    private int id;
    private Gado gado;
    private double lanceMinimo;
    private Lance maiorLance;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    public Leilao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Gado getGado() {
        return gado;
    }

    public void setGado(Gado gado) {
        this.gado = gado;
    }

    public double getLanceMinimo() {
        return lanceMinimo;
    }

    public void setLanceMinimo(double lanceMinimo) {
        this.lanceMinimo = lanceMinimo;
    }

    public Lance getMaiorLance() {
        return maiorLance;
    }

    public void setMaiorLance(Lance maiorLance) {
        this.maiorLance = maiorLance;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    // Ao setar o início, a regra calcula a data de término automaticamente
    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
        if (dataInicio != null) {
            this.dataFim = dataInicio.plusHours(2); // Regra: Fim é 2 horas após o início
        }
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }
}
