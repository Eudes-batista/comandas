package bean;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Acompanhamento;
import modelo.GrupoAcompanhamento;
import servico.AcompanhamentoService;
import servico.GrupoAcompanhamentoService;

@ManagedBean(name="acompanhamentoBean")
@ViewScoped
@Getter @Setter
public class AcompanhamentoBean implements Serializable{
    
    @ManagedProperty(value="#{acompanhamentoService}")
    private AcompanhamentoService acompanhamentoService;
    
    @ManagedProperty(value="#{grupoAcompanhamentoService}")
    private GrupoAcompanhamentoService grupoAcompanhamentoService;
    
    private List<GrupoAcompanhamento> grupoAcompanhamentos;
    private List<Acompanhamento> acompanhamentos;
    private Acompanhamento acompanhamento;
    
    private String pesquisa;    
    
    public void init() {
        listarTodos();
        novo();
    }
    
    private void listarTodos() {
        acompanhamentos=acompanhamentoService.pesquisarTodos();
        grupoAcompanhamentos=grupoAcompanhamentoService.pesquisarTodos();
    }
    
    private void novo() {
        acompanhamento=null;
        acompanhamento=new Acompanhamento();
    }
    
    public void salvar() {
        acompanhamento.setCodigo(acompanhamentoService.verificarId());
        acompanhamento.setNome(acompanhamento.getNome().toUpperCase());
        acompanhamentoService.salvar(acompanhamento);
        init();
    }
    
    public void excluir(Acompanhamento acompanhamento) {
        acompanhamentoService.excluir(acompanhamento);
        listarTodos();
    }
    
    public void pesquisar() {
        acompanhamentos=acompanhamentoService.pesquisar(pesquisa.toUpperCase());
    }
    
    
}
