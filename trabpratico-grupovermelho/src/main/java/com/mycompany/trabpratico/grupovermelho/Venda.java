package com.mycompany.trabpratico.grupovermelho;

import java.math.BigDecimal;
import java.sql.Date;

public class Venda {
    private int codigo;
    private Date data;
    private BigDecimal valorTotal;
    private String formaPagamento;
    private int idCliente;

    public Venda(int codigo, Date data, BigDecimal valorTotal, String formaPagamento, int idCliente) {
        this.codigo = codigo;
        this.data = data;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.idCliente = idCliente;
    }   

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the valorTotal
     */
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    /**
     * @param valorTotal the valorTotal to set
     */
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * @return the formaPagamento
     */
    public String getFormaPagamento() {
        return formaPagamento;
    }

    /**
     * @param formaPagamento the formaPagamento to set
     */
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    /**
     * @return the idCliente
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}