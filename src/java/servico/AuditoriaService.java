package servico;

import java.util.List;
import modelo.Comandas;

public interface AuditoriaService {

    public List<Comandas> listarComandas();

    public List<Comandas> pesquisarComandaPorCodigo(String codigo);
}
