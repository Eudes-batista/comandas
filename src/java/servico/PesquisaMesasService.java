package servico;

import java.util.List;
import modelo.Comandas;
import modelo.dto.ItemPedido;

public interface PesquisaMesasService {
    
    public List<Comandas> listarComandas(String dataInicial,String dataFinal);

    public List<Comandas> pesquisarComandaPorCodigo(String codigo,String dataInicial,String dataFinal);
    
    public List<ItemPedido> listarItemPorPedido(String pedido);
}
