package modelo.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = "PEDIDO")
@ToString
public class ItemPedido implements Serializable {

    private String PEDIDO;
    private String MESA;
    private String COMANDA;
    private Date DATA;
    private String REFERENCIA;
    private String DESCRICAO;
    private String FOI_PRODUZIDO;
    private String IMPRESSAO;
    private String STATUS_ITEM;
    private Double QUANTIDADE;
    private Double VALOR_ITEM;
    private Double TOTAL;
    private String GARCOM;
    private Date DATA_CANCELAMENTO;
}
