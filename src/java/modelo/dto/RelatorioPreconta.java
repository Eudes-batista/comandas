package modelo.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RelatorioPreconta implements Serializable {

    private String PEDIDO;
    private String RESPONSAVEL_TRANSFERENCIA;
    private String RESPONSAVEL_PARCIAL;
    private String RESPONSAVEL_PRECONTA;
    private String MESA;
    private String COMANDA;
    private Date DATA_PRECONTA;
    private String MESA_ORIGEM;    
    private String RESPONSAVEL_REABRIU_MESA;
    private String MESA_REABERTA;    

    public RelatorioPreconta() {
    }

    public RelatorioPreconta(String PEDIDO, String RESPONSAVEL_TRANSFERENCIA, String RESPONSAVEL_PARCIAL, String RESPONSAVEL_PRECONTA, String MESA, String COMANDA, Date DATA_PRECONTA) {
        this.PEDIDO = PEDIDO;
        this.RESPONSAVEL_TRANSFERENCIA = RESPONSAVEL_TRANSFERENCIA;
        this.RESPONSAVEL_PARCIAL = RESPONSAVEL_PARCIAL;
        this.RESPONSAVEL_PRECONTA = RESPONSAVEL_PRECONTA;
        this.MESA = MESA;
        this.COMANDA = COMANDA;
        this.DATA_PRECONTA = DATA_PRECONTA;
    }        
}
