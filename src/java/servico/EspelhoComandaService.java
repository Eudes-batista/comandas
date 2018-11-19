package servico;

import modelo.EspelhoComanda;
import modelo.Mesa;

public interface EspelhoComandaService {
    public void salvar(EspelhoComanda espelhoComanda);
    public void alterar(EspelhoComanda espelhoComanda);
    public void excluir(Integer numero);
    public EspelhoComanda buscarPorId(Integer numero);
    public void atualizarStatusImpressao(String comanda);
    public void atualizarDataPreconta(String data,Mesa mesa);
}
