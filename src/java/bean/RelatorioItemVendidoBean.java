package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Lapt51;
import modelo.Produto;
import modelo.Vendedor;
import modelo.dto.FiltroItemVendido;
import servico.GrupoServico;
import servico.ProdutoService;
import servico.VendedorService;

@ManagedBean(name = "relatorioItemVendidoBean")
@ViewScoped
@Getter
@Setter
public class RelatorioItemVendidoBean implements Serializable {

    @ManagedProperty(value = "#{vendedorService}")
    private VendedorService vendedorService;

    @ManagedProperty(value = "#{grupoServico}")
    private GrupoServico grupoServico;

    @ManagedProperty(value = "#{produtoServico}")
    private ProdutoService produtoService;

    private List<Vendedor> vendedores;
    private List<Lapt51> grupos;
    private List<Produto> produtos;

    private FiltroItemVendido filtroItemVendido;

    public void init() {
        this.filtroItemVendido = new FiltroItemVendido();
        String data = LocalDate.now().toString();
        this.filtroItemVendido.setDataInicial(data);
        this.filtroItemVendido.setDataFinal(data);
        listarVendedores();
        listarGrupos();
        listarProdutos();
    }

    private void listarVendedores() {
        this.vendedores = this.vendedorService.listarVendedor();
    }

    private void listarGrupos() {
        this.grupos = this.grupoServico.listarGrupos();
    }

    private void listarProdutos() {
        this.produtos = this.produtoService.lsitarProdutos();
    }

}
