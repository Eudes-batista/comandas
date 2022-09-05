package bean;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.GrupoAcompanhamento;
import servico.GrupoAcompanhamentoService;

@ManagedBean(name = "grupoAcompanhamentoBean")
@ViewScoped
@Getter
@Setter
public class GrupoAcompanhamentoBean implements Serializable {

    @ManagedProperty(value = "#{grupoAcompanhamentoService}")
    private GrupoAcompanhamentoService grupoAcompanhamentoService;

    private List<GrupoAcompanhamento> grupoAcompanhamentos;
    private GrupoAcompanhamento grupoAcompanhamento;

    public void init() {
        listarTodos();
        novo();
    }

    private void listarTodos() {
        grupoAcompanhamentos = grupoAcompanhamentoService.pesquisarTodos();
    }

    private void novo() {
        grupoAcompanhamento =null;
        grupoAcompanhamento = new GrupoAcompanhamento();
    }
    
    
    public void salvar() {
        grupoAcompanhamento.setCodigo(grupoAcompanhamentoService.verificarId());
        grupoAcompanhamento.setNome(grupoAcompanhamento.getNome().toUpperCase());
        grupoAcompanhamentoService.salvar(grupoAcompanhamento);
        init();
    }
    
    public void excluir(GrupoAcompanhamento grupoAcompanhamento) {
        grupoAcompanhamentoService.excluir(grupoAcompanhamento);
        listarTodos();
    }
    

}
