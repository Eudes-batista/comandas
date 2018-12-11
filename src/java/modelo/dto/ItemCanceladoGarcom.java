package modelo.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemCanceladoGarcom implements Serializable{
    
    private String  NUMERO;
    private String  PEDIDO;
    private String  GARCOM;
    private Double  TOTAL;
    private Double  CANCELAMENTO;
    private String  ITEM;
    private String  MOTIVO;
    private String  OBSERVACAO;
    private String  PRODUZIDO;
    private String  RESPONSAVEL;  
    private String  DESTINO;  

    public ItemCanceladoGarcom() {
    }

    public ItemCanceladoGarcom(String GARCOM) {
        this.GARCOM = GARCOM;
    }

    public ItemCanceladoGarcom(String NUMERO, String PEDIDO, String GARCOM, Double TOTAL, Double CANCELAMENTO, String ITEM, String MOTIVO, String OBSERVACAO, String PRODUZIDO, String RESPONSAVEL, String DESTINO) {
        this.NUMERO = NUMERO;
        this.PEDIDO = PEDIDO;
        this.GARCOM = GARCOM;
        this.TOTAL = TOTAL;
        this.CANCELAMENTO = CANCELAMENTO;
        this.ITEM = ITEM;
        this.MOTIVO = MOTIVO;
        this.OBSERVACAO = OBSERVACAO;
        this.PRODUZIDO = PRODUZIDO;
        this.RESPONSAVEL = RESPONSAVEL;
        this.DESTINO = DESTINO;
    }

    public ItemCanceladoGarcom(String GARCOM, Double TOTAL) {
        this.GARCOM = GARCOM;
        this.TOTAL = TOTAL;
    }

    @Override
    public String toString() {
        return "ItemCanceladoGarcom{" + "NUEMRO=" + NUMERO + ", PEDIDO=" + PEDIDO + ", GARCOM=" + GARCOM + ", TOTAL=" + TOTAL + ", CANCELAMENTO=" + CANCELAMENTO + ", ITEM=" + ITEM + ", MOTIVO=" + MOTIVO + ", OBSERVACAO=" + OBSERVACAO + ", PRODUZIDO=" + PRODUZIDO + ", RESPONSAVEL=" + RESPONSAVEL + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.GARCOM);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemCanceladoGarcom other = (ItemCanceladoGarcom) obj;
        return Objects.equals(this.GARCOM, other.GARCOM);
    }
    
    
}
