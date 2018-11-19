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
@EqualsAndHashCode(of = {"mesa"})
public class Mesa implements Serializable {

    private String mesa;
    private String status;
    private String quantidadePessoasPagantes;
    private String pedido;

    public Mesa(String mesa) {
        this.mesa = mesa;
    }

    public Mesa(String mesa, String status) {
        this.mesa = mesa;
        this.status = status;
    }

    public Mesa(String mesa, String status, String pedido) {
        this.mesa = mesa;
        this.status = status;
        this.pedido = pedido;
    }
    
    
    
}
