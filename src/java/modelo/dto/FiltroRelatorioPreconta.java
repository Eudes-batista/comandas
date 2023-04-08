package modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroRelatorioPreconta {
   private String dataInicial; 
   private String dataFinal; 
   private String pedido; 
   private String vendedor; 
   private String mesa; 
   private String comanda; 
   
}
