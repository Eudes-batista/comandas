package servico;

import java.util.List;
import modelo.dto.ClienteAtendido;
import modelo.dto.Couvert;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.TotalVenda;
import modelo.dto.VendaGarcom;

public interface DashboardService {
    public TotalVenda listarTotasVendasDoDia();
    public List<ClienteAtendido> listarClientesAtendidos();
    public List<ClienteAtendido> listarClientesEmAtendimento();
    public List<VendaGarcom> listarVendasPorGarcom();
    public List<RejeicaoPorcentagemVendedor> listarRejeicaoPorcentagemVendedor();
    public List<VendaGarcom> listarComissaoGarcom();
    public Couvert listarTotalCouvert();
}
