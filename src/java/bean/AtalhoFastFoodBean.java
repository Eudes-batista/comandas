package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.AtalhoFastFood;
import org.omnifaces.util.Messages;
import servico.AtalhoFastFoodService;

@ManagedBean(name="atalhoFastFoodBean")
@ViewScoped
@Getter @Setter
public class AtalhoFastFoodBean implements Serializable{

    @ManagedProperty(value="#{atalhoFastFoodService}")
    private AtalhoFastFoodService atalhoFastFoodService;
    
    private AtalhoFastFood atalhoFastFood;
    
    public void init() {
        this.atalhoFastFood = atalhoFastFoodService.carregar();
        if(this.atalhoFastFood == null){
            this.atalhoFastFood = new AtalhoFastFood();
        }
    }
    
    public void salvar() {
        try {
            atalhoFastFoodService.salvar(atalhoFastFood);
        } catch (Exception ex) {
            Messages.addGlobalError("Erro ao salvar os atalhos "+ex.getMessage());
        }
    }
    
    public void excluir() {
        try {
            atalhoFastFoodService.excluir(atalhoFastFood);
        } catch (Exception ex) {
            Messages.addGlobalError("Erro ao excluir os atalhos "+ex.getMessage());
        }
    }
    
    
}
