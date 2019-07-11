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
import modelo.dto.Cancelamento;
import modelo.dto.EspelhoComandaDTO;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import servico.EspelhoComandaService;

@ManagedBean(name = "espelhoComandaBean")
@ViewScoped
@Getter
@Setter
public class EspelhoComandaBean implements Serializable {

    @ManagedProperty(value = "#{espelhoComandaService}")
    private EspelhoComandaService espelhoComandaService;

    public EspelhoComanda espelhoComanda;

    public double valor;

    public List<EspelhoComanda> espelhoComandas;

    public void init() {
        espelhoComanda = null;
        espelhoComanda = new EspelhoComanda();
    }

    public void atualizarPorcemtagemEvalorPorcentagemItens(Comandas comandas) {
        if (this.valor == 0) {
            this.valor = comandas.getTOTAL();
        }
        if (this.valor < comandas.getTOTAL()) {
            Messages.addGlobalWarn("Valor menor que o da venda.");
            return;
        }
        double valorPago = this.valor;
        double valorDaCompra = comandas.getTOTAL();
        double porcentagem = calcularPorcentagem(valorDaCompra, valorPago);
        this.valor = Double.parseDouble(new DecimalFormat("###,##0.00").format(porcentagem).replace(".", "").replace(",", "."));
        espelhoComandaService.atualizarPorcentagem(comandas.getPEDIDO(), this.valor);
        PrimeFaces.current().ajax().update("frmAuditoria:tabela");
        this.valor = 0;
        Messages.addGlobalInfo("Operação realizada com sucesso!!");
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

    public double calcularPorcentagem(double valorCompra, double valorPago) {
        return (valorPago / (valorCompra / 100d) - 100);
    }

    public void atualizarStatusItemParaCancelado(Cancelamento cancelamento) {
        espelhoComandaService.atualizarStatusItens(cancelamento);
    }

    public String buscarDataPreconta(String pedido) {
        return espelhoComandaService.buscarDataPreconta(pedido);
    }

    public void atualizarUsuarioPreconta(String pedido, String usuario) {
        this.espelhoComandaService.atualizarResponsavelPreconta(pedido, usuario);
    }
    
    public EspelhoComandaDTO buscarQuantidadeCanceladaEQuantidadeLancada(String numero) {
        return this.espelhoComandaService.buscarQuantidadeCanceladaElancada(numero);
    }
    

}
