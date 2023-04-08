package relatorio;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import controle.ControlePdf;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import modelo.Configuracao;
import modelo.Empresa;
import modelo.ItemAcompanhamento;
import modelo.Lancamento;
import modelo.Mesa;
import servico.ComandaService;
import servico.ItemAcompanhamentoService;
import servico.PdfService;
import util.CalcularPreconta;
import util.GerenciaArquivo;

public class PdfDetalhado implements PdfService {

    private final ControlePdf controlePdf = new ControlePdf();
    private final Document documento = ControlePdf.getDocumento();
    private Empresa empresa;
    private Map<String, List<Object[]>> mapComanda;
    private ComandaService comandaService;
    private Mesa mesa;
    private final Set<String> vendedores = new HashSet<>();
    private final DecimalFormat df = new DecimalFormat("##,##0.00");
    private final GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();
    private ItemAcompanhamentoService itemAcompanhamentoService;

    public PdfDetalhado() {
    }

    public PdfDetalhado(Empresa empresa, Map<String, List<Object[]>> mapComanda, ComandaService comandaService, Mesa mesa, ItemAcompanhamentoService itemAcompanhamentoService) {
        this.empresa = empresa;
        this.mapComanda = mapComanda;
        this.comandaService = comandaService;
        this.mesa = mesa;
        this.itemAcompanhamentoService = itemAcompanhamentoService;
    }

    @Override
    public void criarCabecalho() throws DocumentException {

        Paragraph razao = new Paragraph(empresa.getRazao(), ControlePdf.FONT_PPB);
        Paragraph nomeFantasia = new Paragraph(empresa.getNomeFantasia(), ControlePdf.FONT_PPB);
        Paragraph endereco = new Paragraph(empresa.getEndereco() + ", " + empresa.getNumero(), ControlePdf.FONT_PP);
        Paragraph cepFone = new Paragraph("CEP: " + empresa.getCep() + " Fone: " + empresa.getTelefone(), ControlePdf.FONT_PP);
        Paragraph cnpjInsc = new Paragraph("CNPJ: " + empresa.getCnpj() + " Insc.Est.: " + empresa.getInscricaoEstadual(), ControlePdf.FONT_PP);
        Paragraph tipo = new Paragraph("DETALHADA" , ControlePdf.FONT_MB);
        Paragraph espaco = new Paragraph(10f, " ");

        razao.setAlignment(Element.ALIGN_CENTER);
        nomeFantasia.setAlignment(Element.ALIGN_CENTER);
        endereco.setAlignment(Element.ALIGN_CENTER);
        cepFone.setAlignment(Element.ALIGN_CENTER);
        cnpjInsc.setAlignment(Element.ALIGN_CENTER);
        tipo.setAlignment(Element.ALIGN_CENTER);

        Paragraph divider1 = new Paragraph(-1f, "______________________________");

        documento.add(razao);
        documento.add(nomeFantasia);
        documento.add(endereco);
        documento.add(cepFone);
        documento.add(cnpjInsc);
        documento.add(espaco);
        documento.add(tipo);
        documento.add(divider1);

    }
    private double totalzilaCupom = 0;

