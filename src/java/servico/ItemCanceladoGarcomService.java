package servico;

import java.util.List;
import modelo.dto.FiltroItemCancelado;
import modelo.dto.ItemCanceladoGarcom;

public interface ItemCanceladoGarcomService {
    
    public List<ItemCanceladoGarcom> listarItensCanceladosPorGarcom(FiltroItemCancelado filtroItemCancelado);
    public void atualizarObservacaoDestino(String numero,String observacao);
}
