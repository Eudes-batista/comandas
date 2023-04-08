package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVendido implements Serializable{
private String REFERENCIA;
private String DESCRICAO;
private Double QUANTIDADE;
private Double VALOR;
private Double TOTAL;

    public ItemVendido() {
    }

    public ItemVendido(String REFERENCIA, String DESCRICAO, Double QUANTIDADE, Double VALOR, Double TOTAL) {
        this.REFERENCIA = REFERENCIA;
        this.DESCRICAO = DESCRICAO;
        this.QUANTIDADE = QUANTIDADE;
        this.VALOR = VALOR;
        this.TOTAL = TOTAL;
    }

    @Override
    public String toString() {
        return "itemVendido{" + "REFERENCIA=" + REFERENCIA + ", DESCRICAO=" + DESCRICAO + ", QUANTIDADE=" + QUANTIDADE + ", VALOR=" + VALOR + ", TOTAL=" + TOTAL+"}'";
    }


    
}
