package servico;

import java.util.List;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaGarcom;


public interface VendaDetalheService {
    public List<VendaGarcom> listarVendaGarcom();
    public List<RejeicaoPorcentagemVendedor> listarReijeicaoPorcentagemPorVendedor(String vendedor,String dataIncial,String dataFinal);
}
