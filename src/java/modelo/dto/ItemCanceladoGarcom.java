package modelo.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@EqualsAndHashCode(of={"GARCOM"})
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
    private String  MESA;  
    private String  COMANDA;  
    private Date  DATA_CANCELAMENTO;  

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
    
}
