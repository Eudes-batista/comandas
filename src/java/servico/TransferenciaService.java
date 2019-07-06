package servico;

import java.util.List;
import modelo.Transferencia;
import modelo.dto.FiltroTransferencia;

public interface TransferenciaService {
    
    public void salvar(Transferencia transferencia);
    
    public List<Transferencia> listarTransferencias(FiltroTransferencia filtroTransferencia);
} 
