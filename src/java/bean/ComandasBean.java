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
import org.ini4j.Ini;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import relatorio.PdfComanda;
import relatorio.Relatorio;
import servico.ComandaService;
import servico.EmpresaService;
import servico.ItemAcompanhamentoService;
import servico.MesaService;
import servico.UsuarioService;
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
    @ManagedProperty(value = "#{usuarioService}")
    private UsuarioService usuarioService;
    @ManagedProperty(value = "#{empresaService}")
    private EmpresaService empresaService;
    @ManagedProperty(value = "#{itemAcompanhamentoService}")
    private ItemAcompanhamentoService itemAcompanhamentoService;

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

    public void init() {
        listarComandas();
        totalMesa();
    }

    private void listarComandas() {
        comandas.clear();
        List<Object[]> listaComandas = controleService.listarComandasPorMesas(codigoMesa);
        if (!listaComandas.isEmpty()) {
            listaComandas.forEach((c) -> {
                comandas.add(new Comandas(String.valueOf(c[0]), Double.parseDouble(String.valueOf(c[1])), String.valueOf(c[2]), String.valueOf(c[3]), String.valueOf(c[4]), String.valueOf(c[5])));
            });
        }
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

    public void imprimirPreconta(String comanda) {
        GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();
        Relatorio relatorio = new Relatorio(controleService, empresaService, codigoMesa);
        if (gerenciaArquivo.bucarInformacoes().getConfiguracao().getTipoImpressao().equals("rede")) {
            StringBuilder montarCupom = relatorio.montarCupomComanda(comanda);
            try {
                Ini ini = new Ini(new File(impressaoBean.buscarCaminho()));
                ControleRelatorio.imprimir(ini.get("LOCAL").get("impressora"), montarCupom);
                fecharComanda(comanda);
            } catch (IOException ex) {
                Messages.addGlobalError("Erro ao econtra o arquivo " + ex.getMessage());
            }
        } else {
            Empresa empresa = relatorio.getEmpresa();
            List<Lancamento> lancamentos = new ArrayList<>();
            controleService.ListarLancamentos(comanda, codigoMesa).forEach(l -> {
                Lancamento lancamento = new Lancamento();
                lancamento.setComanda(comanda);
                lancamento.setMesa(codigoMesa);
                lancamento.setDescricao(String.valueOf(l[4]));
                lancamento.setQuantidade(Double.parseDouble(String.valueOf(l[5])));
                lancamento.setPreco(Double.parseDouble(String.valueOf(l[6])));
                lancamento.setPrecoTotal(Double.parseDouble(String.valueOf(l[7])));
                lancamento.setVendedor(String.valueOf(l[8]));
                lancamentos.add(lancamento);
            });
            String impressora = gerenciaArquivo.bucarInformacoes().getConfiguracao().getImpressora();
            try {
                PdfComanda pdfComanda = new PdfComanda(lancamentos, empresa);
                ControleImpressao controleImpressao = new ControleImpressao(impressora);
                File file = pdfComanda.gerarPdf();
                controleImpressao.imprime(file);
                fecharComanda(comanda);
            } catch (FileNotFoundException ex) {
                Messages.addGlobalError("Erro ao econtra o arquivo " + ex.getMessage());
            } catch (DocumentException | PrinterException | IOException ex) {
                Messages.addGlobalError("Impressora desligada ou cambo desconectado.\n" + impressora);
            }
        }
        listarComandas();

    }

    public void imprimirPrecontaMesa() {
        mesasBean.imprimirPreconta(codigoMesa);
    }

    public void receberCodigoExcluxao(Comandas comanda) {
        this.comanda = comanda;
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
            setUsuario("");
            setSenha("");
            controleService.excluirMesa(this.codigoMesa, this.comanda.getComanda());
            comandas.remove(this.comanda);
            totalMesa();
            fechar = true;
        } else {
            fechar = false;
            Messages.addGlobalWarn("Essa ação não pode ser executada\n informe um usuario valido ou \nusuario e senha de Gerente");
        }
        context.addCallbackParam("fechar", fechar);
    }

    public void transferirComanda() {
        if (mesaDestino == null) {
            return;
        }
        formatarMesaComQuatroDigitos();
        if (!mesaDestino.equals("RSVA") && !Pattern.compile("\\d").matcher(mesaDestino).find()) {
            Messages.addGlobalWarn("Coloque o numero da mesa ou a sigla RSVA para resevar a mesa.");
            return;
        }
        Comandas cm = comandas.stream().filter(c -> c.getComanda().equals(comandaOrigem)).findAny().orElse(null);
        if (cm != null && !cm.getStatus().equals("P")) {
            transferirMesaComanda(cm);
            atualizarERedirecionar();
            return;
        }
        Messages.addGlobalWarn("Mesa está em preconta " + mesaDestino);
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
        } else {
            controleService.transferirComandaParaComanda(comandaOrigem,mesaDestino);
        }
    }

    public void pesquisarComanda() {
        comandas.clear();
        List<Object[]> listaComandas = controleService.listarComandasPorCodigo(codigoMesa, pesquisa);
        if (!listaComandas.isEmpty()) {
            listaComandas.forEach((c) -> {
                comandas.add(new Comandas(String.valueOf(c[0]), Double.parseDouble(String.valueOf(c[1])), String.valueOf(c[2]), String.valueOf(c[3]), String.valueOf(c[4]), String.valueOf(c[5])));
            });
        }
    }

    public void fecharComanda(String comanda) {
        controleService.atualizarStatusPreconta(comanda);
    }

    public String getTipoTransferencia() {
        return this.tipoTransferencia == null ? "mesa" : this.tipoTransferencia;
    }
}
