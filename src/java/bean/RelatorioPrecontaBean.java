package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Empresa;
import modelo.Vendedor;
import modelo.dto.FiltroRelatorioPreconta;
import modelo.dto.RelatorioPreconta;
import relatorio.Relatorio;
import servico.EmpresaService;
import servico.RelatorioPrecontaService;
import servico.VendedorService;

@ManagedBean(name = "relatorioPrecontaBean")
@ViewScoped
@Getter
@Setter
public class RelatorioPrecontaBean implements Serializable {

    @ManagedProperty(value = "#{relatorioPrecontaService}")
    private RelatorioPrecontaService relatorioPrecontaService;
    @ManagedProperty(value = "#{vendedorService}")
    private VendedorService vendedorService;
    @ManagedProperty(value = "#{empresaService}")
    private EmpresaService empresaService;

    private List<RelatorioPreconta> relatorioPrecontas;
    private List<Vendedor> vendedores;

    private FiltroRelatorioPreconta filtroRelatorioPreconta;
    private Empresa empresa;

    public void init() {
        LocalDate data = LocalDate.now();
        filtroRelatorioPreconta = new FiltroRelatorioPreconta();
        filtroRelatorioPreconta.setDataInicial(data.toString());
        filtroRelatorioPreconta.setDataFinal(data.toString());
        listarVendedores();
        Relatorio relatorio = new Relatorio();
        this.empresa = relatorio.setEmpresaService(empresaService).getEmpresa();
    }

    public void listarTudo() {
        System.out.println("filtroRelatorioPreconta = " + filtroRelatorioPreconta);
        this.relatorioPrecontas = relatorioPrecontaService.listarTudo(filtroRelatorioPreconta);
    }

    private void listarVendedores() {
        this.vendedores = this.vendedorService.listarVendedor();
    }

}
