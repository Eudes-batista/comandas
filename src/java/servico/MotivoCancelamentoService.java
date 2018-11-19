package servico;

import java.util.List;
import modelo.MotivoCancelamento;

public interface MotivoCancelamentoService {

    public void salvar(MotivoCancelamento motivoCancelamento);

    public void excluir(MotivoCancelamento motivoCancelamento);

    public List<MotivoCancelamento> pesquisar(String nome);

    public List<MotivoCancelamento> pesquisarTodos();
    
    public int verificarId();
    
}
