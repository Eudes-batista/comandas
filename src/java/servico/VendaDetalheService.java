package servico;

import java.util.List;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaDetalheRejeicao;
import modelo.dto.VendaGarcom;


public interface VendaDetalheService {
    public List<VendaGarcom> listarVendaGarcom();
    public List<RejeicaoPorcentagemVendedor> listarReijeicaoPorcentagemPorVendedor(String vendedor,String dataIncial,String dataFinal);
    public List<VendaDetalheRejeicao> listarRejeicaoDezPorcentoPorGarcom(String vendedor,String dataIncial,String dataFinal);
}
