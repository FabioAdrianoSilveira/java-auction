package org.openjfx.boilao.controller.model;

import java.time.LocalDateTime;

public class LeilaoModel {

    private int idGado;
    private double lanceMinimo;
    private LocalDateTime dataInicio;

    public LeilaoModel() {
    }

    public LeilaoModel(int idGado, double lanceMinimo, LocalDateTime dataInicio) {
        this.idGado = idGado;
        this.lanceMinimo = lanceMinimo;
        this.dataInicio = dataInicio;
    }

    public int getIdGado() {
        return idGado;
    }

    public void setIdGado(int idGado) {
        this.idGado = idGado;
    }

    public double getLanceMinimo() {
        return lanceMinimo;
    }

    public void setLanceMinimo(double lanceMinimo) {
        this.lanceMinimo = lanceMinimo;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }
}
