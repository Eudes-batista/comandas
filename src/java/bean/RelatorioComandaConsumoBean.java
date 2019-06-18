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
import modelo.dto.FiltroRelatorioPreconta;
import modelo.dto.RelatorioComandaConsumo;
import relatorio.Relatorio;
import servico.EmpresaService;
import servico.RelatorioComandaConsumoService;

@ManagedBean(name = "relatorioComandaConsumoBean")
@ViewScoped
@Getter
@Setter
public class RelatorioComandaConsumoBean implements Serializable {

    @ManagedProperty(value = "#{relatorioComandaConsumoService}")
    private RelatorioComandaConsumoService relatorioComandaConsumoService;

    @ManagedProperty(value = "#{empresaService}")
    private EmpresaService empresaService;

    private Empresa empresa;

    private FiltroRelatorioPreconta filtroRelatorioPreconta;

    private List<RelatorioComandaConsumo> comandasConsumo;

    private String pesquisa;

    public void init() {
        this.filtroRelatorioPreconta = new FiltroRelatorioPreconta();
        String dataAtual = LocalDate.now().toString();
        this.filtroRelatorioPreconta.setDataInicial(dataAtual);
        this.filtroRelatorioPreconta.setDataFinal(dataAtual);
        Relatorio relatorio = new Relatorio();
        this.empresa = relatorio.setEmpresaService(this.empresaService).getEmpresa();
        relatorio = null;
        this.listarComandasEmConsumo();
    }

    public void listarComandasEmConsumo() {
        this.filtroRelatorioPreconta.setComanda(pesquisa);
        this.filtroRelatorioPreconta.setMesa(pesquisa);
        this.filtroRelatorioPreconta.setPedido(pesquisa);
        this.comandasConsumo = this.relatorioComandaConsumoService.listarComandasEmConsumo(filtroRelatorioPreconta);
    }

}
