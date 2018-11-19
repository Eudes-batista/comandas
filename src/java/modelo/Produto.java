package modelo;

import java.io.Serializable;
import java.util.Objects;

public class Produto implements Serializable {

    private String REFERENCIA;
    private String DESCRICAO;
    private double PRECO;

    public Produto() {
    }

    public Produto(String referencia, String descricao, double preco) {
        this.REFERENCIA = referencia;
        this.DESCRICAO = descricao;
        this.PRECO = preco;
    }

    public String getReferencia() {
        return REFERENCIA;
    }

    public void setReferencia(String referencia) {
        this.REFERENCIA = referencia;
    }

    public String getDescricao() {
        return DESCRICAO;
    }

    public void setDescricao(String descricao) {
        this.DESCRICAO = descricao;
    }

    public double getPreco() {
        return PRECO;
    }

    public void setPreco(double preco) {
        this.PRECO = preco;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.REFERENCIA);
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
        final Produto other = (Produto) obj;
        return Objects.equals(this.REFERENCIA, other.REFERENCIA);
    }

    @Override
    public String toString() {
        return "Produto{" + "referencia=" + REFERENCIA + ", descricao=" + DESCRICAO + ", preco=" + PRECO +"}'";
    }
    
}
