package bean;

import com.itextpdf.text.DocumentException;
import controle.ControleImpressao;
import controle.ControlePedido;
import controle.ControleRelatorio;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Acompanhamento;
import modelo.Comandas;
import modelo.Configuracao;
import modelo.EspelhoComanda;
import modelo.GrupoAcompanhamento;
import modelo.ItemAcompanhamento;
import modelo.Lancamento;
import modelo.Lapt51;
import modelo.Produto;
import modelo.Sosa98;
import modelo.Sosa98Id;
import modelo.dto.EspelhoComandaDTO;
import modelo.dto.ItemCanceladoGarcom;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import relatorio.PdfCancelamento;
import relatorio.PdfPedido;
import relatorio.Relatorio;
import servico.AcompanhamentoService;
import servico.ComandaService;
import servico.GrupoAcompanhamentoService;
import servico.GrupoServico;
import servico.ItemAcompanhamentoService;
import servico.PdfService;
import servico.ProdutoService;
import servico.SubGrupoService;
import servico.UsuarioService;
import servico.VendedorService;
import util.GerenciaArquivo;
import util.Log;

@ManagedBean(name = "produtoBean")
@ViewScoped
@Getter
@Setter
public class ProdutoBean implements Serializable {

    @ManagedProperty(value = "#{grupoServico}")
    private GrupoServico grupoServico;
    @ManagedProperty(value = "#{produtoServico}")
    private ProdutoService produtoServico;
    @ManagedProperty(value = "#{subGrupoService}")
    private SubGrupoService subGrupoService;
    @ManagedProperty(value = "#{usuarioService}")
    private UsuarioService usuarioService;
    @ManagedProperty(value = "#{controle}")
    private ComandaService controleService;
    @ManagedProperty(value = "#{vendedorService}")
    private VendedorService vendedorService;
    @ManagedProperty(value = "#{impressaoBean}")
    private ImpressaoBean impressaoBean;
    @ManagedProperty(value = "#{espelhoComandaBean}")
    private EspelhoComandaBean espelhoComandaBean;
    @ManagedProperty(value = "#{grupoAcompanhamentoService}")
    private GrupoAcompanhamentoService grupoAcompanhamentoService;
    @ManagedProperty(value = "#{acompanhamentoService}")
    private AcompanhamentoService acompanhamentoService;
    @ManagedProperty(value = "#{itemAcompanhamentoService}")
    private ItemAcompanhamentoService itemAcompanhamentoService;
    @ManagedProperty(value = "#{motivoCancelamentoBean}")
    private MotivoCancelamentoBean motivoCancelamentoBean;

    private List<Lapt51> grupos;
    private List<Produto> produtos = new ArrayList<>();
    private List<Lancamento> lancamentosAdicionados = new ArrayList<>();
    private List<Lancamento> lancamentosAdicionadosAuxlizar = new ArrayList<>();
    private List<Lancamento> lancamentosSelecionadadosTransferencia;
    private List<Lancamento> lancamentosTransferencias;
    private List<Acompanhamento> acompanhamentos;
    private List<GrupoAcompanhamento> grupoAcompanhamentos;
    private List<String> itensAcompanhamentos;

    private String comanda;
    private String mesa;
    private String vendedor;
    private String quantidadePessoas;

    private int quantidadeItensAdicionados;
    private double quantidade;
    private double valorTotalItens;
    private String pesquisa;
    private String senha;
    private String usuario;
    private String condicao;
    private String mensagem;
    private String pedido;
    private String status;
    private String usuarioTransferencia;

    private Lancamento lancamentoAcompanhamento = new Lancamento();
    private ControlePedido controlePedido;
    private Produto produto = new Produto();
    private Lancamento lancamento;
    private GrupoAcompanhamento grupoAcompanhamento;
    private Comandas comandaTransferencia;
    private Log log = new Log();

