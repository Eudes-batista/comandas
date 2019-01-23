package controle.teste;

import controle.AtalhoFastFoodControle;
import modelo.AtalhoFastFood;

public class Test {

    public static void main(String[] args) {
       
        AtalhoFastFood  atalhoFastFood = new AtalhoFastFood();
        
        atalhoFastFood.setCodigo(1);
        atalhoFastFood.setReferencia01("602");
        atalhoFastFood.setDescricao01("AGUA MINERAL");
        
        AtalhoFastFoodControle atalhoFastFoodControle = new AtalhoFastFoodControle();
        atalhoFastFoodControle.salvar(atalhoFastFood);
        
    }

}
