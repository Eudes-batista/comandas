package servico;

import java.util.List;
import modelo.dto.FiltroRelatorioPreconta;
import modelo.dto.ItemVendido;
import modelo.dto.RelatorioPreconta;

public interface RelatorioPrecontaService {

    public List<RelatorioPreconta> listarTudo(FiltroRelatorioPreconta filtroRelatorioPreconta) ;
    public List<ItemVendido> listarItensVendidos(FiltroRelatorioPreconta filtroRelatorioPreconta);
}