    public void init() {
        if (this.mesa == null || this.comanda == null) {
            mensagem = "Mesa ou comanda inexistente.";
            PrimeFaces.current().executeScript("PF('dialogoImpressao').show();");
            return;
        }
        listarProdutos();
        controlePedido = new ControlePedido(controleService, comanda);
        pedido = pedido == null ? controlePedido.gerarNumero() : pedido;
        status = status == null ? "" : status;
        int buscarNumeroDePessoas = this.controleService.buscarNumeroDePessoas(pedido);
        if (buscarNumeroDePessoas == 0) {
            if (quantidadePessoas != null) {
                quantidadePessoas = String.valueOf(Integer.parseInt(quantidadePessoas));
            } else {
                quantidadePessoas = "1";
            }
        } else {
            quantidadePessoas = String.valueOf(buscarNumeroDePessoas);
        }
    }

    public void listarGrupoAcompanhamentos() {
        grupoAcompanhamentos = grupoAcompanhamentoService.pesquisarTodos();
    }

    public void listarAcompanhamentosPorGrupo() {
        if (grupoAcompanhamento != null) {
            acompanhamentos = acompanhamentoService.pesquisarPorGrupo(grupoAcompanhamento.getNome());
            return;
        }
        if (!grupoAcompanhamentos.isEmpty()) {
            acompanhamentos = acompanhamentoService.pesquisarPorGrupo(grupoAcompanhamentos.get(0).getNome());
        }

    }

    public void listarGrupos() {
        grupos = grupoServico.listarGrupos();
    }

    public void listarProdutos() {
        produtos = produtoServico.lsitarProdutos();
    }

    public void listarProdutoPorGrupo(Lapt51 lapt51) {
        produtos = produtoServico.lsitarProdutoPorGrupo(lapt51.getT51cdgrp());
        mudarQuantidade();
    }

    public void pesquisarProduto() {
        produtos = produtoServico.listarPorReferenciaDescricaoCodigoBarra(pesquisa == null ? "" : pesquisa.toUpperCase());
        mudarQuantidade();
    }

    private void mudarQuantidade() {
        if (quantidade != 1) {
            quantidade = 1;
        }
    }

