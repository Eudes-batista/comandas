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
import modelo.dto.VendaDetalhe;
import modelo.dto.VendaGarcom;
import servico.VendaDetalheService;

@ManagedBean(name = "vendaDetalheBean")
@ViewScoped
@Getter
@Setter
public class VendaDetalheBean implements Serializable {

    @ManagedProperty(value = "#{vendaDetalheService}")
    private VendaDetalheService vendaDetalheService;

    private List<VendaGarcom> vendasGarcom;
    private List<VendaDetalhe> vendaDetalheRejeicaos;
    private List<VendaDetalhe> vendaItens;

    private FiltroVendaDetalhe filtroVendaDetalhe;

    public void init() {
        filtroVendaDetalhe = new FiltroVendaDetalhe();
        filtroVendaDetalhe.setDataInicial(LocalDate.now().toString());
        filtroVendaDetalhe.setDataFinal(LocalDate.now().toString());
        listarVendasGarcom();
    }

    public void listarVendasGarcom() {
        this.vendasGarcom = this.vendaDetalheService.listarVendaGarcom(filtroVendaDetalhe);

    }

    public int getQuantidadeRejeicaoDezPorcento(String garcom) {
        filtroVendaDetalhe.setCargom(garcom);
        return vendaDetalheService.listarReijeicaoPorcentagemPorVendedor(filtroVendaDetalhe).size();
    }

    public void listarRejeicaoPorcentagemPorGarcom(String garcom) {
        filtroVendaDetalhe.setCargom(garcom);
        this.vendaDetalheRejeicaos = this.vendaDetalheService.listarRejeicaoDezPorcentoPorGarcom(filtroVendaDetalhe);
    }

    public void listarItensVendidosPorGarcom(String garcom) {
        filtroVendaDetalhe.setCargom(garcom);
        this.vendaItens=this.vendaDetalheService.listarItensVendidosPorGarcom(filtroVendaDetalhe);
    }
     
    
}
