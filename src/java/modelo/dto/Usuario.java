package modelo.dto;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario{
    
    private String Usuario;
    private String senha;

    public Usuario() {
    }

    public Usuario(String Usuario, String senha) {
        this.Usuario = Usuario;
        this.senha = senha;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.Usuario);
        hash = 53 * hash + Objects.hashCode(this.senha);
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
        if (!Objects.equals(this.Usuario, other.Usuario)) {
            return false;
        }
        return Objects.equals(this.senha, other.senha);
    }

    @Override
    public String toString() {
        return "Usuario{" + "Usuario=" + Usuario + ", senha=" + senha + '}';
    }

}
