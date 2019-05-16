package modelo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = {"MESA"})
public class Mesa implements Serializable {

    private String MESA;
    private String STATUS;
    private String PAGANTES;
    private String PEDIDO;
    private String COMANDA;   

    public Mesa(String mesa) {
        this.MESA = mesa;
    }

    public Mesa(String mesa, String status) {
        this.MESA = mesa;
        this.STATUS = status;
    }

    public Mesa(String mesa, String status, String pedido) {
        this.MESA = mesa;
        this.STATUS = status;
        this.PEDIDO = pedido;
    }
    
    
    
}
