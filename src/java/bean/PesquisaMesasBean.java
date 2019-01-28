package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Comandas;
import servico.PesquisaMesasService;

@ManagedBean(name = "pesquisaMesasBean")
@ViewScoped
@Getter
@Setter
public class PesquisaMesasBean implements Serializable {

    @ManagedProperty(value = "#{pesquisaMesasService}")
    private PesquisaMesasService pesquisaMesasService;

    private String pesquisa;
    private String dataInicial;
    private String dataFinal;

    private List<Comandas> comandas;

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
}