    public void listarProdutosAdicionados() {
        lancamentosAdicionados.clear();
        lancamentosAdicionadosAuxlizar.clear();
        controleService.ListarLancamentos(comanda, mesa).forEach(l -> {
            lancamentosAdicionados.add(new Lancamento(String.valueOf(l[0]),
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
            lancamentosAdicionadosAuxlizar.add(new Lancamento(String.valueOf(l[0]),
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
                    String.valueOf(l[13]).equals("null") ? "" : String.valueOf(l[13])));
        });
        totalizarItensAdicionado();
    }

    private void listarItensAcompanhamento(String item, String pedido) {
        this.itensAcompanhamentos = this.itemAcompanhamentoService.pesquisarItem(item, pedido).stream().map(ItemAcompanhamento::getAcompanhamento).collect(Collectors.toList());
    }

    public void totalizarItensAdicionado() {
        this.valorTotalItens = this.lancamentosAdicionados.stream().mapToDouble(Lancamento::getPrecoTotal).sum();
    }

    public void adicionarItemQuantidadeMetade(Produto p) {
        if (verificarSeComandaJaExiste()) {
            return;
        }
        Lancamento lancamentoItem = new Lancamento();
        lancamentoItem.setComanda(this.comanda);
        lancamentoItem.setMesa(this.mesa);
        String item = this.controleService.gerarSequencia(this.comanda);
        lancamentoItem.setItem(item);
        lancamentoItem.setNumero(this.controleService.gerarNumero());
        lancamentoItem.setReferencia(p.getReferencia());
        lancamentoItem.setDescricao(p.getDescricao());
        lancamentoItem.setQuantidade(0.5);
        lancamentoItem.setPreco(p.getPreco());
        lancamentoItem.setPrecoTotal(p.getPreco() * quantidade);
        lancamentoItem.setVendedor(this.vendedor);
        lancamentoItem.setImprimir("0");
        lancamentoItem.setStatus(this.status);
        lancamentoItem.setPedido(this.pedido);
        this.produto = p;
        preparaItem(lancamentoItem);
    }

    public void adicionarItem(Produto p) {
        if (verificarSeComandaJaExiste()) {
            return;
        }
        Lancamento lancamentoItem = new Lancamento();
        lancamentoItem.setComanda(this.comanda);
        lancamentoItem.setMesa(this.mesa);
        String item = this.controleService.gerarSequencia(this.comanda);
        lancamentoItem.setItem(item);
        lancamentoItem.setNumero(this.controleService.gerarNumero());
        lancamentoItem.setReferencia(p.getReferencia());
        lancamentoItem.setDescricao(p.getDescricao());
        lancamentoItem.setQuantidade(this.quantidade);
        lancamentoItem.setPreco(p.getPreco());
        lancamentoItem.setPrecoTotal(p.getPreco() * quantidade);
        lancamentoItem.setVendedor(this.vendedor);
        lancamentoItem.setImprimir("0");
        lancamentoItem.setStatus(this.status);
        lancamentoItem.setPedido(this.pedido);
        this.produto = p;
        preparaItem(lancamentoItem);
    }

    public void preparaItem(Lancamento lancamento) {
        this.lancamentosAdicionados.add(lancamento);
        this.quantidadeItensAdicionados += 1;
        salvar(lancamento);
    }

    public void adicionarItem() {
        if (verificarSeComandaJaExiste()) {
            return;
        }
        this.produto = this.produtoServico.buscarProduto(produto.getReferencia());
        if (this.produto == null) {
            this.produto = new Produto();
            Messages.addGlobalWarn("Produto não encontrado.");
            return;
        }
        adicionarItem(this.produto);
    }

    public void salvoAcompanhamento() {
        itemAcompanhamentoService.excluirTodos(lancamentoAcompanhamento.getItem(), pedido);
        if (itensAcompanhamentos != null) {
            for (int i = 0; i < itensAcompanhamentos.size(); i++) {
                ItemAcompanhamento itemAcompanhamento = new ItemAcompanhamento();
                itemAcompanhamento.setAcompanhamento(itensAcompanhamentos.get(i));
                itemAcompanhamento.setItem(Integer.parseInt(lancamentoAcompanhamento.getItem()));
                itemAcompanhamento.setNumeroItem(String.valueOf(i + 1));
                itemAcompanhamento.setPedido(pedido);
                itemAcompanhamento.setStatus("N");
                itemAcompanhamentoService.salvar(itemAcompanhamento);
            }
        }
        if (!lancamentoAcompanhamento.getObservacao().isEmpty()) {
            alterar(lancamentoAcompanhamento);
        }
    }

    public void salvar(Lancamento lancamento) {
        try {
            Date data = new Date();
            controleService.salvar(new Sosa98(new Sosa98Id(lancamento.getNumero(), lancamento.getItem()),
                    lancamento.getComanda(),
                    lancamento.getReferencia(),
                    lancamento.getQuantidade(),
                    data,
                    lancamento.getVendedor(),
                    lancamento.getObservacao(),
                    lancamento.getMesa(),
                    lancamento.getStatus(),
                    lancamento.getImprimir(),
                    lancamento.getPedido()
            ));
            salvarEspelho(lancamento, data);
            log.salvarLancamento(lancamento, vendedor);
        } catch (Exception ex) {
            this.mensagem = "Erro na comunicação do servidor, verifique a lista dos itens\n e lançe novamente.";
            PrimeFaces.current().executeScript("PF('dialogoErro').show();");
        }
    }

    private void salvarEspelho(Lancamento lancamento, Date data) {
        EspelhoComanda espelhoComanda = new EspelhoComanda();
        espelhoComanda.setNumero(Integer.parseInt(lancamento.getNumero()));
        espelhoComanda.setPedido(lancamento.getPedido());
        espelhoComanda.setMesa(lancamento.getMesa());
        espelhoComanda.setComanda(lancamento.getComanda());
        espelhoComanda.setData(data);
        espelhoComanda.setNumeroItem(lancamento.getItem());
        espelhoComanda.setReferencia(lancamento.getReferencia());
        espelhoComanda.setPessoasMesa(quantidadePessoas);
        espelhoComanda.setQuantidade(lancamento.getQuantidade());
        espelhoComanda.setQuantidadeLancada(lancamento.getQuantidade());
        espelhoComanda.setVendedor(lancamento.getVendedor());
        espelhoComanda.setImpressao(lancamento.getImprimir());
        espelhoComanda.setStatus(lancamento.getStatus());
        espelhoComanda.setObservacao(lancamento.getObservacao());
        espelhoComanda.setStatusItem("N");
        espelhoComanda.setValorItem(lancamento.getPreco());
        if ("P".equals(status)) {
            try {
                String dataPreconta = espelhoComandaBean.buscarDataPreconta(pedido);
                Date dataPrecontaBanco = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataPreconta);
                espelhoComanda.setDataPreconta(dataPrecontaBanco);
            } catch (ParseException ex) {
                mensagem = "Erro ao converter data da preconta.";
                PrimeFaces.current().executeScript("PF('dialogoErro').show();");
            }
        }
        if (!new GerenciaArquivo().bucarInformacoes().getConfiguracao().getCobraDezPorcento().isEmpty()) {
            espelhoComanda.setPorcentagem(10d);
            double valorComDezPOrcento = lancamento.getPrecoTotal() * 0.10;
            espelhoComanda.setValorPorcentagem(valorComDezPOrcento);
        }
        espelhoComandaBean.setEspelhoComanda(espelhoComanda);
        espelhoComandaBean.salvar();
    }

    private boolean verificarSeComandaJaExiste() {
        if (vendedor == null || "".equals(vendedor) || this.quantidadePessoas == null) {
            return true;
        }
        if (this.mesa == null || this.comanda == null) {
            this.mensagem = "Erro inclusão do Item, verifique a lista dos itens\n e lançe novamente.";
            PrimeFaces.current().executeScript("PF('dialogoErro').show();");
            return true;
        }
        String verificarComanda = controleService.verificarComandaNaMesa(comanda);
        if (!verificarComanda.equals(mesa) && !"0".equals(verificarComanda)) {
            mensagem = "Comanda já existe em outra mesa.";
            PrimeFaces.current().executeScript("PF('dialogoImpressao').show();");
            return true;
        }
        return false;
    }

    private void alterar(Lancamento lancamento) {
        Sosa98 sosa98 = new Sosa98();
        sosa98.setTeobserv(lancamento.getObservacao());
        sosa98.setId(new Sosa98Id(lancamento.getNumero()));
        controleService.alterar(sosa98);
    }

    public void excluirItem(Lancamento lancamento) {
        controleService.excluir(lancamento.getNumero());
        if ("0".equals(lancamento.getImprimir())) {
            espelhoComandaBean.excluir(Integer.parseInt(lancamento.getNumero()));
            itemAcompanhamentoService.excluirTodos(lancamento.getItem(), lancamento.getPedido());
        }
        lancamentosAdicionados.remove(lancamento);
        totalizarItensAdicionado();
        quantidadeItensAdicionados = lancamentosAdicionados.isEmpty() ? 0 : quantidadeItensAdicionados;
    }

    public void receberCodigo(Lancamento lancamento, String condicao) {
        this.lancamento = lancamento;
        this.condicao = condicao;
        if ("E".equals(condicao)) {
            this.quantidade = this.lancamento.getQuantidade();
            this.espelhoComandaBean.init();
            this.motivoCancelamentoBean.listarTodos();
            return;
        }
        if ("T".equals(condicao)) {
            this.lancamentosTransferencias = this.lancamentosAdicionados;
            comandaTransferencia = new Comandas();
        }
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
            acaoAposValidarUsuario();
            fechar = true;
        } else {
            fechar = false;
            Messages.addGlobalWarn("Essa ação não pode ser executada\n informe um usuario valido ou \nusuario e senha de Gerente");
        }
        context.addCallbackParam("fechar", fechar);
    }

