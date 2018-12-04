package servico;

import java.util.List;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.ItemVendido;


public interface ItemVendidoService {
    public List<ItemVendido> listarItensVendidos(FiltroVendaDetalhe vendaDetalhe);
}
