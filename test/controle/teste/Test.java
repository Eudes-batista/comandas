package controle.teste;

import controle.Controle;
import modelo.dto.Cancelamento;

public class Test {

    public static void main(String[] args) {
       
        Controle controle = new Controle();
        
        Cancelamento cancelamento = new Cancelamento();
        
        cancelamento.setMotivo(99);
        cancelamento.setPedidos("203090658,102030659");
        cancelamento.setStatus("M");
        cancelamento.setUsuario("CSS");
        
        controle.cancelarPedidos(cancelamento);
    }

}
