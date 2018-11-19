package bean;

import java.io.Serializable;
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
    
    public void init() {
        espelhoComanda=null;
        espelhoComanda=new EspelhoComanda();
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
}
