package controle.teste;

import controle.Controle;
import controle.ControlePedido;
import controle.EspelhoComandaControle;
import java.util.Date;
import modelo.EspelhoComanda;
import modelo.Sosa98;
import modelo.Sosa98Id;

public class Test {

    public static void main(String[] args) throws Exception {

        System.out.println("Aperte enter para Comecar");
        System.in.read();

        Controle controle = new Controle();
        EspelhoComandaControle espelhoComandaControle = new EspelhoComandaControle();
        ControlePedido controlePedido = new ControlePedido();
        for (int i = 1; i <= 2000; i++) {
            Sosa98Id sosa98id = new Sosa98Id();
            Sosa98 sosa98 = new Sosa98();

            sosa98id.setTenumero(controle.gerarNumero());
            sosa98id.setTenumseq(String.valueOf(i));

            String comanda = String.format("%04d", i);

            sosa98.setId(sosa98id);
            sosa98.setTecdmesa(comanda);
            sosa98.setTecomand(comanda);
            sosa98.setTerefere("702");
            sosa98.setTedatcom(new Date());
            sosa98.setTeimprim("0");
            sosa98.setTeobserv("");
            sosa98.setTepedido(String.valueOf(controlePedido.gerarPedido()));
            sosa98.setTequanti(1.0);
            sosa98.setTestatus("");
            sosa98.setTevended("CSS");
            controle.salvar(sosa98);

            EspelhoComanda espelhoComanda = new EspelhoComanda();
            espelhoComanda.setNumero(Integer.parseInt(sosa98id.getTenumero()));
            espelhoComanda.setPedido(sosa98.getTepedido());
            espelhoComanda.setMesa(sosa98.getTecdmesa());
            espelhoComanda.setComanda(sosa98.getTecomand());
            espelhoComanda.setData(sosa98.getTedatcom());
            espelhoComanda.setNumeroItem(sosa98.getId().getTenumseq());
            espelhoComanda.setReferencia(sosa98.getTerefere());
            espelhoComanda.setPessoasMesa("1");
            espelhoComanda.setQuantidade(sosa98.getTequanti());
            espelhoComanda.setQuantidadeLancada(sosa98.getTequanti());
            espelhoComanda.setVendedor(sosa98.getTevended());
            espelhoComanda.setImpressao(sosa98.getTeimprim());
            espelhoComanda.setStatus(sosa98.getTestatus());
            espelhoComanda.setObservacao(sosa98.getTeobserv());
            espelhoComanda.setStatusItem("N");
            espelhoComanda.setValorItem(1.0);
            if (true) {
                espelhoComanda.setPorcentagem(10d);
                double valorComDezPOrcento = 1 * 0.10;
                espelhoComanda.setValorPorcentagem(valorComDezPOrcento);
            }
            espelhoComandaControle.salvar(espelhoComanda);
        }
    }
}
