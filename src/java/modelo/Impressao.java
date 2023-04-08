package modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.validator.constraints.NotBlank;

@ManagedBean(name = "impressao")
@ViewScoped
public class Impressao implements Serializable{
    
    @NotBlank(message = "campo sub-grupo não pode ser nulo ou vazio")
    private String subgrupo;
    @NotBlank(message = "campo caminho da impressora não pode ser nulo ou vazio")
    private String caminhoImpressora;

    public Impressao() {
    }

    public Impressao(String subgrupo, String caminhoImpressora) {
        this.subgrupo = subgrupo;
        this.caminhoImpressora = caminhoImpressora;
    }    
    
    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public String getCaminhoImpressora() {
        return caminhoImpressora;
    }

    public void setCaminhoImpressora(String caminhoImpressora) {
        this.caminhoImpressora = caminhoImpressora;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.subgrupo);
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
        final Impressao other = (Impressao) obj;
        return Objects.equals(this.subgrupo, other.subgrupo);
    }

    
    
}