    private void acaoAposValidarUsuario() {
        switch (condicao) {
            case "E":
                excluirItem(this.lancamento);
                break;
            case "R":
                reipressao(this.lancamento);
                break;
            case "T":
                if ("P".equals(this.status)) {
                    mensagem = "Comanda em preconta, reabra a comanda para transferir.";
                    PrimeFaces.current().executeScript("PF('dialogoImpressao').show();");
                } else {
                    this.usuarioTransferencia = this.usuario;
                    PrimeFaces.current().executeScript("PF('sidebarTransferenciaItens').show();");
                }
                break;
        }
    }

    private String validarAntesImprimir(String referencia) {
        List<Object[]> subgrupo;
        Profile.Section value = null;
        subgrupo = this.subGrupoService.listarSubGrupo(referencia);
        try {
            Ini ini = new Ini(new File(impressaoBean.buscarCaminho()));
            String sub = String.valueOf(subgrupo.get(0));
            value = ini.get(sub);
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao inicia arquivo.");
        }
        return value != null ? value.getName() : "";
    }

    private void imprimir(String subgrupo, List<Lancamento> lancamentos, Lancamento lanc) {
        if (validarGrupo(subgrupo)) {
            return;
        }
        try {
            String caminhoDaImpressora = buscarCaminhoImpressora(subgrupo);
            Configuracao configuracao = new GerenciaArquivo().bucarInformacoes().getConfiguracao();
            if (configuracao.getTipoImpressao().equals("rede")) {
                StringBuilder cupom = new Relatorio().montarCupomPedido(lancamentos, lanc);
                ControleRelatorio.imprimir(caminhoDaImpressora, cupom);
            } else {
                ControleImpressao controleImpressao = new ControleImpressao(caminhoDaImpressora);
                PdfService pdfPedido = new PdfPedido(lancamentos, lanc, itemAcompanhamentoService);
                File gerarPdf = pdfPedido.gerarPdf();
                controleImpressao.imprime(gerarPdf);
            }
            controleService.atualizarStatusImpressao(comanda);
            espelhoComandaBean.getEspelhoComandaService().atualizarStatusImpressao(this.pedido);
            listarProdutosAdicionados();
        } catch (IOException ex) {
            mensagem = "Impressora desligada ou cambo desconectado.";
        } catch (DocumentException | PrinterException ex) {
            mensagem = "Erro ao gerar cupom de pedido. " + ex.getMessage();
        }
    }

