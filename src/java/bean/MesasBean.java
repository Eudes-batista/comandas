package bean;

import com.itextpdf.text.DocumentException;
import controle.ControleImpressao;
import java.awt.print.PrinterException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Comandas;
import modelo.Empresa;
import modelo.Lancamento;
import modelo.Mesa;
import modelo.dto.Cancelamento;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import relatorio.PdfDetalhado;
import relatorio.PdfMesa;
import relatorio.PdfMesaParcial;
import relatorio.Relatorio;
import servico.ComandaService;
import servico.EmpresaService;
import servico.EspelhoComandaService;
import servico.ItemAcompanhamentoService;
import servico.MesaService;
import servico.PdfService;
import servico.VendedorService;
import util.GerenciaArquivo;
import util.GerenciaEntrada;
import util.Log;
import util.Status;

@ManagedBean(name = "mesasBean")
@ViewScoped
@Getter
@Setter
public class MesasBean implements Serializable {

    @ManagedProperty(value = "#{mesaService}")
    private MesaService controle;
    @ManagedProperty(value = "#{controle}")
    private ComandaService comandaService;
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;
    @ManagedProperty(value = "#{empresaService}")
    private EmpresaService empresaService;
    @ManagedProperty(value = "#{espelhoComandaService}")
    private EspelhoComandaService espelhoComandaService;
    @ManagedProperty(value = "#{itemAcompanhamentoService}")
    private ItemAcompanhamentoService itemAcompanhamentoService;
    @ManagedProperty(value = "#{vendedorService}")
    private VendedorService vendedorService;

    private List<Mesa> mesas;
    private List<Lancamento> lancamentos;
    private List<Comandas> comandas;

    private ImpressaoBean impressaoBean;
    private Log logMesa;
    private Comandas comanda;
    private Mesa mesa;

    private String usuario;
    private String senha;
    private String usuarioTransferencia;
    private String mesaDestino;
    private String mesaOrigem;
    private String pesquisa;
    private String vendedor;
    private String tipoImpressao;
    private Status condicao;
    private boolean reabrimesa;
    private boolean mostrareabrimesa;
    private boolean transferirComanda;
    
    private SimpleDateFormat dateFormat;

    public void init() {
        GerenciaEntrada gerenciaEntrada = new GerenciaEntrada();
        boolean filtroEntrada = gerenciaEntrada.filtroEntrada();
        if (filtroEntrada) {
            this.logMesa = new Log(this.controle);
            this.mesas = new ArrayList<>();
            this.lancamentos = new ArrayList<>();
            this.comandas = new ArrayList<>();
            this.impressaoBean = new ImpressaoBean();
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.novo();
            this.listarMesas();
        }
    }

    public void listarComandas() {
        this.comandas = this.comandaService.listarComandas();
        this.comandas.sort((c1, c2) -> c1.getCOMANDA().compareTo(c2.getCOMANDA()));
    }

    public void pesquisarComanda() {
        this.comandas = this.comandaService.pesquisarComandaPorCodigo(this.pesquisa);
        this.comandas.sort((c1, c2) -> c1.getCOMANDA().compareTo(c2.getCOMANDA()));
    }

    private void listarMesas() {
        this.mesas.clear();
        this.adicionarMesas(separarMesas(this.controle.listarMesas()));
    }

    public void pesquisarMesas() {
        this.mesas.clear();
        this.adicionarMesas(separarMesas(this.controle.listarMesas(this.pesquisa.toUpperCase())));
    }

    private Map<String, List<Mesa>> separarMesas(List<Mesa> listarMesas) {
        return listarMesas.stream().collect(Collectors.groupingBy(Mesa::getMESA));
    }

