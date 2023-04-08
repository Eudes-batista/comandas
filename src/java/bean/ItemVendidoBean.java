package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.ItemVendido;
import servico.ItemVendidoService;

@ManagedBean(name = "itemVendidoBean")
@ViewScoped
@Getter
@Setter
public class ItemVendidoBean implements Serializable {

    @ManagedProperty(value = "#{itemVendidoService}")
    private ItemVendidoService itemVendidoService;

    private FiltroVendaDetalhe filtroVendaDetalhe;

    private List<ItemVendido> itemVendidos;

    public void init() {
        filtroVendaDetalhe = new FiltroVendaDetalhe();
        filtroVendaDetalhe.setDataInicial(LocalDate.now().toString());
        filtroVendaDetalhe.setDataFinal(LocalDate.now().toString());
        listarItensVendidos();
    }

    public void listarItensVendidos() {
        this.itemVendidos = this.itemVendidoService.listarItensVendidos(filtroVendaDetalhe);

    }

}
