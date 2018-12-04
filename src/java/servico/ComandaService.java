package servico;

import java.util.List;
import modelo.Comandas;
import modelo.Lancamento;
import modelo.Sosa98;

public interface ComandaService {

    public List<Comandas> listarComandasPorMesas(String mesa);

    public List<Comandas> listarComandasPorCodigo(String mesa,String comanda);

    public List<Object[]> ListarLancamentos(String codigo,String mesa);
    
    public int buscarQuantidadeProdutosComanda(String comanda,String mesa);

    public void salvar(Sosa98 sosa98);
    
    public void alterar(Sosa98 sosa98);

    public void excluir(String codigo);

    public Sosa98 buscarPorId(String codigo);
    
    public List<Object[]> listarComandas(String codigo);
    
    public List<Comandas> listarComandas();
    
    public List<Comandas> pesquisarComandaPorCodigo(String codigo);

    public String gerarSequencia(String comanda);
    
    public void excluirMesa(String mesa,String comanda);
    
    public int verificarComanda(String mesa,String comanda);

    public void transferirComandaParaMesa(String mesa,Comandas comandas);
    
    public void transferirComandaParaComanda(Comandas comandaOrigem,String comandaDestino);
    
    public void atualizarStatusImpressao(String comanda);
    
    public void atualizarStatusPreconta(Comandas comanda);
    
    public boolean verificarStatusMesa(String comanda);
    
    public int verificarNumeroPedido(String comanda);
    
    public void alterarQuantidadeItem(double quantidade,String numero);
    
    public List<Object[]> pesquisarItensTransferencia(String pedidos);
    
    public void transferirItens(String numero,String item);
    
    public void transferenciaItensParaMesaComanda(Comandas comanda,List<Lancamento> lancamentos);
        
}
