package servico;

import java.util.List;
import modelo.ItemAcompanhamento;
import modelo.Lancamento;

public interface ItemAcompanhamentoService {    
     public void salvar(ItemAcompanhamento itemAcompanhamento);
     public void excluir(ItemAcompanhamento itemAcompanhamento);
     public List<ItemAcompanhamento> pesquisarTodos();
     public List<ItemAcompanhamento> pesquisarNome(String nome);    
     public List<ItemAcompanhamento> pesquisarItem(String item,String pedido);    
     public void excluirTodos(String Item,String pedido);
     public void excluirTodos(String pedido);
     public void atualizarStatusAcompanhamento(Lancamento lancamento,String status);
     public boolean pesquisarSeExisteAcompanahmento(String pedido);
}