    private void adicionarMesas(Map<String, List<Mesa>> mapMesas) {
        for (Map.Entry<String, List<Mesa>> map : mapMesas.entrySet()) {
            String chave = map.getKey();
            List<Mesa> valores = map.getValue();
            int countP = 0;
            for (Mesa mesa : valores) {
                if (mesa.getSTATUS().equals("P")) {
                    countP++;
                    if (countP > 0 && countP < valores.size()) {
                        this.mesas.remove(new Mesa(chave));
                        this.mesas.add(new Mesa(chave, "L", mesa.getPEDIDO()));
                    } else if (countP == valores.size()) {
                        this.mesas.remove(new Mesa(chave));
                        this.mesas.add(new Mesa(chave, "V", mesa.getPEDIDO()));
                        countP = 0;
                    }
                    continue;
                }
                if ((mesa.getSTATUS().isEmpty() || mesa.getSTATUS().equals("null")) && countP == 0) {
                    this.mesas.add(new Mesa(chave, "N", mesa.getPEDIDO()));
                    break;
                }
            }
        }
        this.mesas.sort((m1, m2) -> m1.getMESA().compareTo(m2.getMESA()));
    }

    public void imprimirPreconta(String mesa) {
        this.prepararPreconta(mesa, "normal");
    }
    
    public void reipressaoPreconta(String mesa) {
        this.prepararPreconta(mesa, "");
    }

    public void imprimirPreconta() {
        this.imprimir("normal");
    }

    public void imprimirPrecontaDetalhada() {
        this.imprimir("detalhada");
    }

    public void imprimirParcial() {
        this.imprimir("parcial");
    }

    public void imprimir(String tipo) {
        if (!"RSVA".equals(this.mesa.getMESA()) && Pattern.compile("\\d").matcher(this.mesa.getMESA()).find()) {
            this.prepararPreconta(String.format("%04d", Integer.parseInt(this.mesa.getMESA())), tipo);
            this.listarMesas();
            PrimeFaces.current().executeScript("PF('dialogoPrecontaRapida').hide();");
        }
    }

    private void prepararPreconta(String mesaSelecionada, String tipo) {
        this.mesa = this.inserirPessoasNaMesa(mesaSelecionada);
        this.mesa.setPAGANTES(this.mesa.getPAGANTES() == null || "".equals(this.mesa.getPAGANTES()) ? "1" : this.mesa.getPAGANTES());
        GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();
        Relatorio relatorio = new Relatorio(this.comandaService, this.empresaService, mesaSelecionada);
        Empresa empresa = relatorio.getEmpresa();
        Map<String, List<Object[]>> mapComanda = this.controle.listarComandasPorMesa(mesaSelecionada).stream().collect(Collectors.groupingBy(c -> String.valueOf(c[0])));
        String impressora = gerenciaArquivo.bucarInformacoes().getConfiguracao().getImpressora();
        PdfService pdfService = this.selecionarTipoImpressao(tipo, empresa, mapComanda);
        try {
            new ControleImpressao(impressora).imprime(pdfService.gerarPdf());
        } catch (FileNotFoundException | DocumentException ex) {
            Messages.addGlobalError("Erro ao encontra o arquivo config.txt \n"+ex.getMessage());
        } catch (IOException | PrinterException ex) {
            Messages.addGlobalError("Impressora desligada ou não configurada corretamente");
        }
        if ("normal".equals(tipo)) {
            this.fecharMesa(this.mesa);
        }
    }

    private PdfService selecionarTipoImpressao(String tipo, Empresa empresa, Map<String, List<Object[]>> mapComanda) {
        PdfService pdfService;
        switch (tipo) {
            case "normal":
                pdfService = new PdfMesa(empresa, mapComanda, this.comandaService, this.mesa);
                break;
            case "parcial":
                pdfService = new PdfMesaParcial(empresa, mapComanda, this.comandaService, this.mesa);
                break;
            case "detalhada":
                pdfService = new PdfDetalhado(empresa, mapComanda, this.comandaService, this.mesa, this.itemAcompanhamentoService);                
                break;
            default:
                pdfService = new PdfMesa(empresa, mapComanda, this.comandaService, this.mesa);
                break;
        }
        return pdfService;
    }

    private Mesa inserirPessoasNaMesa(String mesa) {
        int indexMesa = this.mesas.indexOf(new Mesa(mesa));
        Mesa mesaAxu = this.mesas.get(indexMesa);
        if (this.mesa != null) {
            mesaAxu.setPAGANTES(this.mesa.getPAGANTES());
        }
        return mesaAxu;
    }

    public void receberCodigoAutorizacao(Mesa mesa, Status condicao) {
        this.mesa = mesa;
        this.condicao = condicao;
        this.mostrareabrimesa = false;
    }

