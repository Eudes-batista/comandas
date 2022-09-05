package modelo.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemAcompanhamentoTransferencia implements Serializable{
    
    private Integer ITEM;
    private String PEDIDO;

    public ItemAcompanhamentoTransferencia() {
    }

    public ItemAcompanhamentoTransferencia(Integer ITEM, String PEDIDO) {
        this.ITEM = ITEM;
        this.PEDIDO = PEDIDO;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.ITEM);
        hash = 43 * hash + Objects.hashCode(this.PEDIDO);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemAcompanhamentoTransferencia other = (ItemAcompanhamentoTransferencia) obj;
        if (!Objects.equals(this.ITEM, other.ITEM)) {
            return false;
        }
        return Objects.equals(this.PEDIDO, other.PEDIDO);
    }

    @Override
    public String toString() {
        return "ItemAcompanhamentoTransferencia{" + "ITEM=" + ITEM + ", PEDIDO=" + PEDIDO + '}';
    }
    
    
}
