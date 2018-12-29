package bean;

import com.itextpdf.text.DocumentException;
import controle.ControleImpressao;
import controle.ControleRelatorio;
import java.awt.print.PrinterException;
import java.io.File;
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
import org.ini4j.Ini;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import relatorio.PdfDetalhado;
import relatorio.PdfMesa;
import relatorio.Relatorio;
import servico.ComandaService;
import servico.EmpresaService;
import servico.EspelhoComandaService;
import servico.ItemAcompanhamentoService;
import servico.MesaService;
import servico.PdfService;
import servico.UsuarioService;
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
    @ManagedProperty(value = "#{usuarioService}")
    private UsuarioService usuarioService;
    @ManagedProperty(value = "#{empresaService}")
    private EmpresaService empresaService;
    @ManagedProperty(value = "#{espelhoComandaService}")
    private EspelhoComandaService espelhoComandaService;
    @ManagedProperty(value = "#{itemAcompanhamentoService}")
    private ItemAcompanhamentoService itemAcompanhamentoService;
    @ManagedProperty(value = "#{vendedorService}")
    private VendedorService vendedorService;

    private List<Mesa> mesas = new ArrayList<>();
    private List<Lancamento> lancamentos = new ArrayList<>();
    private List<Comandas> comandas = new ArrayList<>();

    private ImpressaoBean impressaoBean = new ImpressaoBean();
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

    public void init() {
        GerenciaEntrada gerenciaEntrada = new GerenciaEntrada();
        boolean filtroEntrada = gerenciaEntrada.filtroEntrada();
        if (filtroEntrada) {
            logMesa = new Log(controle);
            novo();
            listarMesas();
        }
    }

    public void listarComandas() {
        this.comandas = comandaService.listarComandas();
        comandas.sort((c1, c2) -> c1.getCOMANDA().compareTo(c2.getCOMANDA()));
    }

    public void pesquisarComanda() {
        this.comandas = comandaService.pesquisarComandaPorCodigo(pesquisa);
        comandas.sort((c1, c2) -> c1.getCOMANDA().compareTo(c2.getCOMANDA()));
    }

    private void listarMesas() {
        mesas.clear();
        adicionarMesas(separarMesas(this.controle.listarMesas()));
    }

    public void pesquisarMesas() {
        mesas.clear();
        adicionarMesas(separarMesas(controle.listarMesas(pesquisa.toUpperCase())));
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
                        mesas.remove(new Mesa(chave));
                        mesas.add(new Mesa(chave, "L", mesa.getPEDIDO()));
                    } else if (countP == valores.size()) {
                        mesas.remove(new Mesa(chave));
                        mesas.add(new Mesa(chave, "V", mesa.getPEDIDO()));
                        countP = 0;
                    }
                }
                if ((mesa.getSTATUS().isEmpty() || mesa.getSTATUS().equals("null")) && countP == 0) {
                    mesas.add(new Mesa(chave, "N", mesa.getPEDIDO()));
                    break;
                }
            }
        }
        mesas.sort((m1, m2) -> m1.getMESA().compareTo(m2.getMESA()));
    }

    public void imprimirPreconta(String mesa) {
        prepararPreconta(mesa, "normal");
    }

    public void imprimirPreconta() {
        imprimir("normal");
    }

    public void imprimirPrecontaDetalhada() {
        imprimir("detalhada");
    }

    public void imprimirParcial() {
        imprimir("parcial");
    }

    public void imprimir(String tipo) {
        if (!"RSVA".equals(this.mesa.getMESA()) && Pattern.compile("\\d").matcher(this.mesa.getMESA()).find()) {
            prepararPreconta(String.format("%04d", Integer.parseInt(this.mesa.getMESA())), tipo);
            listarMesas();
            PrimeFaces.current().executeScript("PF('dialogoPrecontaRapida').hide();");
        }
    }

    private void prepararPreconta(String mesa1, String tipo) {
        this.mesa = inserirPessoasNaMesa(mesa1);
        this.mesa.setPAGANTES(mesa.getPAGANTES() == null || "".equals(mesa.getPAGANTES()) ? "1" : mesa.getPAGANTES());
        GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();
        Relatorio relatorio = new Relatorio(comandaService, empresaService, mesa1);
        if (gerenciaArquivo.bucarInformacoes().getConfiguracao().getTipoImpressao().equals("rede")) {
            StringBuilder montarCupom = relatorio.montarCupomMesa(mesa1, controle);
            try {
                Ini ini = new Ini(new File(impressaoBean.buscarCaminho()));
                String caminhoDaImpressora = ini.get("LOCAL", "impressora");
                ControleRelatorio.imprimir(caminhoDaImpressora, montarCupom);
            } catch (IOException ex) {
                Messages.addGlobalError("Erro ao encontra o arquivo config.txt");
            }
        } else {
            Empresa empresa = relatorio.getEmpresa();
            Map<String, List<Object[]>> mapComanda = controle.listarComandasPorMesa(mesa1).stream().collect(Collectors.groupingBy(c -> String.valueOf(c[0])));
            String impressora = gerenciaArquivo.getConfiguracao().getImpressora();
            PdfService pdfService;
            if (tipo.equals("normal") || tipo.equals("parcial")) {
                pdfService = new PdfMesa(empresa, mapComanda, comandaService, this.mesa);
            } else {
                pdfService = new PdfDetalhado(empresa, mapComanda, comandaService, mesa, itemAcompanhamentoService);
            }
            try {
                new ControleImpressao(impressora).imprime(pdfService.gerarPdf());
            } catch (FileNotFoundException | DocumentException ex) {
                Messages.addGlobalError("Erro ao encontra o arquivo config.txt");
            } catch (IOException | PrinterException ex) {
                Messages.addGlobalError("Impressora desligada ou não configurada corretamente");
            }
        }
        if ("normal".equals(tipo)) {
            fecharMesa(this.mesa);
        }
    }

    private Mesa inserirPessoasNaMesa(String mesa) {
        int indexMesa = this.mesas.indexOf(new Mesa(mesa));
        Mesa mesaAxu = this.mesas.get(indexMesa);
        mesaAxu.setPAGANTES(this.mesa.getPAGANTES());
        return mesaAxu;
    }

    public void receberCodigoAutorizacao(Mesa mesa, Status condicao) {
        this.mesa = mesa;
        this.condicao = condicao;
        this.mostrareabrimesa = false;
    }

    private String gerarSenha() {
        StringBuilder sb = new StringBuilder();
        this.senha = getSenha();
        String senhaCript = "";
        for (int i = 1; i < 300; i++) {
            sb.append((char) i);
        }
        sb.append(" ");
        for (int i = 1; i <= this.senha.length(); i++) {
            int cod = sb.indexOf(String.valueOf(this.senha.charAt(i - 1))) + (i + 11);
            senhaCript += (char) cod;
        }
        return senhaCript;
    }

    private boolean validarGerente() {
        if (getUsuario().toUpperCase().isEmpty() && gerarSenha().isEmpty()) {
            return false;
        }
        List<Object[]> usuarios = usuarioService.pequisarUsuarios(getUsuario().toUpperCase(), gerarSenha());
        if (usuarios.isEmpty()) {
            return false;
        }
        return String.valueOf(usuarios.get(0)[2]).equals("T");
    }

    public void validarUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean fechar;
        if (validarGerente()) {
            verificarAcaoUsuario();
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
            excluirMesa();
            return;
        }
        switch (this.condicao) {
            case MESA:
                redirecionarParaComandas();
                break;
            case COMANDA:
                redirecionarParaLancamentoItens();
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
            abrirMesa();
            Faces.redirect("comandas.jsf?id=" + this.mesa.getMESA());
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao tentar abrir comandas");
        }
    }

    private void redirecionarParaLancamentoItens() {
        try {
            String uri = "produtos.jsf?comanda=" + this.comanda.getCOMANDA() + "&mesa=" + this.comanda.getMESA() + "&pessoas=" + this.comanda.getPESSOAS() + "&pedido=" + this.comanda.getPEDIDO() + "&status=" + this.comanda.getSTATUS();
            Faces.redirect(uri);
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao tentar abrir comandas");
        }
    }

    private void excluirMesa() {
        logMesa.registrarExclusao(this.mesa.getMESA(), usuario);
        controle.excluirMesa(this.mesa.getMESA());
        mesas.remove(new Mesa(this.mesa.getMESA()));
        espelhoComandaService.atualizarStatusItens(this.mesa.getPEDIDO(),usuario);
    }

    public void transferiMesa() {
        if (mesaOrigem == null && mesaDestino == null) {
            return;
        }
        formataNumeroMesaDestino();
        if (verificarMesaDestinoIgualMesaOrigem()) {
            return;
        }
        if (!mesaDestino.equals("RSVA") && !Pattern.compile("\\d").matcher(mesaDestino).find()) {
            Messages.addGlobalWarn("Coloque o numero da mesa ou a sigla RSVA para resevar a mesa.");
            return;
        }
        Mesa mesaDes = mesas.stream().filter(m -> m.getMESA().equals(mesaDestino)).findFirst().orElse(null);
        if ((mesaDes != null && !mesaDes.getSTATUS().equals("V")) || mesaDes == null) {
            Mesa m = this.getMesas().get(this.getMesas().indexOf(new Mesa(mesaOrigem)));
            controle.transferirMesa(m, mesaDestino.toUpperCase());
            espelhoComandaService.atualizarResponsavelTransferencia(m.getPEDIDO(), usuarioTransferencia.toUpperCase());
            listarMesas();
            PrimeFaces.current().ajax().update("frm:tabelaMesa");
            PrimeFaces.current().executeScript("PF('dialogoTransferencia').hide();");
        } else {
            Messages.addGlobalWarn("Mesa está em preconta " + mesaDestino);
        }
    }

    private boolean verificarMesaDestinoIgualMesaOrigem() {
        if (mesaOrigem.equals(mesaDestino)) {
            Messages.addGlobalWarn("Mesa de destino é a mesma de origem.");
            return true;
        }
        return false;
    }

    private void formataNumeroMesaDestino() throws NumberFormatException {
        if (Pattern.compile("\\d").matcher(mesaDestino).find()) {
            mesaDestino = String.format("%04d", Integer.parseInt(mesaDestino));
        }
    }

    public void fecharMesa(Mesa mesa) {
        controle.atualizarStatusPreconta(mesa, "FECHAR");
        atualizarDataPreContaPessoasPagantes(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), mesa);
        listarMesas();
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
        mesa = mesas.get(mesas.indexOf(new Mesa(comanda.getMESA())));
        if (getSTATUS(mesa)) {
            receberCodigoAutorizacao(mesa, Status.COMANDA);
            this.comanda = comanda;
            this.mostrareabrimesa = false;
            PrimeFaces.current().executeScript("PF('dialogoUsuario').show();");
        }
    }

    private void atualizarDataPreContaPessoasPagantes(String data, Mesa mesa) {
        this.espelhoComandaService.atualizarDataPreconta(data, mesa);
    }

    public void validaVendedor() {
        String permissao = vendedorService.validarVendedor(gerarSenha());
        if (!"null".equals(permissao)) {
            this.vendedor = permissao;
            realizarAcaoDeImpressao(this.tipoImpressao);
            limparStatusJanela();
        } else {
            Messages.addGlobalWarn("Senha incorreta.");
        }
    }

    private void limparStatusJanela() {
        PrimeFaces.current().executeScript("PF('dialogoVendedor').hide();");
        PrimeFaces.current().executeScript("PF('dialogoPreconta').hide();");
        this.senha = "";
        novo();
    }

    public void realizarAcaoDeImpressao(String tipo) {
        if (tipo.equals("parcial")) {
            imprimirParcial();
            espelhoComandaService.atualizarResponsavelParcial(this.mesa.getPEDIDO(), this.vendedor);
            return;
        }
        imprimirPreconta();
        espelhoComandaService.atualizarResponsavelPreconta(this.mesa.getPEDIDO(), this.vendedor);
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
        if (this.reabrimesa) {
            mesa.setSTATUS("");
            controle.atualizarStatusPreconta(mesa, "REABRIR");
            PrimeFaces.current().executeScript("PF('dialogoReabrir').hide();");            
        }
    }

    public void novo() {
        this.mesa = null;
        this.mesa = new Mesa();
    }
    
    public void selecionarComandaParaPreconta(String mesa){
        Mesa mesaParaPreconta = inserirPessoasNaMesa(mesa);
        this.mesa= mesaParaPreconta;
    }
}
