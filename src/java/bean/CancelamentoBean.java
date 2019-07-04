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
    
    @Setter
    private EspelhoComanda espelhoComanda;
    @Setter
    private double quantidade;
    
    public void init() {
        
    }
    
    public void salvarCancelamento() {
        Cancelamento cancelamento = new Cancelamento();
        cancelamento.setCodigoMotivo(this.espelhoComanda.getCodigoMotivoCancelamento());
        cancelamento.setData(this.espelhoComanda.getDataCancelamento());
        cancelamento.setFoiProduzido(this.espelhoComanda.getFoiProduzido());
        cancelamento.setItem(this.espelhoComanda.getNumeroItem());
        cancelamento.setPedido(this.espelhoComanda.getPedido());
        cancelamento.setProduto(this.espelhoComanda.getReferencia());
        cancelamento.setGarcom(this.espelhoComanda.getVendedor());
        cancelamento.setObservacaoMotivo(this.espelhoComanda.getObservacaoMotivo());
        cancelamento.setObservacaoDestino(this.espelhoComanda.getObservacaoDestino());
        cancelamento.setResponsavel(this.espelhoComanda.getRespansavelCancelamento());
        cancelamento.setQuantidade(this.quantidade);
        this.cancelamentoService.salvar(cancelamento);
    }   
    
}
