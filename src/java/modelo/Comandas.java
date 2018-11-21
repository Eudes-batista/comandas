
package modelo;

import java.io.Serializable;
import java.util.Objects;

public class Comandas implements Serializable{

   private String comanda;
   private String mesa;
   private Double Total;
   private String status;
   private String pessoasMesa;
   private String pedido;

    public Comandas() {
    }

    public Comandas(String comanda) {
        this.comanda = comanda;
    }

    public Comandas(String comanda, String mesa) {
        this.comanda = comanda;
        this.mesa = mesa;
    }
    
    public Comandas(String comanda, Double Total, String status,String pessoasMesa,String pedido) {
        this.comanda = comanda;
        this.Total = Total;
        this.status = status;
        this.pessoasMesa = pessoasMesa;
        this.pedido = pedido;
    }

    public Comandas(String comanda, Double Total, String status,String mesa, String pessoasMesa, String pedido) {
        this.comanda = comanda;
        this.mesa = mesa;
        this.Total = Total;
        this.status = status;
        this.pessoasMesa = pessoasMesa;
        this.pedido = pedido;
    }

    public String getComanda() {
        return comanda;
    }

    public void setComanda(String comanda) {
        this.comanda = comanda;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double Total) {
        this.Total = Total;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.comanda);
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
        final Comandas other = (Comandas) obj;
        return Objects.equals(this.comanda, other.comanda);
    }  

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getPessoasMesa() {
        return pessoasMesa;
    }

    public void setPessoasMesa(String pessoasMesa) {
        this.pessoasMesa = pessoasMesa;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }
}