    private String buscarCaminhoImpressora(String subgrupo) {
        try {
            Ini ini = new Ini(new File(impressaoBean.buscarCaminho()));
            String caminhoDaImpressora = ini.get(subgrupo) != null ? ini.get(subgrupo).get("impressora") : "";
            return caminhoDaImpressora;
        } catch (IOException ex) {
            mensagem = "Grupo não está configurado no sistema de impressão.";
            return "";
        }
    }

    private boolean validarGrupo(String subgrupo) {
        if (subgrupo == null && "".equals(subgrupo)) {
            mensagem = "Nenhum Grupo de Impressão foi encontrado nesse item.";
            PrimeFaces.current().executeScript("PF('dialogoImpressao').show();");
            return true;
        }
        return false;
    }

    public void imprimirTodos() {
        List<Lancamento> lancamentosMemoria = this.lancamentosAdicionados;
        if (!lancamentosMemoria.isEmpty()) {
            listarProdutosAdicionados();
            if (lancamentosMemoria.size() != this.lancamentosAdicionados.size()) {
                mensagem = "Erro na comunicação do servidor, verifique a lista dos itens e lançe novamente.";
                PrimeFaces.current().executeScript("PF('dialogoErro').show();");
                return;
            }
            long count = this.lancamentosAdicionados.stream().filter(l -> l.getDescricao().contains("COUVERT")).count();
            if (count != 0) {
                controleService.atualizarStatusImpressao(comanda);
                espelhoComandaBean.getEspelhoComandaService().atualizarStatusImpressao(this.pedido);
            }
            Map<String, List<Lancamento>> mapLanmentos = separarLancamentoPorGrupo();
            if (!mapLanmentos.isEmpty()) {
                mapLanmentos.forEach((subgrupo, lancamentos) -> imprimir(subgrupo, lancamentos, null));
                this.quantidadeItensAdicionados = 0;
                mensagem = "Pedido enviado para impressão.";
                PrimeFaces.current().executeScript("PF('dialogoImpressao').show();");
                return;
            }
        }
        mensagem = "Itens já foram enviados para impressão.";
        PrimeFaces.current().executeScript("PF('dialogoImpressao').show();");
    }

    public void reipressao(Lancamento lancamento) {
        String subgrupo = validarAntesImprimir(lancamento.getReferencia());
        imprimir(subgrupo, null, lancamento);
        this.espelhoComandaBean.salvarPessoaReipressao(usuario, lancamento.getNumero());
    }

