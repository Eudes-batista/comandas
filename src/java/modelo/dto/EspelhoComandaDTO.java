package modelo.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EspelhoComandaDTO implements Serializable {

    private Double QUANTIDADE_CANCELADA;
    private Double QUANTIDADE_LANCADA;
    private Double QUANTIDADE_ATUAL;
    private String COMANDA;
    private String MESA;
    private String PEDIDO;
    private String PESSOAS_MESA;
    private String STATUS;
    private Date DATA_PRECONTA;
    private String RESPONSAVEL_PRECONTA;
}
