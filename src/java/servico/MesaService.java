package servico;

import java.util.List;
import modelo.Mesa;

/**
 *
 * @author Administrador
 */
public interface MesaService {

    public List<Mesa> listarMesas();
    public List<Mesa> listarMesas(String mesa);
    public Object somarTotal(String mesa);
    public List<Object[]> listarComandasPorMesa(String mesa);
    public void excluirMesa(String mesa);
    public void transferirMesa(Mesa mesaOrigem,String mesaDestino);
    public void atualizarStatusPreconta(Mesa mesa,String tipo);
    public List<Object[]> listarLancamentos(String mesa);
    
}
