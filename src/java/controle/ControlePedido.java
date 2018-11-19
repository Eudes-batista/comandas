package controle;

import java.time.LocalDate;
import java.time.LocalTime;
import servico.ComandaService;

public class ControlePedido {

    private final ComandaService comandaService;
    private final String comanda;

    public ControlePedido(ComandaService comandaService, String comanda) {
        this.comandaService = comandaService;
        this.comanda = comanda;
    }

    public String gerarNumero() {
        int pedido = comandaService.verificarNumeroPedido(comanda);
        if (pedido ==0) {
            LocalDate data = LocalDate.now();
            LocalTime hora = LocalTime.now();
            return String.valueOf(((data.getYear() * data.getMonthValue() * data.getDayOfMonth()) + (2217 * hora.getHour())) + hora.getSecond());
        }
        return String.valueOf(pedido);
    }
}
