
package modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import modelo.Lapt51;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroItemVendido {
    
    private String dataInicial;
    private String dataFinal;
    private String garcom;
    private String produto;
    private String produtoFinal;
    private Lapt51 grupo;
    private boolean cancelado;

    @Override
    public String toString() {
        return "FiltroItemVendido{" + "dataInicial=" + dataInicial + ", dataFinal=" + dataFinal + ", garcom=" + garcom + ", produto=" + produto + ", grupo=" + grupo + ", cancelado=" + cancelado + '}';
    }

}
