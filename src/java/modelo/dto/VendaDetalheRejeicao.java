package modelo.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VendaDetalheRejeicao implements Serializable{

    private String PEDIDO;
    private String DESCRICAO;
    private Double VALOR;
    private String PORCENTAGEM;
    private String VALOR_PORCENTAGEM;
    private Date DATA;

    public VendaDetalheRejeicao() {
    }

    public VendaDetalheRejeicao(String PEDIDO, String DESCRICAO, Double VALOR, String PORCENTAGEM, String VALOR_PORCENTAGEM, Date DATA) {
        this.PEDIDO = PEDIDO;
        this.DESCRICAO = DESCRICAO;
        this.VALOR = VALOR;
        this.PORCENTAGEM = PORCENTAGEM;
        this.VALOR_PORCENTAGEM = VALOR_PORCENTAGEM;
        this.DATA = DATA;
    }
    
    
}
