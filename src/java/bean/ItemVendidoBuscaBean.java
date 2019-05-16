
package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.dto.FiltroItemVendido;
import modelo.dto.ItemPedido;
import servico.ItemVendidoService;

@ManagedBean(name="itemVendidoBuscaBean")
@ViewScoped
@Getter @Setter
public class ItemVendidoBuscaBean implements Serializable{

    @ManagedProperty(value="#{itemVendidoService}")
    private ItemVendidoService itemVendidoService;
    
    private List<ItemPedido> itemVendidos;
    
    private FiltroItemVendido filtroItemVendido;
    
    public void init() {
        this.filtroItemVendido = new FiltroItemVendido();
        String data = LocalDate.now().toString();
        this.filtroItemVendido.setDataInicial(data);
        this.filtroItemVendido.setDataFinal(data);
        this.filtroItemVendido.setProduto("");
        this.listarTodos();
    }
    
    public void listarTodos() {
        this.filtroItemVendido.setProduto(!this.filtroItemVendido.getProduto().isEmpty()? this.filtroItemVendido.getProduto().toUpperCase() :"");
        this.itemVendidos =this.itemVendidoService.buscarItensVendidos(filtroItemVendido);
    }
    
}
