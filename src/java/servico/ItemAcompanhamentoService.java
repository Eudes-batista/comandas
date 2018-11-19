package servico;

import java.util.List;
import modelo.ItemAcompanhamento;

public interface ItemAcompanhamentoService {    
     public void salvar(ItemAcompanhamento itemAcompanhamento);
     public void excluir(ItemAcompanhamento itemAcompanhamento);
     public List<ItemAcompanhamento> pesquisarTodos();
     public List<ItemAcompanhamento> pesquisarNome(String nome);    
     public List<ItemAcompanhamento> pesquisarItem(String item,String pedido);    
     public void excluirTodos(String Item,String pedido);
     public void excluirTodos(String pedido);
}
