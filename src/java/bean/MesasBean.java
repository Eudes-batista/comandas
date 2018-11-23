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
import relatorio.PdfMesa;
import relatorio.Relatorio;
import servico.ComandaService;
import servico.EmpresaService;
import servico.EspelhoComandaService;
import servico.ItemAcompanhamentoService;
import servico.MesaService;
import servico.UsuarioService;
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

    private List<Mesa> mesas = new ArrayList<>();
    private List<Lancamento> lancamentos = new ArrayList<>();
    private List<Comandas> comandas = new ArrayList<>();

    private ImpressaoBean impressaoBean = new ImpressaoBean();
    private Log logMesa;
    private Comandas comanda;
    private Mesa mesa;

    private String usuario;
    private String senha;
    private String mesaDestino;
    private String mesaOrigem;
    private String pesquisa;
    private Status condicao;

    public void init() {
        GerenciaEntrada gerenciaEntrada = new GerenciaEntrada();
        boolean filtroEntrada = gerenciaEntrada.filtroEntrada();
        if (filtroEntrada) {
            logMesa = new Log(controle);
            mesa = new Mesa();
            listarMesas();
        }
    }

    public void listarComandas() {
        comandas.clear();
        List<Object[]> listaComandas = comandaService.listarComandas();
        if (!listaComandas.isEmpty()) {
            listaComandas.forEach((c) -> {
                comandas.add(new Comandas(String.valueOf(c[0]), Double.parseDouble(String.valueOf(c[1])), String.valueOf(c[2]), String.valueOf(c[3]), String.valueOf(c[4]), String.valueOf(c[5])));
            });
        }
    }

    public void pesquisarComanda() {
        comandas.clear();
        List<Object[]> listaComandas = comandaService.pesquisarComandaPorCodigo(pesquisa);
        if (!listaComandas.isEmpty()) {
            listaComandas.forEach((c) -> {
                comandas.add(new Comandas(String.valueOf(c[0]), Double.parseDouble(String.valueOf(c[1])), String.valueOf(c[2]), String.valueOf(c[3]), String.valueOf(c[4]), String.valueOf(c[5])));
            });
        }
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
        return listarMesas.stream().collect(Collectors.groupingBy(Mesa::getMesa));
    }

    private void adicionarMesas(Map<String, List<Mesa>> mapMesas) {
        for (Map.Entry<String, List<Mesa>> map : mapMesas.entrySet()) {
            String key = map.getKey();
            List<Mesa> values = map.getValue();
            int countP = 0;
            for (Mesa mesaStatus : values) {
                if (mesaStatus.getStatus().equals("P")) {
                    countP++;
                    if (countP > 0 && countP < values.size()) {
                        mesas.remove(new Mesa(key));
                        mesas.add(new Mesa(key, "L", mesaStatus.getPedido()));
                    } else if (countP == values.size()) {
                        mesas.remove(new Mesa(key));
                        mesas.add(new Mesa(key, "V", mesaStatus.getPedido()));
                        countP = 0;
                    }
                }
                if ((mesaStatus.getStatus().isEmpty() || mesaStatus.getStatus().equals("null")) && countP == 0) {
                    mesas.add(new Mesa(key, "N", mesaStatus.getPedido()));
                    break;
                }
            }
        }
        mesas.sort((m1, m2) -> m1.getMesa().compareTo(m2.getMesa()));
    }

    public void imprimirPreconta(String mesa) {
        prepararPreconta(mesa);
    }

    public void imprimirPreconta() {
        if (!"RSVA".equals(this.mesa.getMesa()) && Pattern.compile("\\d").matcher(this.mesa.getMesa()).find()) {
            prepararPreconta(String.format("%04d", Integer.parseInt(this.mesa.getMesa())));
            listarMesas();
            this.mesa = null;
            this.mesa = new Mesa();
            PrimeFaces.current().executeScript("PF('dialogoPrecontaRapida').hide();");
        }
    }

    private void prepararPreconta(String mesa1) {
        this.mesa = inserirPessoasNaMesa(mesa1);
        this.mesa.setQuantidadePessoasPagantes(mesa.getQuantidadePessoasPagantes() == null || "".equals(mesa.getQuantidadePessoasPagantes()) ? "1" : mesa.getQuantidadePessoasPagantes());
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
            PdfMesa pdfMesa = new PdfMesa(empresa, mapComanda, comandaService, this.mesa);
            try {
                new ControleImpressao(impressora).imprime(pdfMesa.gerarPdf());
            } catch (FileNotFoundException | DocumentException ex) {
                Messages.addGlobalError("Erro ao encontra o arquivo config.txt");
            } catch (IOException | PrinterException ex) {
                Messages.addGlobalError("Impressora desligada ou não configurada corretamente");
            }
        }
        fecharMesa(mesa1);
        atualizarDataPreContaPessoasPagantes(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), mesa);
    }

    private Mesa inserirPessoasNaMesa(String mesa) {
        int indexMesa = this.mesas.indexOf(new Mesa(mesa));
        this.mesas.set(indexMesa, this.mesa);
        return this.mesas.get(indexMesa);
    }

    public void receberCodigoAutorizacao(Mesa mesa, Status condicao) {
        this.mesa = mesa;
        this.condicao = condicao;
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
            default:
                break;
        }
    }

    private void redirecionarParaComandas() {
        try {
            Faces.redirect("comandas.jsf?id=" + this.mesa.getMesa());
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao tentar abrir comandas");
        }
    }

    private void redirecionarParaLancamentoItens() {
        try {
            String uri = "produtos.jsf?comanda=" + this.comanda.getComanda() + "&mesa=" + this.comanda.getMesa() + "&pessoas=" + this.comanda.getPessoasMesa() + "&pedido=" + this.comanda.getPedido() + "&status=" + this.comanda.getStatus();
            Faces.redirect(uri);
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao tentar abrir comandas");
        }
    }

    private void excluirMesa() {
        logMesa.registrarExclusao(this.mesa.getMesa(), usuario);
        controle.excluirMesa(this.mesa.getMesa());
        mesas.remove(new Mesa(this.mesa.getMesa()));
    }

    public void transferiMesa() {
        if (mesaOrigem == null && mesaDestino == null) {return;}
        formataNumeroMesaDestino();
        if (verificarMesaDestinoIgualMesaOrigem()) {return;}
        if (!mesaDestino.equals("RSVA") && !Pattern.compile("\\d").matcher(mesaDestino).find()) {
            Messages.addGlobalWarn("Coloque o numero da mesa ou a sigla RSVA para resevar a mesa.");
            return;
        }
        Mesa mesaDes = mesas.stream().filter(m -> m.getMesa().equals(mesaDestino)).findFirst().orElse(null);
        if ((mesaDes != null && !mesaDes.getStatus().equals("V")) || mesaDes == null) {
            controle.transferirMesa(this.getMesas().get(this.getMesas().indexOf(new Mesa(mesaOrigem))), mesaDestino.toUpperCase());
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

    public void fecharMesa(String mesa) {
        controle.atualizarStatusPreconta(mesa);
        listarMesas();
    }

    public boolean getStatus(Mesa mesa) {
        return this.mesas != null && mesa != null && mesa.getStatus().equals("V");
    }

    public void abrirMesa(Mesa mesa) {
        if (getStatus(mesa)) {
            receberCodigoAutorizacao(mesa, Status.MESA);
            PrimeFaces.current().executeScript("PF('dialogoUsuario').show();");
        }
    }

    public void abrirComanda(Comandas comanda) {
        mesa = mesas.get(mesas.indexOf(new Mesa(comanda.getMesa())));
        if (getStatus(mesa)) {
            receberCodigoAutorizacao(mesa, Status.COMANDA);
            this.comanda = comanda;
            PrimeFaces.current().executeScript("PF('dialogoUsuario').show();");
        }
        mesa = null;
    }

    private void atualizarDataPreContaPessoasPagantes(String data, Mesa mesa) {
        this.espelhoComandaService.atualizarDataPreconta(data, mesa);
    }

}
