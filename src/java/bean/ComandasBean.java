package bean;

import com.itextpdf.text.DocumentException;
import controle.ControleImpressao;
import controle.ControleRelatorio;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Comandas;
import modelo.Empresa;
import modelo.ItemAcompanhamento;
import modelo.Lancamento;
import modelo.dto.Cancelamento;
import modelo.dto.Usuario;
import org.ini4j.Ini;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import relatorio.PdfComanda;
import relatorio.Relatorio;
import servico.ComandaService;
import servico.EmpresaService;
import servico.EspelhoComandaService;
import servico.ItemAcompanhamentoService;
import servico.MesaService;
import servico.VendedorService;
import util.GerenciaArquivo;

@ManagedBean(name = "comandasBean")
@ViewScoped
@Getter
@Setter
public class ComandasBean implements Serializable {

    @ManagedProperty(value = "#{controle}")
    private ComandaService controleService;
    @ManagedProperty(value = "#{mesaService}")
    private MesaService mesaService;
    @ManagedProperty(value = "#{mesasBean}")
    private MesasBean mesasBean;
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;
    @ManagedProperty(value = "#{empresaService}")
    private EmpresaService empresaService;
    @ManagedProperty(value = "#{itemAcompanhamentoService}")
    private ItemAcompanhamentoService itemAcompanhamentoService;
    @ManagedProperty(value = "#{espelhoComandaService}")
    private EspelhoComandaService espelhoComandaService;
    @ManagedProperty(value = "#{vendedorService}")
    private VendedorService vendedorService;

    private List<Comandas> comandas = new ArrayList<>();
    private List<Lancamento> lancamentos = new ArrayList<>();
    private List<ItemAcompanhamento> itemAcompanhamentos;

    private ImpressaoBean impressaoBean = new ImpressaoBean();

    private String codigoMesa;
    private double totalMesa;

    private String usuario;
    private String senha;
    private Comandas comanda;
    private String mesaDestino;
    private String comandaOrigem;
    private String pesquisa;
    private String tipoTransferencia;
    private String tipo;
    private Comandas comandaSelecionada;

    public void init() {
        listarComandas();
        totalMesa();
    }

    private void listarComandas() {
        this.comandas = controleService.listarComandasPorMesas(codigoMesa);
        this.itemAcompanhamentos = itemAcompanhamentoService.pesquisarTodos();
    }

    public void listarProdutosAdicionados(String comanda) {
        comandaOrigem = comanda;
        lancamentos.clear();
        controleService.ListarLancamentos(comanda, codigoMesa).forEach(l -> {
            lancamentos.add(new Lancamento(String.valueOf(l[0]),
                    String.valueOf(l[1]),
                    String.valueOf(l[2]),
                    String.valueOf(l[3]),
                    String.valueOf(l[4]),
                    Double.parseDouble(String.valueOf(l[5])),
                    Double.parseDouble(String.valueOf(l[6])),
                    Double.parseDouble(String.valueOf(l[7])),
                    String.valueOf(l[8]),
                    String.valueOf(l[9]),
                    String.valueOf(l[10]),
                    String.valueOf(l[11]).equals("null") ? "" : String.valueOf(l[11]),
                    String.valueOf(l[12]).equals("null") ? "" : String.valueOf(l[12]),
                    String.valueOf(l[13]).equals("null") ? "" : String.valueOf(l[13])
            ));
        });
    }

    public List<ItemAcompanhamento> getItemAcompanhamentos(Lancamento lancamento) {
        List<ItemAcompanhamento> acompanhamentos = null;
        if (lancamento != null) {
            acompanhamentos = this.itemAcompanhamentos.stream().filter(item -> String.valueOf(item.getItem()).equals(lancamento.getItem()) && item.getPedido().equals(lancamento.getPedido())).collect(Collectors.toList());
        }
        return acompanhamentos;
    }

    private void totalMesa() {
        Object somarTotal = mesaService.somarTotal(codigoMesa);
        totalMesa = 0.0;
        if (somarTotal != null) {
            totalMesa = Double.parseDouble(String.valueOf(somarTotal));
        }
    }

    public void selecionarComanda(Comandas comanda) {
        this.comandaSelecionada = comanda;
    }

    public void imprimirPreconta(String comanda) {
        GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();
        Relatorio relatorio = new Relatorio(controleService, empresaService, codigoMesa);
        Comandas comandaInformacoes = this.comandas.get(this.comandas.indexOf(new Comandas(comanda)));
        Empresa empresa = relatorio.getEmpresa();
        List<Lancamento> lancamentosImpressao = new ArrayList<>();
        controleService.ListarLancamentos(comanda, codigoMesa).forEach(l -> {
            Lancamento lancamento = new Lancamento();
            lancamento.setComanda(comanda);
            lancamento.setMesa(codigoMesa);
            lancamento.setReferencia(String.valueOf(l[3]));
            lancamento.setDescricao(String.valueOf(l[4]));
            lancamento.setQuantidade(Double.parseDouble(String.valueOf(l[5])));
            lancamento.setPreco(Double.parseDouble(String.valueOf(l[6])));
            lancamento.setPrecoTotal(Double.parseDouble(String.valueOf(l[7])));
            lancamento.setVendedor(String.valueOf(l[8]));
            lancamento.setPedido(String.valueOf(l[13]));
            lancamentosImpressao.add(lancamento);
        });
        String impressora = gerenciaArquivo.bucarInformacoes().getConfiguracao().getImpressora();
        try {
            PdfComanda pdfComanda = new PdfComanda(lancamentosImpressao, empresa);
            ControleImpressao controleImpressao = new ControleImpressao(impressora);
            File file = pdfComanda.gerarPdf();
            controleImpressao.imprime(file);
            fecharComanda(comandaInformacoes);
        } catch (FileNotFoundException ex) {
            Messages.addGlobalError("Erro ao econtra o arquivo " + ex.getMessage());
        } catch (DocumentException | PrinterException | IOException ex) {
            Messages.addGlobalError("Impressora desligada ou cambo desconectado.\n" + impressora);
        }
        listarComandas();
    }

