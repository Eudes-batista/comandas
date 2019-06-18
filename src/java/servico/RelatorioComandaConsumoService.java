package servico;

import java.util.List;
import modelo.dto.FiltroRelatorioPreconta;
import modelo.dto.RelatorioComandaConsumo;

public interface RelatorioComandaConsumoService {
    public List<RelatorioComandaConsumo> listarComandasEmConsumo(FiltroRelatorioPreconta filtroRelatorioPreconta);
}
