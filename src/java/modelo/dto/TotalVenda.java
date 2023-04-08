package modelo.dto;

import java.io.Serializable;

public class TotalVenda implements Serializable{
    
    private Double VENDAS;
    private Double ITENS;

    public TotalVenda(Double VENDAS, Double ITENS) {
        this.VENDAS = VENDAS;
        this.ITENS = ITENS;
    }

    public TotalVenda() {
    }

    public Double getVENDAS() {
        if(this.VENDAS == null)
            return 0.0;
        return VENDAS;
    }

    public void setVENDAS(Double VENDAS) {
        this.VENDAS = VENDAS;
    }

    public Double getITENS() {
        if(this.ITENS == null)
            return 0.0;
        return ITENS;
    }

    public void setITENS(Double ITENS) {
        this.ITENS = ITENS;
    }

    
}
