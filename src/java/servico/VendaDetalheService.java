package servico;

import java.util.List;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaDetalhe;
import modelo.dto.VendaGarcom;


public interface VendaDetalheService {
    public List<VendaGarcom> listarVendaGarcom(FiltroVendaDetalhe filtroVendaDetalhe);
    public List<RejeicaoPorcentagemVendedor> listarReijeicaoPorcentagemPorVendedor(FiltroVendaDetalhe filtroVendaDetalhe);
    public List<VendaDetalhe> listarRejeicaoDezPorcentoPorGarcom(FiltroVendaDetalhe filtroVendaDetalhe);
    public List<VendaDetalhe> listarItensVendidosPorGarcom(FiltroVendaDetalhe filtroVendaDetalhe);
}
