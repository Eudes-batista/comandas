package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Comandas;
import modelo.dto.ItemPedido;
import servico.PesquisaMesasService;

@ManagedBean(name = "pesquisaMesasBean")
@ViewScoped
@Getter
@Setter
public class PesquisaMesasBean implements Serializable {

    @ManagedProperty(value = "#{pesquisaMesasService}")
    private PesquisaMesasService pesquisaMesasService;
    @ManagedProperty(value = "#{mesasBean}")
    private MesasBean mesasBean;

    private String pesquisa;
    private String dataInicial;
    private String dataFinal;
    private String pedido;

    private List<Comandas> comandas;
    private List<ItemPedido> itensPedido;

    public void init() {
        LocalDate localDate = LocalDate.now();
        dataInicial = localDate.toString();
        dataFinal = localDate.toString();
        listarComandas();
    }

    public void listarComandas() {
        this.comandas = pesquisaMesasService.listarComandas(dataInicial, dataFinal);
        comandas.sort((c1, c2) -> c1.getCOMANDA().compareTo(c2.getCOMANDA()));
    }

    public void pesquisarComanda() {
        this.comandas = pesquisaMesasService.pesquisarComandaPorCodigo(pesquisa, dataInicial, dataFinal);
        comandas.sort((c1, c2) -> c1.getCOMANDA().compareTo(c2.getCOMANDA()));
    }

    public void listarItemPorPedido(String pedido) {
        this.pedido = pedido;
        this.itensPedido = this.pesquisaMesasService.listarItemPorPedido(pedido);
    }

    public void imprimirPrecontaMesa(Comandas comandas) {
        this.mesasBean.setMesas(new ArrayList<>());
        this.mesasBean.setPesquisa(comandas.getMESA());
        this.mesasBean.pesquisarMesas();
        this.mesasBean.reipressaoPreconta(comandas.getMESA());
        this.listarComandas();
    }
}
