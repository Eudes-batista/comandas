package bean;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Cancelamento;
import modelo.EspelhoComanda;
import servico.CancelamentoService;

@ManagedBean(name="cancelamentoBean")
@ViewScoped
public class CancelamentoBean implements Serializable{
    
    @Getter @Setter
    @ManagedProperty(value = "#{cancelamentoService}")
    private CancelamentoService cancelamentoService;
    
    @Getter @Setter
    private List<Cancelamento> cancelamentos;
    
    public void init() {
        
    }
    
    public void salvarCancelamento(Cancelamento cancelamento) {        
        this.cancelamentoService.salvar(cancelamento);
    }
    
    
    
}