    public void imprimirPrecontaMesa() {
        mesasBean.setPesquisa(codigoMesa);
        mesasBean.pesquisarMesas();
        mesasBean.imprimirPreconta(codigoMesa);
        listarComandas();
    }

    public void receberCodigoExcluxao(Comandas comanda) {
        this.comanda = comanda;
        this.tipo = "EXCLUIR";
    }

    public void validarUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean fechar;
        this.usuarioBean.setUsuario(new Usuario(this.usuario, this.senha));
        if (!this.usuarioBean.validarGerente()) {
            fechar = false;
            Messages.addGlobalWarn("Essa ação não pode ser executada\n informe um usuario valido ou \nusuario e senha de Gerente");
            context.addCallbackParam("fechar", fechar);
            return;
        }
        if ("EXCLUIR".equals(this.tipo)) {
            excluirComanda();
        } else if ("TRANFERENCIA".equals(this.tipo)) {
            PrimeFaces.current().executeScript("PF('dialogoTransferencia').show();");
        }
        setUsuario("");
        setSenha("");
        fechar = true;
        context.addCallbackParam("fechar", fechar);
    }

    private void excluirComanda() {
        this.controleService.excluirMesa(this.codigoMesa, this.comanda.getCOMANDA());
        this.comandas.remove(this.comanda);
        Cancelamento cancelamento = new Cancelamento();
        cancelamento.setUsuario(this.usuario.toUpperCase());
        cancelamento.setPedidos(this.comanda.getPEDIDO());
        cancelamento.setStatus("D");
        cancelamento.setMotivo(100);
        this.controleService.cancelarPedidos(cancelamento);
        this.espelhoComandaService.atualizarStatusItens(cancelamento);
        totalMesa();
    }

    public void transferirComanda() {
        if (this.mesaDestino == null) {
            return;
        }
        formatarMesaComQuatroDigitos();
        if (!this.mesaDestino.equals("RSVA") && !Pattern.compile("\\d").matcher(this.mesaDestino).find()) {
            Messages.addGlobalWarn("Coloque o numero da mesa ou a sigla RSVA para resevar a mesa.");
            return;
        }
        Comandas cm = this.comandas.stream().filter(c -> c.getCOMANDA().equals(this.comandaOrigem)).findAny().orElse(null);
        if (cm != null && !cm.getSTATUS().equals("P")) {
            transferirMesaComanda(cm);
            atualizarERedirecionar();
            return;
        }
        Messages.addGlobalWarn("Mesa está em preconta " + this.mesaDestino);
    }

    private void formatarMesaComQuatroDigitos() throws NumberFormatException {
        if (Pattern.compile("\\d").matcher(mesaDestino).find()) {
            mesaDestino = String.format("%04d", Integer.parseInt(mesaDestino));
        }
    }

    private void atualizarERedirecionar() {
        if (comandas.size() > 1) {
            comandas.remove(new Comandas(comandaOrigem));
            PrimeFaces.current().ajax().update("frm:tabelaComanda");
            PrimeFaces.current().executeScript("PF('dialogoTransferencia').hide();");
            return;
        }
        try {
            Faces.redirect("mesas.jsf");
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao abrir a pagina das mesas.");
        }
    }

    private void transferirMesaComanda(Comandas comandaOrigem) {
        if ("mesa".equals(this.tipoTransferencia)) {
            controleService.transferirComandaParaMesa(mesaDestino, comandaOrigem);
            return;
        }
        controleService.transferirComandaParaComanda(comandaOrigem, mesaDestino);
    }

    public void pesquisarComanda() {
        this.comandas = controleService.listarComandasPorCodigo(codigoMesa, pesquisa);
    }

    public void fecharComanda(Comandas comanda) {
        controleService.atualizarStatusPreconta(comanda);
    }

    public String getTipoTransferencia() {
        return this.tipoTransferencia == null ? "mesa" : this.tipoTransferencia;
    }

    public void setComandaOrigem(String comanda) {
        this.comandaOrigem = comanda;
        this.tipo = "TRANFERENCIA";
    }

    public void validaVendedor() {
        usuarioBean.setUsuario(new Usuario(usuario, senha));
        String permissao = vendedorService.validarVendedor(usuarioBean.gerarSenha());
        if ("null".equals(permissao)) {
            Messages.addGlobalWarn("Senha incorreta.");
            return;
        }
        imprimirPreconta(this.comandaSelecionada.getCOMANDA());
        espelhoComandaService.atualizarResponsavelPreconta(this.comandaSelecionada.getPEDIDO(), permissao.toUpperCase());
        PrimeFaces.current().executeScript("PF('dialogoVendedor').hide();");
    }
}
