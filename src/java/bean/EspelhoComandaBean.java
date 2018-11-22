package bean;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.EspelhoComanda;
import servico.EspelhoComandaService;

@ManagedBean(name="espelhoComandaBean")
@ViewScoped
public class EspelhoComandaBean implements Serializable{
    
    @Getter @Setter
    @ManagedProperty(value="#{espelhoComandaService}")
    private EspelhoComandaService espelhoComandaService;
    
    @Getter @Setter
    public EspelhoComanda espelhoComanda;    
    
    @Getter @Setter
    public List<EspelhoComanda> espelhoComandas;    
    
    public void init() {
        espelhoComanda=null;
        espelhoComanda=new EspelhoComanda();
        listarAuditoria();
    }
    
    
    public void salvar() {
        this.espelhoComandaService.salvar(this.espelhoComanda);
    }
    
    public void excluir(Integer numero) {
        this.espelhoComandaService.excluir(numero);
    }
    
    public void alterar() {
        this.espelhoComandaService.alterar(espelhoComanda);
    }
    
    public EspelhoComanda buscarPorId(Integer numero){
        return this.espelhoComandaService.buscarPorId(numero);
    }
    
    public void salvarPessoaReipressao(String usuario,String item) {
        this.espelhoComandaService.salvarPessoaQueAutorizouReipressao(usuario, item);
    }
 
    private void listarAuditoria() {
        this.espelhoComandas = this.espelhoComandaService.listarAuditoria();
    }
    
}
