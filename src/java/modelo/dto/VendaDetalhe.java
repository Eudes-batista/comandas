package modelo.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VendaDetalhe implements Serializable{

    private String PEDIDO;
    private String REFERENCIA;
    private String DESCRICAO;
    private Double QUANTIDADE;
    private Double VALOR;
    private Double TOTAL;
    private Double PORCENTAGEM;
    private Double VALOR_PORCENTAGEM;
    private Date DATA;

    public VendaDetalhe() {
    }

    public VendaDetalhe(String PEDIDO, String REFERENCIA, String DESCRICAO, Double QUANTIDADE, Double VALOR, Double TOTAL, Double PORCENTAGEM, Double VALOR_PORCENTAGEM, Date DATA) {
        this.PEDIDO = PEDIDO;
        this.REFERENCIA = REFERENCIA;
        this.DESCRICAO = DESCRICAO;
        this.QUANTIDADE = QUANTIDADE;
        this.VALOR = VALOR;
        this.TOTAL = TOTAL;
        this.PORCENTAGEM = PORCENTAGEM;
        this.VALOR_PORCENTAGEM = VALOR_PORCENTAGEM;
        this.DATA = DATA;
    }

}
