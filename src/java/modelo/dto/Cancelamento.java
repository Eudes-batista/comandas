package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cancelamento implements Serializable {

    private String pedidos;
    private String usuario;
    private String status;
    private int motivo;

    public Cancelamento() {
    }

    public Cancelamento(String pedidos, String usuario, String status, int motivo) {
        this.pedidos = pedidos;
        this.usuario = usuario;
        this.status = status;
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "Cancelamento{" + "pedidos=" + pedidos + ", usuario=" + usuario + ", status=" + status + ", motivo=" + motivo + '}';
    }
    
}