    private Map<String, List<Lancamento>> separarLancamentoPorGrupo() {
        Map<String, List<Lancamento>> mapLanmentos = new HashMap<>();
        List<Lancamento> lancamentosGrupos;
        for (Lancamento lancamento2 : lancamentosAdicionados) {
            if (lancamento2.getImprimir().equals("0") && !lancamento2.getDescricao().contains("COUVERT")) {
                String grupoLancamento = validarAntesImprimir(lancamento2.getReferencia());
                lancamentosGrupos = mapLanmentos.get(grupoLancamento);
                if (lancamentosGrupos == null) {
                    lancamentosGrupos = new ArrayList<>();
                }
                lancamentosGrupos.add(lancamento2);
                mapLanmentos.put(grupoLancamento, lancamentosGrupos);
            }
        }
        return mapLanmentos;
    }

    public void validaVendedor() {
        String permissao = vendedorService.validarVendedor(gerarSenha());
        if (!"null".equals(permissao)) {
            vendedor = permissao;
            PrimeFaces.current().executeScript("PF('dialogoVendedor').hide();");
            this.senha = "";
        } else {
            Messages.addGlobalWarn("Senha incorreta.");
        }

    }

    public void adidcionarAcompanhamento(String acompanhamento) {
        this.itensAcompanhamentos = this.itensAcompanhamentos == null ? new ArrayList<>() : this.itensAcompanhamentos;
        this.itensAcompanhamentos.add(acompanhamento);
    }

    public double getQuantidade() {
        if (quantidade == 0) {
            quantidade = 1;
        }
        return quantidade;
    }

    public void setLancamentoAcompanhamento(Lancamento lancamentoAcompanhamento) {
        this.lancamentoAcompanhamento = lancamentoAcompanhamento;
        listarGrupoAcompanhamentos();
        listarAcompanhamentosPorGrupo();
        listarItensAcompanhamento(lancamentoAcompanhamento.getItem(), pedido);
    }

    public void excluirProdutoJaImpresso() {
        if (validarGerente()) {
            excluirProdutoJaImpressoSosa98();
            alterarEspelhoComandaItemExcluido();
            ItemCanceladoGarcom canceladoGarcom = preencherInformacoesCancelamento();
            try {
                imprimirCancelamento(lancamento, canceladoGarcom);
                PrimeFaces.current().executeScript("PF('dialogoCancelamento').hide()");
            } catch (DocumentException | IOException | PrinterException ex) {
                Messages.addGlobalWarn("Erro ao imprimir cancelamento\n verifique se a impressora está ligada.");
            }
            this.lancamento = null;
            this.lancamento = new Lancamento();
            this.espelhoComandaBean.espelhoComanda = null;
            this.usuario = "";
            this.senha = "";
        } else {
            Messages.addGlobalWarn("Usuário não autorizado.");
        }
    }

    private ItemCanceladoGarcom preencherInformacoesCancelamento() {
        ItemCanceladoGarcom canceladoGarcom = new ItemCanceladoGarcom();
        canceladoGarcom.setCANCELAMENTO(this.quantidade);
        int codigoMotivoCancelamento = espelhoComandaBean.espelhoComanda.getCodigoMotivoCancelamento();
        canceladoGarcom.setMOTIVO(this.motivoCancelamentoBean.buscarPorCodigo(codigoMotivoCancelamento).getNome());
        canceladoGarcom.setOBSERVACAO(this.espelhoComandaBean.espelhoComanda.getObservacaoMotivo());
        canceladoGarcom.setPRODUZIDO(this.espelhoComandaBean.espelhoComanda.getFoiProduzido() ? "SIM" : "NÃO");
        canceladoGarcom.setRESPONSAVEL(this.espelhoComandaBean.espelhoComanda.getRespansavelCancelamento());
        return canceladoGarcom;
    }

