
package servico;

import modelo.AtalhoFastFood;


public interface AtalhoFastFoodService {
    public void salvar(AtalhoFastFood atalhoFastFood);
    public void excluir(AtalhoFastFood atalhoFastFood) throws Exception;
    public AtalhoFastFood carregar();
    public int verificarId();
}
