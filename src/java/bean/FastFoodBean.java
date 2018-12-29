
package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;

@ManagedBean(name="fastFoodBean")
@ViewScoped
@Getter
@Setter
public class FastFoodBean implements Serializable{

    private String referencia01 = "705";
    
    public void lancarProduto(String referencia){
        System.out.println("referencia = " + referencia);
    }
    
    
}
