package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteAtendido implements Serializable{
    
    private String PEDIDO;
    private String PESSOAS;
    private Double QUANTIDADE;

    public ClienteAtendido() {
    }

    public ClienteAtendido(String PEDIDO, String PESSOAS) {
        this.PEDIDO = PEDIDO;
        this.PESSOAS = PESSOAS;
    }

    public ClienteAtendido(String PEDIDO, String PESSOAS, Double QUANTIDADE) {
        this.PEDIDO = PEDIDO;
        this.PESSOAS = PESSOAS;
        this.QUANTIDADE = QUANTIDADE;
    }

    public ClienteAtendido(Double QUANTIDADE) {
        this.QUANTIDADE = QUANTIDADE;
    }
    
    

}
