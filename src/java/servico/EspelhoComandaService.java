package servico;

import java.util.List;
import modelo.EspelhoComanda;
import modelo.Mesa;

public interface EspelhoComandaService {
    public void salvar(EspelhoComanda espelhoComanda);
    public void alterar(EspelhoComanda espelhoComanda);
    public void excluir(Integer numero);
    public EspelhoComanda buscarPorId(Integer numero);
    public void atualizarStatusImpressao(String comanda);
    public void atualizarDataPreconta(String data,Mesa mesa);
    public void salvarPessoaQueAutorizouReipressao(String usuario,String item);
    public List<EspelhoComanda> listarAuditoria();
    public void atualizarPorcentagem(String pedido,double porcentagem);
    public List<Object[]> listarProdutosPedido(String pedido);
    public void atualizarValorPorcentagemItens(String pedido);
    public void atualizarStatusItens(String pedidos);
}
