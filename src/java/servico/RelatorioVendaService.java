package servico;

import java.util.List;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaGarcom;

public interface RelatorioVendaService {
    
    public List<VendaGarcom> listarVendasGarcom(FiltroVendaDetalhe filtroVendaDetalhe);
    public List<RejeicaoPorcentagemVendedor> listarRejeicoesPorGarcom(FiltroVendaDetalhe filtroVendaDetalhe);
    
}
