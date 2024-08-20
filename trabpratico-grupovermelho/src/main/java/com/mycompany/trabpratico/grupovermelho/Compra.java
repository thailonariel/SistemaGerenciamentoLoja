package com.mycompany.trabpratico.grupovermelho;

import java.math.BigDecimal;
import java.sql.Date;

public class Compra {
    private int codigo;
    private Date data;
    private BigDecimal valorTotal;

    public Compra(int codigo, Date data, BigDecimal valorTotal) {
        this.codigo = codigo;
        this.data = data;
        this.valorTotal = valorTotal;
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
   
}
