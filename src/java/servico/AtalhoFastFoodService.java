
package servico;

import modelo.AtalhoFastFood;


public interface AtalhoFastFoodService {
    public void salvar(AtalhoFastFood atalhoFastFood) throws Exception;
    public void excluir(AtalhoFastFood atalhoFastFood) throws Exception;
    public AtalhoFastFood carregar();
}
