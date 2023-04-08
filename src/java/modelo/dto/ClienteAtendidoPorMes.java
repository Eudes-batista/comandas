package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteAtendidoPorMes implements Serializable{
    
    private Integer quantidade;
    private Integer mes;

    public ClienteAtendidoPorMes() {
    }

    public ClienteAtendidoPorMes(Integer quantidade, Integer mes) {
        this.quantidade = quantidade;
        this.mes = mes;
    }

    @Override
    public String toString() {
        return "{quantidade:"+quantidade+",mes:"+mes+"}";
    }
        
}
