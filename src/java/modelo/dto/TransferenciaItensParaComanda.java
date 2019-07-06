package modelo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import modelo.Comandas;
import modelo.Lancamento;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaItensParaComanda {
    
    private Comandas comanda;
    private List<Lancamento> lancamentosTransferencia;
    private List<Lancamento> lancamentosOrigem;
    private String usuarioTransferencia;
    
}
