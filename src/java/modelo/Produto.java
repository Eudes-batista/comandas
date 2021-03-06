package modelo;

import java.io.Serializable;
import java.util.Objects;

public class Produto implements Serializable {

    private String REFERENCIA;
    private String DESCRICAO;
    private Double PRECO;
    private String GRUPO;
    private String UNIDADE;

    public Produto() {
    }

    public Produto(String referencia, String descricao, Double preco) {
        this.REFERENCIA = referencia;
        this.DESCRICAO = descricao;
        this.PRECO = preco;
    }

    public Produto(String REFERENCIA, String DESCRICAO, Double PRECO, String GRUPO) {
        this.REFERENCIA = REFERENCIA;
        this.DESCRICAO = DESCRICAO;
        this.PRECO = PRECO;
        this.GRUPO = GRUPO;
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

    public Double getPreco() {
        return PRECO;
    }

    public void setPreco(Double preco) {
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

    public String getGRUPO() {
        return GRUPO;
    }

    public void setGRUPO(String GRUPO) {
        this.GRUPO = GRUPO;
    }

    public String getUNIDADE() {
        return UNIDADE;
    }

    public void setUNIDADE(String UNIDADE) {
        this.UNIDADE = UNIDADE;
    }
    
}
