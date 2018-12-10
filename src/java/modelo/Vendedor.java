
package modelo;

import java.io.Serializable;
import java.util.Objects;


public class Vendedor implements Serializable{

    private String CODIGO;
    private String NOME;

    public Vendedor() {
    }

    public Vendedor(String CODIGO, String NOME) {
        this.CODIGO = CODIGO;
        this.NOME = NOME;
    }

    public String getId() {
        return CODIGO;
    }

    public void setId(String id) {
        this.CODIGO = id;
    }

    public String getNome() {
        return NOME;
    }

    public void setNome(String nome) {
        this.NOME = nome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.CODIGO);
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
        final Vendedor other = (Vendedor) obj;
        return Objects.equals(this.CODIGO, other.CODIGO);
    }

       
    
}
