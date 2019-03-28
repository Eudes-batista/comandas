package controle.teste;

import controle.PesquisaMesasControle;
import java.util.List;
import modelo.dto.ItemPedido;

public class Test {

    public static void main(String[] args) {
       
        PesquisaMesasControle pesquisaMesasControle = new PesquisaMesasControle();
        
        List<ItemPedido> itensPedido = pesquisaMesasControle.listarItemPorPedido("154192");
        itensPedido.forEach(System.out::println);
    }

}
