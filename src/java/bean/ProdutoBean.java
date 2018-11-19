package bean;

import com.itextpdf.text.DocumentException;
import controle.ControleImpressao;
import controle.ControlePedido;
import controle.ControleRelatorio;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
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
import modelo.Configuracao;
import modelo.EspelhoComanda;
import modelo.GrupoAcompanhamento;
import modelo.ItemAcompanhamento;
import modelo.Lancamento;
import modelo.Lapt51;
import modelo.Produto;
import modelo.Sosa98;
import modelo.Sosa98Id;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import relatorio.PdfPedido;
import relatorio.Relatorio;
import servico.AcompanhamentoService;
import servico.ComandaService;
import servico.GrupoAcompanhamentoService;
import servico.GrupoServico;
import servico.ItemAcompanhamentoService;
import servico.ProdutoService;
import servico.SubGrupoService;
import servico.UsuarioService;
import servico.VendedorService;
import util.GerenciaArquivo;

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

    private Lancamento lancamentoAcompanhamento = new Lancamento();
    private ControlePedido controlePedido;
    private Produto produto = new Produto();
    private Lancamento lancamento;
    private GrupoAcompanhamento grupoAcompanhamento;

    public void init() {
        listarProdutos();
        controlePedido = new ControlePedido(controleService, comanda);
        pedido = pedido == null ? controlePedido.gerarNumero() : pedido;
        status = status == null ? "" : status;
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
        produtos.clear();
        produtoServico.lsitarProdutos().forEach(p -> {
            String descricao = String.valueOf(p[1]);
            if (descricao.length() >= 25) {
                descricao = descricao.substring(0, 25);
            }
            produtos.add(new Produto(String.valueOf(p[0]), descricao, Double.parseDouble(String.valueOf(p[2]))));
        });
    }

    public void listarProdutoPorGrupo(Lapt51 lapt51) {
        produtos.clear();
        produtoServico.lsitarProdutoPorGrupo(lapt51.getT51cdgrp()).forEach(p -> {
            produtos.add(new Produto(String.valueOf(p[2]), String.valueOf(p[0]), Double.parseDouble(String.valueOf(p[1]))));
        });
        mudarQuantidade();
    }

    public void pesquisarProduto() {
        produtos.clear();
        produtoServico.listarPorReferenciaDescricaoCodigoBarra(pesquisa == null ? "" : pesquisa.toUpperCase()).forEach(p -> {
            produtos.add(new Produto(String.valueOf(p[1]), String.valueOf(p[0]), Double.parseDouble(String.valueOf(p[2]))));
        });
        mudarQuantidade();
    }

    private void mudarQuantidade() {
        if (quantidade != 1) {
            quantidade = 1;
        }
    }

    public void listarProdutosAdicionados() {
        lancamentosAdicionados.clear();
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
        });
        totalizarItensAdicionado();
    }

    private void listarItensAcompanhamento(String item, String pedido) {
        this.itensAcompanhamentos = this.itemAcompanhamentoService.pesquisarItem(item, pedido).stream().map(ItemAcompanhamento::getAcompanhamento).collect(Collectors.toList());
    }

    public void totalizarItensAdicionado() {
        this.valorTotalItens = this.lancamentosAdicionados.stream().mapToDouble(Lancamento::getPrecoTotal).sum();
    }

    public void adicionarItem(Produto p) {
        Lancamento lancamentoItem = new Lancamento();
        lancamentoItem.setComanda(this.comanda);
        lancamentoItem.setMesa(this.mesa);
        lancamentoItem.setItem(this.controleService.gerarSequencia(this.comanda));
        lancamentoItem.setNumero(gerarNumero());
        lancamentoItem.setReferencia(p.getReferencia());
        lancamentoItem.setDescricao(p.getDescricao());
        lancamentoItem.setQuantidade(this.quantidade);
        lancamentoItem.setPreco(p.getPreco());
        lancamentoItem.setVendedor(this.vendedor);
        lancamentoItem.setImprimir("0");
        lancamentoItem.setStatus(this.status);
        lancamentoItem.setPedido(this.pedido);
        this.lancamentosAdicionados.add(lancamentoItem);
        this.quantidadeItensAdicionados += 1;
        salvar(lancamentoItem);
        this.produto = p;
    }

    public void adicionarItem() {
        this.produto = this.produtoServico.buscarProduto(produto.getReferencia());
        if (this.produto != null) {
            adicionarItem(this.produto);
        } else {
            this.produto = new Produto();
            Messages.addGlobalWarn("Produto não encontrado.");
        }
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
                itemAcompanhamentoService.salvar(itemAcompanhamento);
            }
        }
        if (!lancamentoAcompanhamento.getObservacao().isEmpty()) {
            alterar(lancamentoAcompanhamento);
        }
    }

    public void salvar(Lancamento lancamento) {
        Date data = new Date();
        controleService.salvar(new Sosa98(new Sosa98Id(lancamento.getNumero(), lancamento.getItem()),
                lancamento.getComanda(), lancamento.getReferencia(),
                lancamento.getQuantidade(), data,
                lancamento.getVendedor(), lancamento.getObservacao(),
                lancamento.getMesa(),
                lancamento.getStatus(),
                lancamento.getImprimir(),
                lancamento.getPedido()
        ));
        salvarEspelho(lancamento, data);
    }

    private void alterar(Lancamento lancamento) {
        Sosa98 sosa98 = new Sosa98();
        sosa98.setTeobserv(lancamento.getObservacao());
        sosa98.setId(new Sosa98Id(lancamento.getNumero()));
        controleService.alterar(sosa98);
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
        espelhoComanda.setVendedor(lancamento.getVendedor());
        espelhoComanda.setImpressao(lancamento.getImprimir());
        espelhoComanda.setStatus(lancamento.getStatus());
        espelhoComanda.setObservacao(lancamento.getObservacao());
        espelhoComanda.setStatusItem("N");
        if (!new GerenciaArquivo().bucarInformacoes().getConfiguracao().getCobraDezPorcento().isEmpty()) {
            espelhoComanda.setPorcentagem(10d);
        }
        espelhoComandaBean.setEspelhoComanda(espelhoComanda);
        espelhoComandaBean.salvar();
    }

    public void excluirItem(Lancamento lancamento) {
        controleService.excluir(lancamento.getNumero());
        if ("0".equals(lancamento.getImprimir())) {
            espelhoComandaBean.excluir(Integer.parseInt(lancamento.getNumero()));
            itemAcompanhamentoService.excluirTodos(lancamento.getItem(),lancamento.getPedido());
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
            setUsuario("");
            setSenha("");
            if (condicao.equals("E")) {
                excluirItem(this.lancamento);
            } else if (condicao.equals("R")) {
                reipressao(this.lancamento);
            }
            fechar = true;
        } else {
            fechar = false;
            Messages.addGlobalWarn("Essa ação não pode ser executada\n informe um usuario valido ou \nusuario e senha de Gerente");
        }
        context.addCallbackParam("fechar", fechar);
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
                new ControleImpressao(caminhoDaImpressora).imprime(new PdfPedido(lancamentos, lanc, itemAcompanhamentoService).gerarPdf());
            }
            controleService.atualizarStatusImpressao(comanda);
            espelhoComandaBean.getEspelhoComandaService().atualizarStatusImpressao(comanda);
            listarProdutosAdicionados();
        } catch (IOException ex) {
            mensagem ="Impressora desligada ou cambo desconectado.";
        } catch (DocumentException | PrinterException ex) {
            mensagem ="Erro ao gerar cupom de pedido.";
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
        Map<String, List<Lancamento>> mapLanmentos = separarLancamentoPorGrupo();
        if (!mapLanmentos.isEmpty()) {
            mapLanmentos.forEach((subgrupo, lancamentos) -> imprimir(subgrupo, lancamentos, null));
            this.quantidadeItensAdicionados = 0;
            mensagem = "Pedido enviado para impressão.";
            PrimeFaces.current().executeScript("PF('dialogoImpressao').show();");
            return;
        }
        mensagem = "Itens já foram enviados para impressão.";
        PrimeFaces.current().executeScript("PF('dialogoImpressao').show();");
    }

    public void reipressao(Lancamento lancamento) {
        String subgrupo = validarAntesImprimir(lancamento.getReferencia());
        imprimir(subgrupo, null, lancamento);
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
        if (!permissao.equals("null")) {
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

    public String gerarNumero() {
        LocalDate data = LocalDate.now();
        LocalTime hora = LocalTime.now();
        String numero = String.valueOf(((data.getYear() * data.getMonthValue() * data.getDayOfMonth()) + (2217 * hora.getHour())) + (hora.getSecond() + hora.getNano()));
        return numero.length() > 6 ? numero.substring(0, 6) : numero;
    }

    public void excluirProdutoJaImpresso() {
        if (validarGerente()) {
            excluirProdutoJaImpressoSosa98();
            alterarEspelhoComandaItemExcluido();
            this.lancamento = null;
            this.lancamento = new Lancamento();
            this.espelhoComandaBean.espelhoComanda = null;
            this.usuario = "";
            this.senha = "";
            PrimeFaces.current().executeScript("PF('dialogoCancelamento').hide()");
        } else {
            Messages.addGlobalWarn("Usuário não autorizado.");
        }
    }

    private void alterarEspelhoComandaItemExcluido() {
        this.espelhoComandaBean.setEspelhoComanda(this.espelhoComandaBean.buscarPorId(Integer.parseInt(lancamento.getNumero())));
        this.espelhoComandaBean.espelhoComanda.setNumero(Integer.parseInt(lancamento.getNumero()));
        this.espelhoComandaBean.espelhoComanda.setQuantidadeCancelada(this.quantidade);
        this.espelhoComandaBean.espelhoComanda.setStatusItem("C");
        this.espelhoComandaBean.alterar();
    }

    private void excluirProdutoJaImpressoSosa98() {
        if (this.lancamento.getQuantidade() == this.quantidade) {
            this.controleService.excluir(lancamento.getNumero());
            this.lancamentosAdicionados.remove(this.lancamento);
        } else {
            this.controleService.alterarQuantidadeItem(quantidade, lancamento.getNumero());
            listarProdutosAdicionados();
        }
    }

}
