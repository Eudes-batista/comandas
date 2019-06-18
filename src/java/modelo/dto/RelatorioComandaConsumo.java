
package modelo.dto;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"PEDIDO"})
public class RelatorioComandaConsumo implements Serializable{

    private String MESA;
    private String COMANDA;
    private String PEDIDO;
    private String STATUS;
    private Double TOTAL;
    private String PARCIAL;
    
}
