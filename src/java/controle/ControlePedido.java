package controle;

import java.time.LocalDate;
import java.time.LocalTime;
import servico.ComandaService;

public class ControlePedido {

    private final ComandaService comandaService;
    private final String comanda;

    public ControlePedido() {
        this.comandaService = null;
        this.comanda = null;
    }
    
    public ControlePedido(ComandaService comandaService, String comanda) {
        this.comandaService = comandaService;
        this.comanda = comanda;
    }

    public String gerarNumero() {
        int pedido = comandaService.verificarNumeroPedido(comanda);
        if (pedido == 0) {            
            pedido =gerarPedido();
            while(verificarPedido(pedido)){
                pedido =gerarPedido();
            }
            return String.valueOf(pedido);
        }
        return String.valueOf(pedido);
    }
    
    private boolean verificarPedido(int pedido){
        return comandaService.verificarSePedidoJaExiste(String.valueOf(pedido)) != 0;
    }
    
    public int gerarPedido(){
        LocalDate data = LocalDate.now();
        LocalTime hora = LocalTime.now();
        String numero = String.valueOf(((data.getYear() * data.getMonthValue() * data.getDayOfMonth()) + (2217 * hora.getHour())) + (hora.getSecond() + hora.getNano()));
        int pedido =numero.length() > 9 ? Integer.parseInt(numero.substring(0, 9)) : Integer.parseInt(numero);
        return pedido;
    }
}
