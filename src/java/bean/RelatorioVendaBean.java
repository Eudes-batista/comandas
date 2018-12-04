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
import modelo.dto.VendaGarcom;
import servico.RelatorioVendaService;

@ManagedBean(name="relatorioVendaBean")
@ViewScoped
@Getter @Setter
public class RelatorioVendaBean implements Serializable{
    
    @ManagedProperty(value="#{relatorioVendaService}")
    private RelatorioVendaService relatorioVendaService;
    
    private List<VendaGarcom> vendaGarcoms;
    
    private FiltroVendaDetalhe filtroVendaDetalhe;
    
    public void init() {
        filtroVendaDetalhe=new FiltroVendaDetalhe();
        LocalDate date = LocalDate.now();
        filtroVendaDetalhe.setDataInicial(date.toString());
        filtroVendaDetalhe.setDataFinal(date.toString());
    }
    
    
    public void listarVendasGarcom() {
        this.vendaGarcoms= this.relatorioVendaService.listarVendasGarcom(filtroVendaDetalhe);
    }
    
}
