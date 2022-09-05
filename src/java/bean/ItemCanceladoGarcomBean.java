package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.dto.FiltroItemCancelado;
import modelo.dto.ItemCanceladoGarcom;
import servico.ItemCanceladoGarcomService;

@ManagedBean(name = "ItemCanceladoGarcomBean")
@ViewScoped
@Getter
@Setter
public class ItemCanceladoGarcomBean implements Serializable {

    @ManagedProperty(value = "#{itemCanceladoGarcomService}")
    private ItemCanceladoGarcomService itemCanceladoGarcomService;

    private List<ItemCanceladoGarcom> itemCanceladoGarcoms;

    private FiltroItemCancelado filtroItemCancelado;
    
    private ItemCanceladoGarcom itemCanceladoGarcom;

    public void init() {
        this.filtroItemCancelado = new FiltroItemCancelado();
        this.filtroItemCancelado.setDataInical(LocalDate.now().toString());
        this.filtroItemCancelado.setDataFinal(LocalDate.now().toString());
        listarItensCancelados();
    }

    public void listarItensCancelados() {
        this.itemCanceladoGarcoms = this.itemCanceladoGarcomService.listarItensCanceladosPorGarcom(this.filtroItemCancelado);
    }

    public void atualizarObservacaoDestino(){
        this.itemCanceladoGarcomService.atualizarObservacaoDestino(this.itemCanceladoGarcom.getNUMERO(), this.itemCanceladoGarcom.getDESTINO().toUpperCase());
    }
    
    public void selecionarItemCancelado(ItemCanceladoGarcom itemCanceladoGarcom) {
        this.itemCanceladoGarcom =itemCanceladoGarcom;
    }
    
    
}
