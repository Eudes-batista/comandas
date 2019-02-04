package bean;

import controle.ControlePedido;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import modelo.AtalhoFastFood;
import modelo.Comandas;
import modelo.Configuracao;
import modelo.EspelhoComanda;
import modelo.Lancamento;
import modelo.Lapt51;
import modelo.Mesa;
import modelo.Produto;
import modelo.Sosa98;
import modelo.Sosa98Id;
import modelo.dto.Usuario;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import servico.AtalhoFastFoodService;
import servico.ComandaService;
import servico.GrupoServico;
import servico.ProdutoService;
import util.GerenciaArquivo;

@ManagedBean(name = "fastFoodBean")
@ViewScoped
@Getter
@Setter
public class FastFoodBean implements Serializable {

    @ManagedProperty(value = "#{grupoServico}")
    private GrupoServico grupoServico;
    @ManagedProperty(value = "#{produtoServico}")
    private ProdutoService produtoServico;
    @ManagedProperty(value = "#{atalhoFastFoodService}")
    private AtalhoFastFoodService atalhoFastFoodService;
    @ManagedProperty(value = "#{controle}")
    private ComandaService controleService;
    @ManagedProperty(value = "#{espelhoComandaBean}")
    private EspelhoComandaBean espelhoComandaBean;
    @ManagedProperty(value = "#{produtoBean}")
    private ProdutoBean produtoBean;
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;
    @ManagedProperty(value = "#{mesasBean}")
    private MesasBean mesasBean;

    private AtalhoFastFood atalhoFastFood;
    private Usuario usuario;
    private Comandas comandas;
    private ControlePedido controlePedido;
    private Produto produto;
    private Lancamento lancamento;
    private Configuracao configuracao;

    private List<Lapt51> grupos;
    private List<Lancamento> lancamentos;
    private List<Produto> produtos;
    private List<Comandas> listaComandas;

    private String pesquisa;
    private double quantidade;
    private double total;

