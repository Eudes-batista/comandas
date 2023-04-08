package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Empresa;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaGarcom;
import relatorio.Relatorio;
import servico.EmpresaService;
import servico.RelatorioVendaService;

@ManagedBean(name = "relatorioVendaBean")
@ViewScoped
@Getter
@Setter
public class RelatorioVendaBean implements Serializable {

    @ManagedProperty(value = "#{relatorioVendaService}")
    private RelatorioVendaService relatorioVendaService;
    @ManagedProperty(value = "#{empresaService}")
    private EmpresaService empresaService;

    private List<VendaGarcom> vendaGarcoms;
    private List<RejeicaoPorcentagemVendedor> rejeicaoPorcentagemVendedores;

    private FiltroVendaDetalhe filtroVendaDetalhe;
    private Empresa empresa;

    private int totalItens;
    private double totalComissao;
    private double totalVendas;
    private int quantidadeClientesAtendidos;

    public void init() {
        filtroVendaDetalhe = new FiltroVendaDetalhe();
        LocalDate date = LocalDate.now();
        filtroVendaDetalhe.setDataInicial(date.toString());
        filtroVendaDetalhe.setDataFinal(date.toString());
    }

    public void filtrar() {
        listarVendasGarcom();
        listarRejeicaoGarcons();
        calcularQuantidadeClientesAtendidos();
        Relatorio relatorio = new Relatorio();
        this.empresa = relatorio.setEmpresaService(empresaService).getEmpresa();
    }

    private void calcularQuantidadeClientesAtendidos() {
        quantidadeClientesAtendidos = relatorioVendaService.listarClientesAtendidos(filtroVendaDetalhe).stream().filter(p -> p.getPESSOAS() != null).mapToInt(p -> Integer.parseInt(p.getPESSOAS())).sum();
    }

    private void listarVendasGarcom() {
        this.vendaGarcoms = this.relatorioVendaService.listarVendasGarcom(filtroVendaDetalhe);
        calcularTotais();
    }

    private void listarRejeicaoGarcons() {
        this.rejeicaoPorcentagemVendedores = this.relatorioVendaService.listarRejeicoesPorGarcom(filtroVendaDetalhe);
    }

    private void calcularTotais() {
        this.totalItens = this.vendaGarcoms.stream().mapToInt(v -> v.getITENS().intValue()).sum();
        this.totalComissao = this.vendaGarcoms.stream().mapToDouble(VendaGarcom::getCOMISSAO).sum();
        this.totalVendas = this.vendaGarcoms.stream().mapToDouble(VendaGarcom::getVENDAS).sum();
    }

}
