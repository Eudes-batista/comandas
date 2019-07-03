package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EspelhoComandaDTO implements Serializable{
    private Double QUANTIDADE_CANCELADA;
    private Double QUANTIDADE_LANCADA;
}
