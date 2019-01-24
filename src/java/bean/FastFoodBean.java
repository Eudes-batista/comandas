package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.AtalhoFastFood;
import modelo.Lancamento;
import modelo.Lapt51;
import modelo.Produto;
import servico.AtalhoFastFoodService;
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

    private AtalhoFastFood atalhoFastFood;
    private List<Lapt51> grupos;
    private List<Produto> produtos;
    private String pesquisa;
    private double quantidade;
    private List<Lancamento> lancamentos;

    public void init() {
        this.atalhoFastFood = atalhoFastFoodService.carregar();
        this.grupos = grupoServico.listarGrupos();
        this.produtos = produtoServico.lsitarProdutos();
        this.lancamentos = new ArrayList<>();
    }

    public void pesquisarProduto() {
        this.produtos = this.produtoServico.listarPorReferenciaDescricaoCodigoBarra(pesquisa);
    }

    public void pesquisarPorGrupo(Lapt51 lapt51) {
        this.produtos = this.produtoServico.lsitarProdutoPorGrupo(lapt51.getT51cdgrp());
    }
    
    public void buscarProduto(String refencia) {
        Produto produto = produtoServico.buscarProduto(refencia);
        adicionarItem(produto);
    }
    

    public void adicionarItem(Produto p) {
        Lancamento lancamentoItem = new Lancamento();
        lancamentoItem.setReferencia(p.getReferencia());
        lancamentoItem.setDescricao(p.getDescricao());
        this.quantidade = this.quantidade == 0 ? 1 : this.quantidade;
        lancamentoItem.setQuantidade(this.quantidade);
        lancamentoItem.setPreco(p.getPreco());
        lancamentoItem.setPrecoTotal(p.getPreco() * quantidade);
        lancamentoItem.setImprimir("0");
        lancamentoItem.setStatus("");
        this.lancamentos.add(lancamentoItem);
    }
    
    public void removerItem(Lancamento lancamento){
        this.lancamentos.remove(lancamento);
    }
    
    public double getQuantidade(){
       return this.quantidade == 0 ? 1 : quantidade;
    }
    
}
