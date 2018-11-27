package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteAtendido implements Serializable{
    
    private String PEDIDO;
    private String PESSOAS;

    public ClienteAtendido() {
    }

    public ClienteAtendido(String PEDIDO, String PESSOAS) {
        this.PEDIDO = PEDIDO;
        this.PESSOAS = PESSOAS;
    }

}
