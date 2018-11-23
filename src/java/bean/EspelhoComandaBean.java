package bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Comandas;
import modelo.EspelhoComanda;
import servico.EspelhoComandaService;

@ManagedBean(name = "espelhoComandaBean")
@ViewScoped
public class EspelhoComandaBean implements Serializable {

    @Getter
    @Setter
    @ManagedProperty(value = "#{espelhoComandaService}")
    private EspelhoComandaService espelhoComandaService;

    @Getter
    @Setter
    public EspelhoComanda espelhoComanda;

    @Getter
    @Setter
    public double valor;

    @Getter
    @Setter
    public List<EspelhoComanda> espelhoComandas;

    public void init() {
        espelhoComanda = null;
        espelhoComanda = new EspelhoComanda();
    }

    public void atualizarPorcemtagemEvalorPorcentagemItens(Comandas comandas) {
        Double valorPago = this.valor;
        Double valorDaCompra = comandas.getTotal();
        Double porcentagem = calcularPorcentagem(valorDaCompra, valorPago);
        this.valor = Double.parseDouble(new DecimalFormat("###,##0.00").format(porcentagem).replace(".","").replace(",","."));
        espelhoComandaService.atualizarPorcentagem(comandas.getPedido(),this.valor);
    }

    public void salvar() {
        this.espelhoComandaService.salvar(this.espelhoComanda);
    }

    public void excluir(Integer numero) {
        this.espelhoComandaService.excluir(numero);
    }

    public void alterar() {
        this.espelhoComandaService.alterar(espelhoComanda);
    }

    public EspelhoComanda buscarPorId(Integer numero) {
        return this.espelhoComandaService.buscarPorId(numero);
    }

    public void salvarPessoaReipressao(String usuario, String item) {
        this.espelhoComandaService.salvarPessoaQueAutorizouReipressao(usuario, item);
    }

    private void listarAuditoria() {
        this.espelhoComandas = this.espelhoComandaService.listarAuditoria();
    }

    private double calcularPorcentagem(double valorCompra, double valorPago) {
        return (valorPago / (valorCompra / 100d) - 100);
    }

}
