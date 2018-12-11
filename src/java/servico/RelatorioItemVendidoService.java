package servico;

import java.util.List;
import modelo.dto.FiltroItemVendido;
import modelo.dto.ItemVendido;


public interface RelatorioItemVendidoService {
    public List<ItemVendido> listaItensVendidos(FiltroItemVendido filtroItemVendido);
}
