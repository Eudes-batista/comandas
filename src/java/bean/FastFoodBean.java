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
import modelo.Lancamento;
import modelo.Lapt51;
import modelo.Produto;
import modelo.Sosa98;
import modelo.Sosa98Id;
import modelo.dto.Usuario;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import servico.AtalhoFastFoodService;
import servico.ComandaService;
import servico.GrupoServico;
import servico.ProdutoService;

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

    private AtalhoFastFood atalhoFastFood;
    private Usuario usuario;
    private Comandas comandas;
    private ControlePedido controlePedido;

    private List<Lapt51> grupos;
    private List<Lancamento> lancamentos;
    private List<Produto> produtos;

    private String pesquisa;
    private double quantidade;
    private Produto produto;

    public void init() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        if (this.usuario == null) {
            try {
                Faces.redirect("login.jsf");
            } catch (IOException ex) {
                Messages.addGlobalError("Erro ao redireciona para a pagina de login.");
            }
        }
        this.atalhoFastFood = atalhoFastFoodService.carregar();
        this.grupos = grupoServico.listarGrupos();
        this.produtos = produtoServico.lsitarProdutosFastFood();
        this.lancamentos = new ArrayList<>();
        this.comandas = new Comandas();
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
                PrimeFaces.current().executeScript("PF('dialogQuantidade').show();");
                return;
            }
        }
        adicionarItem(produtoEncontrado);
    }

    public void definirQuantidade() {
        this.adicionarItem(this.produto);
        this.produto = null;
    }

    public void adicionarItem(Produto p) {
        if (p.getUNIDADE().contains("KG")) {
            if (this.produto == null) {
                this.produto = p;
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
    }

    public void removerItem(Lancamento lancamento) {
        this.lancamentos.remove(lancamento);
    }

    public double getQuantidade() {
        return this.quantidade == 0 ? 1 : quantidade;
    }

    public void finalizar() {
        if (this.comandas.getCOMANDA() == null) {
            PrimeFaces.current().executeScript("PF('dialogFinalizar').show();");
            return;
        }
        this.controlePedido = new ControlePedido(controleService, this.comandas.getCOMANDA());
        lancamentos.forEach((lancamento) -> {
            salvar(lancamento, controlePedido);
        });
    }

    public void novoProduto() {
        this.produto = null;
    }

    private void salvar(Lancamento lancamento, ControlePedido controlePedido) {
        Sosa98 sosa98 = new Sosa98();
        Sosa98Id sosa98Id = new Sosa98Id();
        Date data = new Date();
        sosa98.setId(sosa98Id);
        String item = this.controleService.gerarSequencia(this.comandas.getCOMANDA());
        sosa98Id.setTenumseq(item);
        sosa98Id.setTenumero(this.controleService.gerarNumero() + item);
        sosa98.setTecdmesa(this.comandas.getCOMANDA());
        sosa98.setTecomand(this.comandas.getCOMANDA());
        sosa98.setTedatcom(data);
        sosa98.setTeimprim(lancamento.getImprimir());
        sosa98.setTepedido(controlePedido.gerarNumero());
        sosa98.setTequanti(lancamento.getQuantidade());
        sosa98.setTerefere(lancamento.getReferencia());
        sosa98.setTestatus(lancamento.getStatus());
        sosa98.setTevended(lancamento.getVendedor());
        try {
            this.controleService.salvar(sosa98);
            sosa98Id = null;
            sosa98 = null;
        } catch (Exception ex) {
            Messages.addGlobalError("Erro ao salvar");
        }
    }

}