    public void init() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        if (this.usuario == null) {
            try {
                Faces.redirect("login.jsf");
            } catch (IOException ex) {
                Messages.addGlobalError("Erro ao redireciona para a pagina de login.");
            }
            return;
        }
        this.atalhoFastFood = atalhoFastFoodService.carregar();
        this.grupos = grupoServico.listarGrupos();
        this.produtos = produtoServico.lsitarProdutosFastFood();
        this.lancamentos = new ArrayList<>();
        this.comandas = new Comandas();
        this.configuracao=new GerenciaArquivo().bucarInformacoes().getConfiguracao();
    }

    public void pesquisarProduto() {
        this.produtos = this.produtoServico.listarPorReferenciaDescricaoCodigoBarraFastFood(pesquisa);
    }

    public void pesquisarPorGrupo(Lapt51 lapt51) {
        this.produtos = this.produtoServico.lsitarProdutoPorGrupoFastFood(lapt51.getT51cdgrp());
    }

    public void buscarProduto(String refencia) {
        Produto produtoEncontrado = produtoServico.buscarProdutoFastFood(refencia);
        if (this.produto == null) {
            if (produtoEncontrado.getUNIDADE().contains("KG")) {
                this.produto = produtoEncontrado;
                this.quantidade = 0;
                PrimeFaces.current().executeScript("PF('dialogQuantidade').show();");
                return;
            }
        }
        adicionarItem(produtoEncontrado);
    }

    public void definirQuantidade() {
        if (validarQuantidade()) {
            Messages.addGlobalWarn("Quantidade tem que ser maior que zero.");
            return;
        }
        this.adicionarItem(this.produto);
        PrimeFaces.current().executeScript("PF('dialogQuantidade').hide();");
        this.produto = null;
        this.quantidade = 1;
    }

    public void adicionarItem(Produto p) {
        if (p.getUNIDADE().contains("KG")) {
            if (this.produto == null) {
                this.produto = p;
                this.quantidade = 0;
                PrimeFaces.current().executeScript("PF('dialogQuantidade').show();");
                return;
            }
        }
        Lancamento lancamentoItem = new Lancamento();
        lancamentoItem.setReferencia(p.getReferencia());
        lancamentoItem.setDescricao(p.getDescricao());
        this.quantidade = this.quantidade == 0 ? 1 : this.quantidade;
        lancamentoItem.setQuantidade(this.quantidade);
        lancamentoItem.setPreco(p.getPreco());
        lancamentoItem.setPrecoTotal(p.getPreco() * quantidade);
        lancamentoItem.setImprimir("0");
        lancamentoItem.setStatus("P");
        lancamentoItem.setVendedor(this.usuario.getNOME());
        this.lancamentos.add(lancamentoItem);
        this.total = this.lancamentos.stream().mapToDouble(Lancamento::getPrecoTotal).sum();
    }

    private boolean validarQuantidade() {
        return this.quantidade == 0;
    }

    public void removerItem(Lancamento lancamento) {
        this.lancamentos.remove(lancamento);
        this.excluir(lancamento);
        this.total = this.lancamentos.stream().mapToDouble(Lancamento::getPrecoTotal).sum();
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void finalizar() {
        if (this.comandas.getCOMANDA() == null) {
            if (!this.lancamentos.isEmpty()) {
                PrimeFaces.current().executeScript("PF('dialogFinalizar').show();");
            }
            return;
        }
        this.controlePedido = new ControlePedido(controleService, this.comandas.getCOMANDA());
        List<Lancamento> lancamentosAdicionados = this.produtoBean.getLancamentosAdicionados();
        String pedido = controlePedido.gerarNumero();
        this.produtoBean.setPedido(pedido);
        this.comandas.setPEDIDO(pedido);
        lancamentos.stream().filter(la -> la.getImprimir().equals("0")).forEach((l) -> {
            salvar(l, pedido);
            if (lancamentosAdicionados.size() != lancamentos.size()) {
                lancamentosAdicionados.add(l);
            }
        });
        this.produtoBean.setComanda(this.comandas.getCOMANDA());
        this.produtoBean.setMesa(this.comandas.getCOMANDA());
        realizarImpressao();
        this.comandas = null;
        this.lancamentos = null;
        this.comandas = new Comandas();
        this.lancamentos = new ArrayList<>();
        this.total = 0;
        novoProduto();
        PrimeFaces.current().executeScript("PF('dialogFinalizar').hide();");
    }

    private void realizarImpressao() {
        switch (this.configuracao.getCondicaoParaImpressao()) {
            case "PP":
                this.produtoBean.imprimirTodos();
                PrimeFaces.current().ajax().update("frmDialogMensagem:dlImpressao");
                PrimeFaces.current().ajax().update("frm:total");
                this.imprimirPrecontaMesa();
                break;
            case "P":
                this.produtoBean.imprimirTodos();
                PrimeFaces.current().ajax().update("frmDialogMensagem:dlImpressao");
                PrimeFaces.current().ajax().update("frm:total");
                break;
            default:
                this.imprimirPrecontaMesa();
                this.espelhoComandaBean.getEspelhoComandaService().atualizarStatusImpressao(this.comandas.getPEDIDO());
                break;
        }
    }

    public void novoProduto() {
        this.produto = null;
    }

    private void salvar(Lancamento lancamento, String pedido) {
        Sosa98 sosa98 = new Sosa98();
        Sosa98Id sosa98Id = new Sosa98Id();
        Date data = new Date();
        sosa98.setId(sosa98Id);
        String item = this.controleService.gerarSequencia(this.comandas.getCOMANDA());
        String numero = this.controleService.gerarNumero() + item;
        lancamento.setComanda(this.comandas.getCOMANDA());
        lancamento.setMesa(this.comandas.getCOMANDA());
        lancamento.setItem(item);
        lancamento.setNumero(numero);
        lancamento.setPedido(pedido);
        sosa98Id.setTenumseq(item);
        sosa98Id.setTenumero(numero);
        sosa98.setTecdmesa(this.comandas.getCOMANDA());
        sosa98.setTecomand(this.comandas.getCOMANDA());
        sosa98.setTedatcom(data);
        sosa98.setTeimprim(lancamento.getImprimir());
        sosa98.setTepedido(pedido);
        sosa98.setTequanti(lancamento.getQuantidade());
        sosa98.setTerefere(lancamento.getReferencia());
        sosa98.setTestatus(lancamento.getStatus());
        sosa98.setTevended(lancamento.getVendedor());
        try {
            this.controleService.salvar(sosa98);
            this.salvarEspelho(lancamento, data);
            sosa98Id = null;
            sosa98 = null;
        } catch (Exception ex) {
            Messages.addGlobalError("Erro ao salvar");
        }
    }

    private void salvarEspelho(Lancamento lancamento, Date data) {
        EspelhoComanda espelhoComanda = new EspelhoComanda();
        espelhoComanda.setNumero(Integer.parseInt(lancamento.getNumero()));
        espelhoComanda.setPedido(lancamento.getPedido());
        espelhoComanda.setMesa(lancamento.getMesa());
        espelhoComanda.setComanda(lancamento.getComanda());
        espelhoComanda.setData(data);
        espelhoComanda.setNumeroItem(lancamento.getItem());
        espelhoComanda.setReferencia(lancamento.getReferencia());
        espelhoComanda.setPessoasMesa(this.comandas.getPESSOAS().isEmpty() ? "1" : this.comandas.getPESSOAS());
        espelhoComanda.setQuantidade(lancamento.getQuantidade());
        espelhoComanda.setVendedor(lancamento.getVendedor());
        espelhoComanda.setImpressao(lancamento.getImprimir());
        espelhoComanda.setStatus(lancamento.getStatus());
        espelhoComanda.setObservacao(lancamento.getObservacao());
        espelhoComanda.setStatusItem("N");
        espelhoComanda.setValorItem(lancamento.getPreco());
        espelhoComanda.setDataPreconta(new Date());
        if (!this.configuracao.getCobraDezPorcento().isEmpty()) {
            espelhoComanda.setPorcentagem(10d);
            double valorComDezPOrcento = lancamento.getPrecoTotal() * 0.10;
            espelhoComanda.setValorPorcentagem(valorComDezPOrcento);
        }
        espelhoComandaBean.setEspelhoComanda(espelhoComanda);
        espelhoComandaBean.salvar();
    }

    public void setLancamento(Lancamento lancamento) {
        this.lancamento = lancamento;
    }

    public void validarUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean fechar;
        if (this.usuarioBean.validarGerente()) {
            removerItem(this.lancamento);
            this.lancamento = null;
            fechar = true;
        } else {
            fechar = false;
            Messages.addGlobalWarn("Essa ação não pode ser executada\n informe um usuario valido ou \nusuario e senha de Gerente");
        }
        context.addCallbackParam("fechar", fechar);
    }

    public void listarComandas() {
        this.listaComandas = this.controleService.listarComandas();
    }

    public void pesquisarComandas() {
        this.listaComandas = this.controleService.pesquisarComandaPorCodigo(pesquisa);
    }

    public void abrirComanda(Comandas comanda) {
        this.produtoBean.setComanda(comanda.getCOMANDA());
        this.produtoBean.setMesa(comanda.getMESA());
        this.produtoBean.listarProdutosAdicionados();
        this.lancamentos = this.produtoBean.getLancamentosAdicionados();
        this.comandas = comanda;
        this.total = this.lancamentos.stream().mapToDouble(Lancamento::getPrecoTotal).sum();
    }

    public void sair() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            Faces.redirect("login.jsf");
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao abrir tela de login.");
        }
    }

    public void imprimirPrecontaMesa() {
        mesasBean.setPesquisa(this.comandas.getCOMANDA());
        mesasBean.pesquisarMesas();
        mesasBean.imprimirPreconta(this.comandas.getCOMANDA());
        this.espelhoComandaBean.atualizarUsuarioPreconta(this.comandas.getPEDIDO(), this.usuario.getNOME());
        listarComandas();
    }

    public void imprimirPrecontaMesa(Comandas comandas) {
        mesasBean.setPesquisa(comandas.getCOMANDA());
        mesasBean.setMesa(new Mesa());
        mesasBean.getMesa().setMESA(comandas.getCOMANDA());
        mesasBean.getMesa().setCOMANDA(comandas.getCOMANDA());
        mesasBean.getMesa().setPAGANTES(comandas.getPAGANTES());
        mesasBean.pesquisarMesas();
        mesasBean.imprimirPreconta(comandas.getCOMANDA());
        listarComandas();
    }

    private void excluir(Lancamento lancamento) {
        if ("1".equals(lancamento.getImprimir())) {
            this.controleService.excluir(lancamento.getNumero());
            this.espelhoComandaBean.excluir(Integer.parseInt(lancamento.getNumero()));
        }
    }

}
