package bean;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.MotivoCancelamento;
import servico.MotivoCancelamentoService;

@ManagedBean(name="motivoCancelamentoBean")
@ViewScoped
@Getter @Setter
public class MotivoCancelamentoBean implements Serializable{
    
    @ManagedProperty(value = "#{motivoCancelamentoService}")
    private MotivoCancelamentoService motivoCancelamentoService;
    
    private MotivoCancelamento motivoCancelamento;
    
    private List<MotivoCancelamento> motivoCancelamentos; 
    
    public void init() {
        listarTodos();
        novo();
    }
    
    private void novo() {
        this.motivoCancelamento = null;
        this.motivoCancelamento=new MotivoCancelamento();        
        listarTodos();
    }
    
    
    public void listarTodos() {
       this.motivoCancelamentos = motivoCancelamentoService.pesquisarTodos();
    }
    
    public void salvar() {
        this.motivoCancelamento.setCodigo(motivoCancelamentoService.verificarId());
        this.motivoCancelamento.setNome(motivoCancelamento.getNome().toUpperCase());
        this.motivoCancelamentoService.salvar(motivoCancelamento);
        novo();
    }
    
    public void excluir(MotivoCancelamento motivoCancelamento) {
        this.motivoCancelamentoService.excluir(motivoCancelamento);
        listarTodos();
    }
        
}
