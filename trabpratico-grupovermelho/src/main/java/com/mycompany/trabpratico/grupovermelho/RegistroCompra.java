package com.mycompany.trabpratico.grupovermelho;

import java.math.BigDecimal;

public class RegistroCompra {
    private int codCompra;
    private int idProduto;
    private BigDecimal valorUnitario;
    private String observacao;

    public RegistroCompra(int codCompra, int idProduto, BigDecimal valorUnitario, String observacao) {
        this.codCompra = codCompra;
        this.idProduto = idProduto;
        this.valorUnitario = valorUnitario;
        this.observacao = observacao;
    }

    /**
     * @return the codCompra
     */
    public int getCodCompra() {
        return codCompra;
    }

    /**
     * @param codCompra the codCompra to set
     */
    public void setCodCompra(int codCompra) {
        this.codCompra = codCompra;
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
     * @return the valorUnitario
     */
    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    /**
     * @param valorUnitario the valorUnitario to set
     */
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    
}