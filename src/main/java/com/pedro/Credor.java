package com.pedro;

public class Credor {

    private String nome;
    private String documento;
    private String titulo;
    private double quantidade;
    private double valor;

    public Credor() {
    }

    public Credor(String nome, String documento, String titulo, double quantidade, Double valor) {
        this.nome = nome;
        this.documento = documento;
        this.titulo = titulo;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}