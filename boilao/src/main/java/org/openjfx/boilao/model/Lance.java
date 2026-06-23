/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.openjfx.boilao.model;

import java.time.LocalDateTime;

public class Lance {

    private double valor;
    private Usuario licitante;
    private LocalDateTime dataLance;
    private Leilao leilao;

    public Lance() {
        this.dataLance = LocalDateTime.now();
    }

    public Lance(double valor, Leilao leilao, Usuario licitante) {
        this.valor = valor;
        this.leilao = leilao;
        this.licitante = licitante;
        this.dataLance = LocalDateTime.now();
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Usuario getLicitante() {
        return licitante;
    }

    public void setLicitante(Usuario licitante) {
        this.licitante = licitante;
    }

    public LocalDateTime getDataLance() {
        return dataLance;
    }

    public void setDataLance(LocalDateTime dataLance) {
        this.dataLance = dataLance;
    }

    public Leilao getLeilao() {
        return leilao;
    }

    public void setLeilao(Leilao leilao) {
        this.leilao = leilao;
    }
}
