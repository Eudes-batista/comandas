package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.AtalhoFastFood;
import modelo.Produto;
import org.omnifaces.util.Messages;
import servico.AtalhoFastFoodService;
import servico.ProdutoService;

@ManagedBean(name = "atalhoFastFoodBean")
@ViewScoped
@Getter
@Setter
public class AtalhoFastFoodBean implements Serializable {

    @ManagedProperty(value = "#{atalhoFastFoodService}")
    private AtalhoFastFoodService atalhoFastFoodService;
    @ManagedProperty(value = "#{produtoServico}")
    private ProdutoService produtoServico;

    private AtalhoFastFood atalhoFastFood;

    public void init() {
//        this.atalhoFastFood = atalhoFastFoodService.carregar();
        if (this.atalhoFastFood == null) {
            this.atalhoFastFood = new AtalhoFastFood();
        }
    }

    public void salvar() {
        try {
            atalhoFastFoodService.salvar(atalhoFastFood);
        } catch (Exception ex) {
            Messages.addGlobalError("Erro ao salvar os atalhos " + ex.getMessage());
        }
    }

    public void excluir() {
        try {
            atalhoFastFoodService.excluir(atalhoFastFood);
        } catch (Exception ex) {
            Messages.addGlobalError("Erro ao excluir os atalhos " + ex.getMessage());
        }
    }
    
    public String buscarProduto(String refencia) {
        Produto produto = produtoServico.buscarProduto(refencia);
        if(produto == null){
            Messages.addGlobalWarn("Produto não econtrado.");
            return "";
        }
        return produto.getDescricao();
    }
    
    public void pesquisarReferenciaUm() {
        this.atalhoFastFood.setDescricao01(buscarProduto(this.atalhoFastFood.getReferencia01()));
    }
    public void pesquisarReferenciaDois() {
        this.atalhoFastFood.setDescricao02(buscarProduto(this.atalhoFastFood.getReferencia02()));
    }
    public void pesquisarReferenciaTres() {
        this.atalhoFastFood.setDescricao03(buscarProduto(this.atalhoFastFood.getReferencia03()));
    }
    public void pesquisarReferenciaQuatro() {
        this.atalhoFastFood.setDescricao04(buscarProduto(this.atalhoFastFood.getReferencia04()));
    }
    public void pesquisarReferenciaCinco() {
        this.atalhoFastFood.setDescricao05(buscarProduto(this.atalhoFastFood.getReferencia05()));
    }
    public void pesquisarReferenciaSeis() {
        this.atalhoFastFood.setDescricao06(buscarProduto(this.atalhoFastFood.getReferencia06()));
    }
    public void pesquisarReferenciaSete() {
        this.atalhoFastFood.setDescricao07(buscarProduto(this.atalhoFastFood.getReferencia07()));
    }
    public void pesquisarReferenciaOito() {
        this.atalhoFastFood.setDescricao08(buscarProduto(this.atalhoFastFood.getReferencia08()));
    }
    public void pesquisarReferenciaNove() {
        this.atalhoFastFood.setDescricao09(buscarProduto(this.atalhoFastFood.getReferencia09()));
    }
    public void pesquisarReferenciaDez() {
        this.atalhoFastFood.setDescricao10(buscarProduto(this.atalhoFastFood.getReferencia10()));
    }
    public void pesquisarReferenciaOnze() {
        this.atalhoFastFood.setDescricao11(buscarProduto(this.atalhoFastFood.getReferencia11()));
    }
    public void pesquisarReferenciaDoze() {
        this.atalhoFastFood.setDescricao12(buscarProduto(this.atalhoFastFood.getReferencia12()));
    }
}
