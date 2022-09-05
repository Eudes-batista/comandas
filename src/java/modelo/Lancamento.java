package modelo;

import java.io.Serializable;
import java.util.Objects;

public class Lancamento implements Serializable{
    
    private String numero;
    private String item;   
    private String comanda;    
    private String referencia;    
    private String descricao;    
    private double quantidade;    
    private double preco;    
    private double precoTotal;    
    private String vendedor;    
    private String observacao;    
    private String mesa;
    private String imprimir;
    private String status;
    private String pedido;

   
    public Lancamento() {
    }

    public Lancamento(String numero) {
        this.numero = numero;
    }
    
    public Lancamento(String mesa, String comanda) {
        this.mesa = mesa;
        this.comanda = comanda;
    }

    public Lancamento(String numero, String item, String comanda, String referencia, String descricao, double quantidade, double preco, double precoTotal, String vendedor, String observacao, String mesa, String imprimir, String status,String pedido) {
        this.numero = numero;
        this.item = item;
        this.comanda = comanda;
        this.referencia = referencia;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
        this.precoTotal = precoTotal;
        this.vendedor = vendedor;
        this.observacao = observacao;
        this.mesa = mesa;
        this.imprimir = imprimir;
        this.status = status;
        this.pedido = pedido;
    }

    public Lancamento(String comanda, String referencia, String descricao, double quantidade, double preco, double precoTotal,String observacao) {        
        this.comanda = comanda;
        this.referencia = referencia;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
        this.precoTotal = precoTotal;
        this.observacao = observacao;
    }

    public Lancamento(String descricao, double precoTotal) {
        this.descricao = descricao;
        this.precoTotal = precoTotal;
    }
    
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getComanda() {
        return comanda;
    }

    public void setComanda(String comanda) {
        this.comanda = comanda;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String getObservacao() {
        if (observacao == null || observacao.equals("null")) {
            observacao = "";
        }
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
   
     public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.comanda);
        hash = 67 * hash + Objects.hashCode(this.mesa);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lancamento other = (Lancamento) obj;
        if (!Objects.equals(this.comanda, other.comanda)) {
            return false;
        }
        if (!Objects.equals(this.referencia, other.referencia)) {
            return false;
        }
        return Objects.equals(this.mesa, other.mesa);
    }

    @Override
    public String toString() {
        return "Lancamento{" + "numero=" + numero + ", item=" + item + ", comanda=" + comanda + ", referencia=" + referencia + ", descricao=" + descricao + ", quantidade=" + quantidade + ", preco=" + preco + ", precoTotal=" + precoTotal + ", vendedor=" + vendedor + ", observacao=" + observacao + ", mesa=" + mesa + ", imprimir=" + imprimir + '}';
    }

    public String getImprimir() {
        return imprimir;
    }

    public void setImprimir(String imprimir) {
        this.imprimir = imprimir;
    }

}
