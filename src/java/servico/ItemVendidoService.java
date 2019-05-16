package servico;

import java.util.List;
import modelo.dto.FiltroItemVendido;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.ItemPedido;
import modelo.dto.ItemVendido;


public interface ItemVendidoService {
    public List<ItemVendido> listarItensVendidos(FiltroVendaDetalhe vendaDetalhe);
    public List<ItemPedido> buscarItensVendidos(FiltroItemVendido filtroItemVendido);
}