    @Override
    public void criarCorpo() throws DocumentException {
        List<String> numerosComandas = mapComanda.keySet().stream().map(String::valueOf).collect(Collectors.toList());
        Collections.sort(numerosComandas);
        for (String numeroComanda : numerosComandas) {
            List<Lancamento> lancamentos = new ArrayList<>();
            Configuracao configuracao = this.gerenciaArquivo.bucarInformacoes().getConfiguracao();
            CalcularPreconta calcularPreconta = new CalcularPreconta(lancamentos,configuracao);
            Paragraph divider1 = new Paragraph(-1f, "______________________________");
            Paragraph espaco = new Paragraph(10f, " ");

            PdfPTable tabelaMesaComanda = new PdfPTable(2);
            tabelaMesaComanda.setWidthPercentage(100f);

            PdfPTable tabelaValorMesaComanda = new PdfPTable(2);
            tabelaValorMesaComanda.setWidthPercentage(100f);

            PdfPTable tabelaItens = new PdfPTable(4);
            tabelaItens.setWidthPercentage(100f);
            tabelaItens.setWidths(new float[]{25, 125, 25, 30});

            PdfPTable tabelaComissao = new PdfPTable(4);
            tabelaComissao.setWidthPercentage(100f);
            tabelaComissao.setWidths(new float[]{25, 125, 25, 30});

//        ***********TITULO MESA/COMANDA*************
            PdfPCell tituloMesa = controlePdf.criarCelula("Mesa", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
            PdfPCell tituloComanda = controlePdf.criarCelula("Comanda", ControlePdf.FONT_PP, 1, Element.ALIGN_CENTER);

//        ***********VALOR MESA/COMANDA*************
            PdfPCell mesa = controlePdf.criarCelula(this.mesa.getMESA(), ControlePdf.FONT_PPB, 1, Element.ALIGN_LEFT);
            PdfPCell comanda = controlePdf.criarCelula(numeroComanda, ControlePdf.FONT_PPB, 1, Element.ALIGN_CENTER);

//        ***********TITULO ITENS*************
            PdfPTable tabelaCabecalhoItens = new PdfPTable(4);
            tabelaCabecalhoItens.setWidthPercentage(100f);
            tabelaCabecalhoItens.setWidths(new float[]{25, 125, 25, 30});

            PdfPCell tituloQtd = controlePdf.criarCelula("QTD", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
            PdfPCell tituloProduto = controlePdf.criarCelula("Produto", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
            PdfPCell tituloValor = controlePdf.criarCelula("Valor", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
            PdfPCell tituloSubTotal = controlePdf.criarCelula("Total", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);

            tabelaMesaComanda.addCell(tituloMesa);
            tabelaMesaComanda.addCell(tituloComanda);
            tabelaCabecalhoItens.addCell(tituloQtd);
            tabelaCabecalhoItens.addCell(tituloProduto);
            tabelaCabecalhoItens.addCell(tituloValor);
            tabelaCabecalhoItens.addCell(tituloSubTotal);

            mesa.setPaddingTop(-5f);
            comanda.setPaddingTop(-5f);

            tabelaValorMesaComanda.addCell(mesa);
            tabelaValorMesaComanda.addCell(comanda);

//        ***********ITENS*************
            double totalComanda = 0.0;
            List<Object[]> comandas = comandaService.listarComandas(numeroComanda);
            for (Object[] c : comandas) {
                String descricao = String.valueOf(c[1]);
                String quantidade = df.format(Double.parseDouble(String.valueOf(c[2])));
                String valorUnitario = df.format(Double.parseDouble(String.valueOf(c[3])));
                String hora = "";
                try {
                    hora = new SimpleDateFormat("HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(c[9])));
                } catch (ParseException ex) {
                    Logger.getLogger(PdfDetalhado.class.getName()).log(Level.SEVERE, null, ex);
                }
                String garcom = String.valueOf(c[6]);
                double valorTotal = Double.parseDouble(String.valueOf(c[4]));

                PdfPCell qtd = controlePdf.criarCelula(quantidade, ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
                PdfPCell produto = controlePdf.criarCelula(descricao, ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
                PdfPCell valor = controlePdf.criarCelula(valorUnitario, ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
                PdfPCell subTotal = controlePdf.criarCelula(df.format(valorTotal), ControlePdf.FONT_PP, 1, Element.ALIGN_CENTER);
                PdfPCell horaItem = controlePdf.criarCelula(hora, ControlePdf.FONT_PPP, 2, Element.ALIGN_LEFT);
                PdfPCell garcomItem = controlePdf.criarCelula(garcom, ControlePdf.FONT_PPP, 2, Element.ALIGN_RIGHT);
                PdfPCell div = controlePdf.criarCelula("...............................................................................", ControlePdf.FONT_PP, 4, Element.ALIGN_CENTER);

                qtd.setPaddingTop(-7f);
                produto.setPaddingTop(-5f);
                valor.setPaddingTop(-5f);
                subTotal.setPaddingTop(-5f);
                div.setPaddingTop(-5f);

                tabelaItens.addCell(qtd);
                tabelaItens.addCell(produto);
                tabelaItens.addCell(valor);
                tabelaItens.addCell(subTotal);

                List<ItemAcompanhamento> acompanhamentos = listarAcompanhamentos(String.valueOf(c[7]), String.valueOf(c[8]));
                for (ItemAcompanhamento acompanhamento : acompanhamentos) {
                    PdfPCell itemAcompanhamento = controlePdf.criarCelula(acompanhamento.getAcompanhamento(), ControlePdf.FONT_PPP, 4, Element.ALIGN_CENTER);
                    itemAcompanhamento.setPaddingTop(-5f);
                    tabelaItens.addCell(itemAcompanhamento);
                }

                tabelaItens.addCell(horaItem);
                tabelaItens.addCell(garcomItem);

                tabelaItens.addCell(div);

                totalComanda += valorTotal;
                vendedores.add(String.valueOf(c[6]));
                lancamentos.add(new Lancamento(descricao, valorTotal));
            }
            documento.add(divider1);
            documento.add(espaco);
            documento.add(tabelaMesaComanda);
            documento.add(tabelaValorMesaComanda);
            documento.add(divider1);
            documento.add(tabelaCabecalhoItens);
            documento.add(tabelaItens);

            double valorSubTotal = calcularPreconta.calcularItens(l -> !l.getDescricao().isEmpty());
            double valorOpcional = calcularPreconta.realizarCalculoValorOpcional();
            double resultado = calcularPreconta.realizaCalculoTotal();
            if (calcularPreconta.verificarDezPorcento()) {
                PdfPCell labelsubTotal = controlePdf.criarCelula("Sub-Total", ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);
                PdfPCell subTotal = controlePdf.criarCelula(df.format(valorSubTotal), ControlePdf.FONT_PP, 2, Element.ALIGN_CENTER);

                PdfPCell dezPorcento = controlePdf.criarCelula(configuracao.getMensagemDezPorcento(), ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);
                PdfPCell valor = controlePdf.criarCelula(df.format(valorOpcional), ControlePdf.FONT_PP, 2, Element.ALIGN_CENTER);

                tabelaComissao.addCell(labelsubTotal);
                tabelaComissao.addCell(subTotal);
                tabelaComissao.addCell(dezPorcento);
                tabelaComissao.addCell(valor);
            }
            PdfPCell tituloTotal = controlePdf.criarCelula("Total: ", ControlePdf.FONT_PPB, 2, Element.ALIGN_RIGHT);
            PdfPCell total = controlePdf.criarCelula(df.format(calcularPreconta.verificarDezPorcento() ? resultado : valorSubTotal), ControlePdf.FONT_PPB, 2, Element.ALIGN_CENTER);
            tabelaComissao.addCell(tituloTotal);
            tabelaComissao.addCell(total);
            documento.add(tabelaComissao);
            totalzilaCupom += calcularPreconta.verificarDezPorcento() ? resultado : valorSubTotal;
        }
    }

    private List<ItemAcompanhamento> listarAcompanhamentos(String item, String pedido) {
        return itemAcompanhamentoService.pesquisarItem(item, pedido);
    }

    @Override
    public void criarRodape() throws DocumentException {
        PdfPTable tabelaRodape = new PdfPTable(4);
        tabelaRodape.setWidthPercentage(100f);
        tabelaRodape.setWidths(new float[]{25, 125, 25, 30});

        String quantidadePessoasPagantes = this.mesa.getPAGANTES();
        String garcons = vendedores.size() > 1 ? "Garçons " : "Garçom ";

        PdfPCell tituloPessaosPagantes = controlePdf.criarCelula("Pagantes: " + quantidadePessoasPagantes, ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);
        PdfPCell totalPessaosPagantes = controlePdf.criarCelula(df.format(totalzilaCupom / Integer.parseInt(quantidadePessoasPagantes)), ControlePdf.FONT_PP, 2, Element.ALIGN_CENTER);

        PdfPCell tituloTotal = controlePdf.criarCelula("Total", ControlePdf.FONT_P, 2, Element.ALIGN_RIGHT);
        PdfPCell total = controlePdf.criarCelula(df.format(totalzilaCupom), ControlePdf.FONT_PB, 2, Element.ALIGN_CENTER);

        PdfPCell vendedor = controlePdf.criarCelula(garcons + vendedores.stream().collect(Collectors.joining(",")), ControlePdf.FONT_PP, 4, Element.ALIGN_LEFT);
        PdfPCell hora = controlePdf.criarCelula(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), ControlePdf.FONT_PP, 4, Element.ALIGN_LEFT);

        PdfPCell tituloPedido = controlePdf.criarCelula("Pedido ", ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);
        String pedidos = Arrays.asList(this.mesa.getPEDIDO().split(",")).stream().collect(Collectors.toSet()).stream().collect(Collectors.joining(","));
        PdfPCell pedido = controlePdf.criarCelula(pedidos, ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);

        PdfPCell div = controlePdf.criarCelula("________________________________________", ControlePdf.FONT_PPB, 4, Element.ALIGN_CENTER);

        div.setPaddingTop(-5f);

        tabelaRodape.addCell(div);
        if (!quantidadePessoasPagantes.equals("1")) {
            tabelaRodape.addCell(tituloPessaosPagantes);
            tabelaRodape.addCell(totalPessaosPagantes);
        }
        tabelaRodape.addCell(tituloTotal);
        tabelaRodape.addCell(total);
        tabelaRodape.addCell(hora);
        tabelaRodape.addCell(vendedor);
        tabelaRodape.addCell(tituloPedido);
        tabelaRodape.addCell(pedido);
        tabelaRodape.addCell(div);

        documento.add(tabelaRodape);

    }

    @Override
    public File gerarPdf() throws FileNotFoundException, DocumentException {
        File file = new File(controlePdf.buscarCaminho() + "detalhado.pdf");
        PdfWriter.getInstance(documento, new FileOutputStream(file));
        documento.open();
        criarCabecalho();
        totalzilaCupom = 0;
        criarCorpo();
        criarRodape();
        documento.close();
        return file;
    }
}
