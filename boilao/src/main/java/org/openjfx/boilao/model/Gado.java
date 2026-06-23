/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.openjfx.boilao.model;

import org.openjfx.boilao.model.enums.RACA;

public abstract class Gado {

    protected int id;
    protected String nome;
    protected int idade;
    protected RACA raca;
    protected double peso;
    protected String nomePai;
    protected String nomeMae;
    protected String observacoes;

    public Gado() {
    }

    public Gado(int id) {
        this.id = id;
    }

    public Gado(String nome, int idade, RACA raca, double peso, String nomePai, String nomeMae, String observacoes) {
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.peso = peso;
        this.nomePai = nomePai;
        this.nomeMae = nomeMae;
        this.observacoes = observacoes;
    }

    // Getters e Setters
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

    public abstract String getTipoGadoStr();
}
