package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Couvert implements Serializable{
    
    private String DESCRICAO;
    private Double VALOR;

    public Couvert() {
    }

    public Couvert(String DESCRICAO, Double VALOR) {
        this.DESCRICAO = DESCRICAO;
        this.VALOR = VALOR;
    }
    
    
}
