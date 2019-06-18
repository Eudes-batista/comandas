package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.dto.FiltroRelatorioPreconta;
import modelo.dto.RelatorioComandaConsumo;
import servico.RelatorioComandaConsumoService;

@ManagedBean(name = "relatorioComandaConsumoBean")
@ViewScoped
@Getter
@Setter
public class RelatorioComandaConsumoBean implements Serializable {

    @ManagedProperty(value = "#{relatorioComandaConsumoService}")
    private RelatorioComandaConsumoService relatorioComandaConsumoService;

    private FiltroRelatorioPreconta filtroRelatorioPreconta;

    private List<RelatorioComandaConsumo> comandasConsumo;

    public void init() {
        this.filtroRelatorioPreconta = new FiltroRelatorioPreconta();
        String dataAtual = LocalDate.now().toString();
        this.filtroRelatorioPreconta.setDataInicial(dataAtual);
        this.filtroRelatorioPreconta.setDataFinal(dataAtual);
        this.listarComandasEmConsumo();
    }

    public void listarComandasEmConsumo() {
        this.comandasConsumo = this.relatorioComandaConsumoService.listarComandasEmConsumo(filtroRelatorioPreconta);
    }

}
