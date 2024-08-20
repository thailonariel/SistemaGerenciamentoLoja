/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trabpratico.grupovermelho;

import java.math.BigDecimal;

public class Caixa {
    private int codigo;
    private BigDecimal valorCaixa;

    public Caixa(int codigo, BigDecimal valorCaixa) {
        this.codigo = codigo;
        this.valorCaixa = valorCaixa;
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
     * @return the valorCaixa
     */
    public BigDecimal getValorCaixa() {
        return valorCaixa;
    }

    /**
     * @param valorCaixa the valorCaixa to set
     */
    public void setValorCaixa(BigDecimal valorCaixa) {
        this.valorCaixa = valorCaixa;
    }

}