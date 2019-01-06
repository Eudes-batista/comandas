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
import modelo.Lapt51;
import modelo.Produto;
import modelo.Vendedor;
import modelo.dto.FiltroItemVendido;
import modelo.dto.ItemCanceladoGarcom;
import modelo.dto.ItemVendido;
import relatorio.Relatorio;
import servico.EmpresaService;
import servico.GrupoServico;
import servico.ProdutoService;
import servico.RelatorioItemVendidoService;
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
    @ManagedProperty(value = "#{empresaService}")
    private EmpresaService empresaService;
    @ManagedProperty(value = "#{relatorioItemVendidoService}")
    private RelatorioItemVendidoService relatorioItemVendidoService;
    

    private List<Vendedor> vendedores;
    private List<Lapt51> grupos;
    private List<Produto> produtos;
    private List<ItemVendido> itemVendidos;
    private List<ItemCanceladoGarcom> itemCanceladoGarcons;

    private FiltroItemVendido filtroItemVendido;
    private Empresa empresa;
    private Produto produto;

    private String pesquisa;

    private double total;
    private double quantidade;

    public void init() {
        String data = LocalDate.now().toString();
        this.filtroItemVendido = new FiltroItemVendido();
        this.filtroItemVendido.setDataInicial(data);
        this.filtroItemVendido.setDataFinal(data);
        Relatorio relatorio = new Relatorio();
        this.empresa = relatorio.setEmpresaService(empresaService).getEmpresa();
        listarVendedores();
        listarGrupos();
    }

    public void selecionarProduto(Produto produto) {
        filtroItemVendido.setProduto(produto.getReferencia());
    }

    private void listarVendedores() {
        this.vendedores = this.vendedorService.listarVendedor();
    }

    private void listarGrupos() {
        this.grupos = this.grupoServico.listarGrupos();
    }

    public void listarProdutos() {
        this.produtos = this.produtoService.lsitarProdutos();
    }

    public void pesquisarProduto() {
        this.produtos = this.produtoService.listarPorReferenciaDescricaoCodigoBarra(pesquisa.toUpperCase());
    }

    public void filtrar() {
        if (!filtroItemVendido.isCancelado()) {
            this.itemVendidos = this.relatorioItemVendidoService.listaItensVendidos(filtroItemVendido);
            somarTotais();
        }else{
            this.itemCanceladoGarcons = this.relatorioItemVendidoService.listarItensCanceladosPorGarcom(filtroItemVendido);
            this.quantidade = this.itemCanceladoGarcons.stream().filter(item -> item.getCANCELAMENTO() != null).mapToDouble(ItemCanceladoGarcom::getCANCELAMENTO).sum();
            this.total = this.itemCanceladoGarcons.stream().filter(item -> item.getCANCELAMENTO() != null).mapToDouble(ItemCanceladoGarcom::getTOTAL).sum();
        }
    }

    private void somarTotais() {
        this.total = this.itemVendidos.stream().mapToDouble(ItemVendido::getTOTAL).sum();
        this.quantidade = this.itemVendidos.stream().mapToDouble(ItemVendido::getQUANTIDADE).sum();

    }
}
