package com.mycompany.trabpratico.grupovermelho;

import java.math.BigDecimal;

public class Produto {    
    private int idProduto;
    private String nome;
    private double preco;
    private String tamanho;
    private String cor;
    private String descricao;
    private int estacao;

    public Produto(int idProduto, String nome, double preco, String tamanho, String cor, String descricao, int estacao) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.preco = preco;
        this.tamanho = tamanho;
        this.cor = cor;
        this.descricao = descricao;
        this.estacao = estacao;
    }

    /**
     * @return the idProduto
     */
    public int getIdProduto() {
        return idProduto;
    }

    /**
     * @param idProduto the idProduto to set
     */
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the preco
     */
    public double getPreco() {
        return preco;
    }

    /**
     * @param preco the preco to set
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * @return the tamanho
     */
    public String getTamanho() {
        return tamanho;
    }

    /**
     * @param tamanho the tamanho to set
     */
    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    /**
     * @return the cor
     */
    public String getCor() {
        return cor;
    }

    /**
     * @param cor the cor to set
     */
    public void setCor(String cor) {
        this.cor = cor;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the estacao
     */
    public int getEstacao() {
        return estacao;
    }

    /**
     * @param estacao the estacao to set
     */
    public void setEstacao(short estacao) {
        this.estacao = estacao;
    }
    
}
