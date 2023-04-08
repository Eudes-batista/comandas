package modelo.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario implements Serializable {

    private String NOME;
    private String SENHA;
    private String GERENTE;

    public Usuario() {
    }

    public Usuario(String Usuario, String senha) {
        this.NOME = Usuario;
        this.SENHA = senha;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.NOME);
        hash = 53 * hash + Objects.hashCode(this.SENHA);
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.NOME, other.NOME)) {
            return false;
        }
        return Objects.equals(this.SENHA, other.SENHA);
    }

    @Override
    public String toString() {
        return "Usuario{" + "Usuario=" + NOME + ", senha=" + SENHA + '}';
    }

}
