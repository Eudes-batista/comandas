package servico;

import java.util.List;
import modelo.Cancelamento;

public interface CancelamentoService {
    
    public boolean salvar(Cancelamento cancelamento);
    public List<Cancelamento> listar();
    public String gerarChavePrimaria();
}
