package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RejeicaoPorcentagemVendedor implements Serializable{

    private String PEDIDO;
    private String VENDEDOR;

    public RejeicaoPorcentagemVendedor() {
    }

    public RejeicaoPorcentagemVendedor(String PEDIDO, String VENDEDOR) {
        this.PEDIDO = PEDIDO;
        this.VENDEDOR = VENDEDOR;
    }
    
    
}
