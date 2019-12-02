package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Comandas;

public class JuntaComandas {

    private static double valorCouvert;

    public static Map<String, Comandas> juntarPedidosPorComanda(List<Comandas> lista) {
        valorCouvert=0;
        Map<String, Comandas> mapAuditoria = new HashMap<>();
        for (Comandas comandas : lista) {
            String chave = comandas.getCOMANDA();
            Comandas comanda = mapAuditoria.get(chave);
            comanda = realizarCalculoValorTotal(comandas, comanda);
            mapAuditoria.put(chave, comanda);
        }
        return mapAuditoria;
    }

    public static Map<String, Comandas> juntarPorPedidoPreconta(List<Comandas> lista) {
        valorCouvert=0;
        Map<String, Comandas> mapAuditoria = new HashMap<>();
        lista.forEach((comandas) -> {
            String pedido = comandas.getPEDIDO();
            Comandas comanda = mapAuditoria.get(pedido);
            comanda = realizarCalculoValorTotal(comandas, comanda);
            mapAuditoria.put(pedido, comanda);
        });
        return mapAuditoria;
    }

    private static Comandas realizarCalculoValorTotal(Comandas comandaOrigem, Comandas comandaAdicionada) {
        double total = 0, totalSemOValorDoCouvert = 0;;
        if (comandaAdicionada == null) {
            comandaAdicionada = comandaOrigem;
            total = comandaAdicionada.getTOTAL();
            valorCouvert = comandaOrigem.getDESCRICAO().contains("COUVERT") ? comandaOrigem.getTOTAL() : 0;
        } else {
            total += comandaAdicionada.getTOTAL() + comandaOrigem.getTOTAL();
            if (comandaOrigem.getDESCRICAO().contains("COUVERT")) {
                valorCouvert += comandaOrigem.getTOTAL();
            }
        }
        comandaAdicionada.setTOTAL(total);
        totalSemOValorDoCouvert = total - valorCouvert;
        totalSemOValorDoCouvert = totalSemOValorDoCouvert * 1.1;
        comandaAdicionada.setTOTAL_COM_DEZ_PORCENTO(totalSemOValorDoCouvert + valorCouvert);
        return comandaAdicionada;
    }

}
