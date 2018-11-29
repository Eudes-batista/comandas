package bean;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Comandas;
import servico.AuditoriaService;

@ManagedBean(name="auditoriaBean")
@ViewScoped
@Getter @Setter
public class AuditoriaBean implements Serializable{

    @ManagedProperty(value="#{auditoriaService}")
    private AuditoriaService auditoriaService;
    
    private String pesquisa;
    
    private List<Comandas> comandas;
    
     public void listarComandas() {
        this.comandas = auditoriaService.listarComandas();
        comandas.sort((c1, c2) -> c1.getCOMANDA().compareTo(c2.getCOMANDA()));
    }

    public void pesquisarComanda() {
        this.comandas = auditoriaService.pesquisarComandaPorCodigo(pesquisa);
        comandas.sort((c1, c2) -> c1.getCOMANDA().compareTo(c2.getCOMANDA()));
    }
}
