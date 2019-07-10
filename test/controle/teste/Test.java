package controle.teste;

import controle.Controle;
import java.util.Date;
import modelo.dto.EspelhoComandaDTO;

public class Test {

    public static void main(String[] args) {
       
        Controle controle = new Controle();
        
        EspelhoComandaDTO espelhoComanda = controle.buscarPedidoDeDistino("285137495");
        
        Date data = espelhoComanda.getDATA_PRECONTA();
        
        
    }

}