    public void validarUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean fechar;
        this.usuarioBean.getUsuario().setNOME(this.usuario);
        this.usuarioBean.getUsuario().setSENHA(this.senha);
        if (this.usuarioBean.validarGerente()) {
            this.verificarAcaoUsuario();
            setUsuario("");
            setSenha("");
            fechar = true;
        } else {
            fechar = false;
            Messages.addGlobalWarn("Essa ação não pode ser executada\n informe um usuario valido ou \nusuario e senha de Gerente");
        }
        context.addCallbackParam("fechar", fechar);
    }

    private void verificarAcaoUsuario() {
        if (this.condicao == null) {
            this.excluirMesa();
            return;
        }
        switch (this.condicao) {
            case MESA:
                this.redirecionarParaComandas();
                break;
            case COMANDA:
                this.redirecionarParaLancamentoItens();
                break;
            case TRANSFERENCIA:
                this.usuarioTransferencia = this.usuario;
                PrimeFaces.current().executeScript("PF('dialogoTransferencia').show();");
                break;
            default:
                break;
        }
    }

    private void redirecionarParaComandas() {
        try {
            this.abrirMesa();
            Faces.redirect("comandas.jsf?id=" + this.mesa.getMESA());
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao tentar abrir comandas");
        }
    }

    private void redirecionarParaLancamentoItens() {
        try {
            String uri = "produtos.jsf?comanda=" + this.comanda.getCOMANDA() + "&mesa=" + this.comanda.getMESA() + "&pedido=" + this.comanda.getPEDIDO() + "&status=" + this.comanda.getSTATUS();
            Faces.redirect(uri);
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao tentar abrir comandas");
        }
    }

    private void excluirMesa() {
        this.logMesa.registrarExclusao(this.mesa.getMESA(), this.usuario);
        this.controle.excluirMesa(this.mesa.getMESA());
        this.mesas.remove(new Mesa(this.mesa.getMESA()));
        Cancelamento cancelamento = new Cancelamento();
        cancelamento.setUsuario(this.usuario.toUpperCase());
        cancelamento.setPedidos(this.mesa.getPEDIDO());
        cancelamento.setStatus("M");
        cancelamento.setMotivo(99);
        this.comandaService.cancelarPedidos(cancelamento);
        this.espelhoComandaService.atualizarStatusItens(cancelamento);
    }

    public void transferiMesa() {
        if (this.mesaOrigem == null && this.mesaDestino == null) {
            return;
        }
        this.formataNumeroMesaDestino();
        if (this.verificarMesaDestinoIgualMesaOrigem()) {
            return;
        }
        if (!this.mesaDestino.equals("RSVA") && !Pattern.compile("\\d").matcher(this.mesaDestino).find()) {
            Messages.addGlobalWarn("Coloque o numero da mesa ou a sigla RSVA para resevar a mesa.");
            return;
        }
        Mesa mesaDestinoEncontrada = this.mesas.stream().filter(m -> m.getMESA().equals(this.mesaDestino)).findFirst().orElse(null);
        if ((mesaDestinoEncontrada != null && !mesaDestinoEncontrada.getSTATUS().equals("V")) || mesaDestinoEncontrada == null) {
            Mesa mesaOrigemEncontrada = this.getMesas().get(this.getMesas().indexOf(new Mesa(this.mesaOrigem)));
            if (this.transferirComanda) {
                mesaOrigemEncontrada.setCOMANDA(mesaOrigemEncontrada.getMESA());
            }
            this.controle.transferirMesa(mesaOrigemEncontrada, this.mesaDestino.toUpperCase());
            this.espelhoComandaService.atualizarResponsavelTransferencia(mesaOrigemEncontrada.getPEDIDO(), this.usuarioTransferencia.toUpperCase());
            this.listarMesas();
            PrimeFaces.current().ajax().update("frm:tabelaMesa");
            PrimeFaces.current().executeScript("PF('dialogoTransferencia').hide();");
            return;
        }
        Messages.addGlobalWarn("Mesa está em preconta " + this.mesaDestino);
    }

    private boolean verificarMesaDestinoIgualMesaOrigem() {
        if (this.mesaOrigem.equals(this.mesaDestino)) {
            Messages.addGlobalWarn("Mesa de destino é a mesma de origem.");
            return true;
        }
        return false;
    }

    private void formataNumeroMesaDestino() throws NumberFormatException {
        if (Pattern.compile("\\d").matcher(this.mesaDestino).find()) {
            this.mesaDestino = String.format("%04d", Integer.parseInt(this.mesaDestino));
        }
    }

    public void fecharMesa(Mesa mesa) {
        mesa.setResponsavelPorReabrirMesa("");
        this.controle.atualizarStatusPreconta(mesa, "FECHAR");
        if(this.dateFormat == null){
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        this.atualizarDataPreContaPessoasPagantes(this.dateFormat.format(new Date()), mesa);
        this.listarMesas();
    }

    public boolean getSTATUS(Mesa mesa) {
        return this.mesas != null && mesa != null && mesa.getSTATUS().equals("V");
    }

    public void abrirMesa(Mesa mesa) {
        if (getSTATUS(mesa)) {
            receberCodigoAutorizacao(mesa, Status.MESA);
            this.mostrareabrimesa = true;
            PrimeFaces.current().executeScript("PF('dialogoUsuario').show();");
        }
    }

    public void abrirComanda(Comandas comanda) {
        this.mesa = this.mesas.get(this.mesas.indexOf(new Mesa(comanda.getMESA())));
        if (getSTATUS(this.mesa)) {
            receberCodigoAutorizacao(this.mesa, Status.COMANDA);
            this.comanda = comanda;
            this.mostrareabrimesa = false;
            PrimeFaces.current().executeScript("PF('dialogoUsuario').show();");
        }
    }

    private void atualizarDataPreContaPessoasPagantes(String data, Mesa mesa) {
        this.espelhoComandaService.atualizarDataPreconta(data, mesa);
    }

    public void validaVendedor() {
        this.usuarioBean.getUsuario().setSENHA(senha);
        String permissao = this.vendedorService.validarVendedor(this.usuarioBean.gerarSenha());
        if (!"null".equals(permissao)) {
            this.vendedor = permissao;
            this.realizarAcaoDeImpressao(this.tipoImpressao);
            this.limparStatusJanela();
        } else {
            Messages.addGlobalWarn("Senha incorreta.");
        }
    }

    private void limparStatusJanela() {
        PrimeFaces.current().executeScript("PF('dialogoVendedor').hide();");
        PrimeFaces.current().executeScript("PF('dialogoPreconta').hide();");
        this.senha = "";
        this.novo();
    }

    public void realizarAcaoDeImpressao(String tipo) {
        if (tipo.equals("parcial")) {
            this.imprimirParcial();
            this.espelhoComandaService.atualizarResponsavelParcial(this.mesa.getPEDIDO(), this.vendedor);
            return;
        }
        this.imprimirPreconta();
        this.espelhoComandaService.atualizarResponsavelPreconta(this.mesa.getPEDIDO(), this.vendedor);
    }

    public void setMesaOrigem(String mesaOrigem) {
        this.mesaOrigem = mesaOrigem;
        this.condicao = Status.TRANSFERENCIA;
    }

    public void abrirMesa() {
        if (!this.mesas.contains(this.mesa)) {
            Messages.addGlobalWarn("Não existe essa mesa aberta.");
            return;
        }
        if (this.mesa.getPEDIDO() == null) {
            this.reabrimesa = true;
        }
        this.mesa = this.mesas.get(this.mesas.indexOf(this.mesa));
        this.mesa.setResponsavelPorReabrirMesa("");
        if (this.reabrimesa) {
            this.mesa.setSTATUS("");
            this.mesa.setResponsavelPorReabrirMesa(this.usuario.toUpperCase());
            this.controle.atualizarStatusPreconta(this.mesa, "REABRIR");
            PrimeFaces.current().executeScript("PF('dialogoReabrir').hide();");
        }
    }

    public void novo() {
        this.mesa = null;
        this.mesa = new Mesa();
    }

    public void selecionarComandaParaPreconta(String mesa) {
        Mesa mesaParaPreconta = inserirPessoasNaMesa(mesa);
        this.mesa = mesaParaPreconta;
    }
}