    private void alterarEspelhoComandaItemExcluido() {
        EspelhoComanda espelhoComanda = this.espelhoComandaBean.espelhoComanda;
        this.espelhoComandaBean.setEspelhoComanda(this.espelhoComandaBean.buscarPorId(Integer.parseInt(this.lancamento.getNumero())));
        this.espelhoComandaBean.espelhoComanda.setNumero(Integer.parseInt(this.lancamento.getNumero()));
        EspelhoComandaDTO espelhoComandaDTO = this.espelhoComandaBean.buscarQuantidadeCanceladaEQuantidadeLancada(this.lancamento.getNumero());
        if (this.lancamento.getQuantidade() != this.quantidade) {
            double quantidadeCancelada = espelhoComandaDTO == null ? this.quantidade : espelhoComandaDTO.getQUANTIDADE_CANCELADA() + this.quantidade;
            this.espelhoComandaBean.espelhoComanda.setQuantidadeCancelada(quantidadeCancelada);
            double quantidadeRestante = espelhoComandaDTO == null ? this.lancamento.getQuantidade() - quantidadeCancelada : espelhoComandaDTO.getQUANTIDADE_LANCADA() - quantidadeCancelada;
            this.espelhoComandaBean.espelhoComanda.setQuantidade(quantidadeRestante);
        } else {
            this.espelhoComandaBean.espelhoComanda.setQuantidade(0.0);
            this.espelhoComandaBean.espelhoComanda.setQuantidadeCancelada(this.quantidade);
            this.espelhoComandaBean.espelhoComanda.setStatusItem("C");
        }
        this.espelhoComandaBean.espelhoComanda.setRespansavelCancelamento(this.usuario.toUpperCase());
        this.espelhoComandaBean.espelhoComanda.setCodigoMotivoCancelamento(espelhoComanda.getCodigoMotivoCancelamento());
        this.espelhoComandaBean.espelhoComanda.setObservacaoMotivo(espelhoComanda.getObservacaoMotivo());
        this.espelhoComandaBean.espelhoComanda.setFoiProduzido(espelhoComanda.getFoiProduzido());
        this.espelhoComandaBean.espelhoComanda.setDataCancelamento(new Date());
        this.espelhoComandaBean.alterar();
        this.itemAcompanhamentoService.atualizarStatusAcompanhamento(lancamento, "C");
    }

    private void excluirProdutoJaImpressoSosa98() {
        if (this.lancamento.getQuantidade() == this.quantidade) {
            this.controleService.excluir(lancamento.getNumero());
            this.lancamentosAdicionados.remove(this.lancamento);
        } else {
            double qtd = lancamento.getQuantidade() - quantidade;
            this.controleService.alterarQuantidadeItem(qtd, lancamento.getNumero());
            listarProdutosAdicionados();
        }
    }

    public void transferirItensParaMesaComanda() {
        if (lancamentosSelecionadadosTransferencia.isEmpty()) {
            Messages.addGlobalWarn("Nenhum item selecionado.");
            return;
        }
        controleService.transferenciaItensParaComanda(comandaTransferencia, lancamentosSelecionadadosTransferencia, lancamentosAdicionadosAuxlizar, usuarioTransferencia.toUpperCase());
        if (lancamentosAdicionados.size() == lancamentosSelecionadadosTransferencia.size()) {
            try {
                Faces.redirect("mesas.jsf");
            } catch (IOException ex) {
                Messages.addGlobalError("Não foi possivel redirecionar para mesas.");
            }
            return;
        }
        PrimeFaces.current().executeScript("PF('sidebarTransferenciaItens').hide();");
        listarProdutosAdicionados();
    }

    public void pegarQuantidadeTransferencia(Lancamento lancamentoTransferencia) {
        this.lancamento = lancamentoTransferencia;
    }

    private void imprimirCancelamento(Lancamento lancamento, ItemCanceladoGarcom itemCanceladoGarcom) throws FileNotFoundException, DocumentException, IOException, PrinterException {
        PdfService pdfService = new PdfCancelamento(lancamento, itemCanceladoGarcom, itemAcompanhamentoService);
        File pdf = pdfService.gerarPdf();
        String impressora = new GerenciaArquivo().bucarInformacoes().getConfiguracao().getImpressora();
        ControleImpressao controleImpressao = new ControleImpressao(impressora);
        controleImpressao.imprime(pdf);
    }

}
