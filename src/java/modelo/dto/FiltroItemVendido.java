
package modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroItemVendido {
    
    private String dataInicial;
    private String dataFinal;
    private String garcom;
    private String produto;
    private String grupo;
    
}
