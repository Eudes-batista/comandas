package modelo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = {"empresa"})
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Configuracao implements Serializable {

    private String caminho;
    private String empresa;
    private String impressora;
    private String tipoImpressao;
    private String cobraDezPorcento;
    private String condicaoParaImpressao;
    private String gerarComandaAutomatico;
    private String caminhoCatraca;
    private String mensagemDezPorcento;
    
}
