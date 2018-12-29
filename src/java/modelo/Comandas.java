package modelo;

import java.io.Serializable;
import java.util.Objects;

public class Comandas implements Serializable {

    private String COMANDA;
    private String MESA;
    private Double TOTAL;
    private String STATUS;
    private String PESSOAS;
    private String PEDIDO;
    private String PAGANTES;
    private String VENDEDOR;
    private String DESCRICAO;
    private Double PORCENTAGEM;

    public Comandas() {
    }

    public Comandas(String comanda) {
        this.COMANDA = comanda;
    }

    public Comandas(String comanda, String mesa) {
        this.COMANDA = comanda;
        this.MESA = mesa;
    }

    public Comandas(String comanda, Double Total, String status, String pessoasMesa, String pedido, String vendedor) {
        this.COMANDA = comanda;
        this.TOTAL = Total;
        this.STATUS = status;
        this.PESSOAS = pessoasMesa;
        this.PEDIDO = pedido;
        this.VENDEDOR = vendedor;
    }

    public Comandas(String COMANDA, String MESA, Double TOTAL, String STATUS, String PESSOAS, String PEDIDO, String PAGANTES, String VENDEDOR, String DESCRICAO) {
        this.COMANDA = COMANDA;
        this.MESA = MESA;
        this.TOTAL = TOTAL;
        this.STATUS = STATUS;
        this.PESSOAS = PESSOAS;
        this.PEDIDO = PEDIDO;
        this.PAGANTES = PAGANTES;
        this.VENDEDOR = VENDEDOR;
        this.DESCRICAO = DESCRICAO;
    }

    public String getCOMANDA() {
        return COMANDA;
    }

    public void setCOMANDA(String COMANDA) {
        this.COMANDA = COMANDA;
    }

    public Double getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(Double TOTAL) {
        this.TOTAL = TOTAL;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.COMANDA);
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
        return Objects.equals(this.COMANDA, other.COMANDA);
    }

    public String getVENDEDOR() {
        return VENDEDOR;
    }

    public void setVENDEDOR(String VENDEDOR) {
        this.VENDEDOR = VENDEDOR;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getMESA() {
        return MESA;
    }

    public void setMESA(String MESA) {
        this.MESA = MESA;
    }

    public String getPESSOAS() {
        return PESSOAS;
    }

    public void setPESSOAS(String PESSOAS) {
        this.PESSOAS = PESSOAS;
    }

    public String getPEDIDO() {
        return PEDIDO;
    }

    public void setPEDIDO(String PEDIDO) {
        this.PEDIDO = PEDIDO;
    }

    public String getPAGANTES() {
        return PAGANTES;
    }

    public void setPAGANTES(String PAGANTES) {
        this.PAGANTES = PAGANTES;
    }

    public String getDESCRICAO() {
        return DESCRICAO;
    }

    public void setDESCRICAO(String DESCRICAO) {
        this.DESCRICAO = DESCRICAO;
    }

    @Override
    public String toString() {
        return "Comandas{" + "COMANDA=" + COMANDA + ", MESA=" + MESA + ", TOTAL=" + TOTAL + ", STATUS=" + STATUS + ", PESSOAS=" + PESSOAS + ", PEDIDO=" + PEDIDO + ", PAGANTES=" + PAGANTES + ", VENDEDOR=" + VENDEDOR + ", DESCRICAO=" + DESCRICAO + '}';
    }

    public Double getPORCENTAGEM() {
        return PORCENTAGEM;
    }

    public void setPORCENTAGEM(Double PORCENTAGEM) {
        this.PORCENTAGEM = PORCENTAGEM;
    }

}
