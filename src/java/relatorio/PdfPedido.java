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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import modelo.ItemAcompanhamento;
import modelo.Lancamento;
import servico.ItemAcompanhamentoService;
import servico.PdfService;

/**
 *
 * @author administrador
 */
public class PdfPedido implements PdfService {

    private final Document documento = ControlePdf.getDocumento();
    private final ControlePdf controlePdf = new ControlePdf();
    private List<Lancamento> lancamentos = null;
    private Lancamento lancamento = null;
    private ItemAcompanhamentoService itemAcompanhamentoService;
    private final String separador = "..........................................................";

    public PdfPedido(List<Lancamento> lancamentos, Lancamento lancamento, ItemAcompanhamentoService itemAcompanhamentoService) {
        this.lancamentos = lancamentos;
        this.lancamento = lancamento;
        this.itemAcompanhamentoService = itemAcompanhamentoService;
    }

    public PdfPedido(List<Lancamento> lancamentos, Lancamento lancamento) {
        this.lancamentos = lancamentos;
        this.lancamento = lancamento;
    }

    public PdfPedido(Lancamento lancamento) {
        this.lancamento = lancamento;
    }

    public PdfPedido() {
    }

    @Override
    public File gerarPdf() throws FileNotFoundException, DocumentException {
        String codigoPedido = lancamentos != null ? lancamentos.get(0).getPedido() : lancamento.getPedido();
        File file = new File(controlePdf.buscarCaminho() + "pedido" + codigoPedido + ".pdf");
        PdfWriter.getInstance(documento, new FileOutputStream(file));
        documento.open();
        criarCabecalho();
        criarCorpo();
        criarRodape();
        documento.close();
        return file;
    }

    @Override
    public void criarCabecalho() throws DocumentException {

        PdfPTable tabelaReimpressao = new PdfPTable(1);
        PdfPTable tabelaTitulo = new PdfPTable(3);
        PdfPTable tabelaValores = new PdfPTable(3);

//        ***********TITULO MESA/COMANDA*************
        PdfPCell reimpressao = controlePdf.criarCelula("***** REIMPRESSÃO *****", ControlePdf.FONT_MB, 1, Element.ALIGN_CENTER);
        PdfPCell tituloMesa = controlePdf.criarCelula("Mesa", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        PdfPCell tituloComanda = controlePdf.criarCelula("Comanda", ControlePdf.FONT_PP, 1, Element.ALIGN_CENTER);
        PdfPCell tituloHora = controlePdf.criarCelula("Hora", ControlePdf.FONT_PP, 1, Element.ALIGN_CENTER);

//        ***********HORA*************
        PdfPCell hora = controlePdf.criarCelula(new SimpleDateFormat("HH:mm:ss").format(new Date()), ControlePdf.FONT_PB, 1, Element.ALIGN_CENTER);

//        ***********NÚMERO MESA*************
        PdfPCell numeroMesa = controlePdf.criarCelula(lancamentos != null ? lancamentos.get(0).getMesa() : lancamento.getMesa(), ControlePdf.FONT_MB, 1, Element.ALIGN_LEFT);

//        ***********NÚMERO COMANDA************
        PdfPCell numeroComanda = controlePdf.criarCelula(lancamentos != null ? lancamentos.get(0).getComanda() : lancamento.getComanda(), ControlePdf.FONT_MB, 1, Element.ALIGN_CENTER);

        Paragraph divider1 = new Paragraph(-1f, "______________________________");

//        ***********POSICIONAMENTO DE CAMPOS*************
        numeroMesa.setPaddingTop(-7f);
        numeroComanda.setPaddingTop(-7f);
        hora.setPaddingTop(-3f);

        if (lancamento != null) {
            tabelaReimpressao.addCell(reimpressao);

        }

        tabelaTitulo.addCell(tituloMesa);
        tabelaTitulo.addCell(tituloComanda);
        tabelaTitulo.addCell(tituloHora);
        tabelaTitulo.setWidthPercentage(100f);

        tabelaValores.addCell(numeroMesa);
        tabelaValores.addCell(numeroComanda);
        tabelaValores.addCell(hora);
        tabelaValores.setWidthPercentage(100f);

        documento.add(tabelaReimpressao);
        documento.add(tabelaTitulo);
        documento.add(tabelaValores);
        documento.add(divider1);

    }

    @Override
    public void criarCorpo() throws DocumentException {

        //        ***********TITULO ITENS*************
        PdfPTable tabelaCabecalhoItens = new PdfPTable(2);
        tabelaCabecalhoItens.setWidthPercentage(100f);
        tabelaCabecalhoItens.setWidths(new float[]{25, 100});

        PdfPCell tituloQtd = controlePdf.criarCelula("QTD", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        PdfPCell tituloProduto = controlePdf.criarCelula("Produto", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        tituloQtd.setPaddingTop(-3f);
        tituloProduto.setPaddingTop(-3f);

        tabelaCabecalhoItens.addCell(tituloQtd);
        tabelaCabecalhoItens.addCell(tituloProduto);
        documento.add(tabelaCabecalhoItens);

//        ***********ITENS*************
        String descricaoItem;
        if (lancamentos != null) {
            for (Lancamento lanc : lancamentos) {
                descricaoItem = lanc.getDescricao().toUpperCase();
                if (verificarNomeCouvertNaDescricao(descricaoItem)) {
                    carregarItens(lanc);
                }
            }
            return;
        }
        descricaoItem = lancamento.getDescricao().toUpperCase();
        if (verificarNomeCouvertNaDescricao(descricaoItem)) {
            carregarItens(lancamento);
        }
    }

    private boolean verificarNomeCouvertNaDescricao(String descricao) {
        return !descricao.contains("couvert".toUpperCase());
    }

    private void carregarItens(Lancamento lanc) throws DocumentException {
        PdfPTable tabelaItens = new PdfPTable(2);
        tabelaItens.setWidthPercentage(100f);
        tabelaItens.setWidths(new float[]{50, 200});

        PdfPCell qtd = controlePdf.criarCelula(String.valueOf(lanc.getQuantidade()), ControlePdf.FONT_M, 1, Element.ALIGN_LEFT);
        PdfPCell produto = controlePdf.criarCelula(lanc.getDescricao(), ControlePdf.FONT_PB, 1, Element.ALIGN_LEFT);
        PdfPCell obs = controlePdf.criarCelula("OBS: " + lanc.getObservacao(), ControlePdf.FONT_P, 2, Element.ALIGN_LEFT);
        PdfPCell div = controlePdf.criarCelula(separador, ControlePdf.FONT_PP, 2, Element.ALIGN_CENTER);

        qtd.setPaddingTop(-7f);
        produto.setPaddingTop(-5f);
        obs.setPaddingTop(-5f);
        div.setPaddingTop(-5f);

        tabelaItens.addCell(qtd);
        tabelaItens.addCell(produto);

        List<ItemAcompanhamento> acompanhamentos = listarAcompanhamentos(lanc);
        for (ItemAcompanhamento acompanhamento : acompanhamentos) {
            PdfPCell itemAcompanhamento = controlePdf.criarCelula(acompanhamento.getAcompanhamento(), ControlePdf.FONT_PP, 2, Element.ALIGN_CENTER);
            itemAcompanhamento.setPaddingTop(-5f);
            tabelaItens.addCell(itemAcompanhamento);
        }

        tabelaItens.addCell(obs);
        tabelaItens.addCell(div);

        documento.add(tabelaItens);
    }

    private List<ItemAcompanhamento> listarAcompanhamentos(Lancamento lancamento) {
        return itemAcompanhamentoService.pesquisarItem(lancamento.getItem(), lancamento.getPedido());
    }

    @Override
    public void criarRodape() throws DocumentException {

        PdfPTable tabelaRodape = new PdfPTable(4);
        tabelaRodape.setWidthPercentage(100f);

        PdfPCell tituloGarcom = controlePdf.criarCelula("Garçom", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        PdfPCell garcom = controlePdf.criarCelula(lancamentos != null ? lancamentos.get(0).getVendedor() : lancamento.getVendedor(), ControlePdf.FONT_PPB, 1, Element.ALIGN_LEFT);
        PdfPCell titulopedido = controlePdf.criarCelula("Pedido ", ControlePdf.FONT_PP, 1, Element.ALIGN_RIGHT);
        PdfPCell pedido = controlePdf.criarCelula(lancamentos != null ? lancamentos.get(0).getPedido() : lancamento.getPedido(), ControlePdf.FONT_PP, 1, Element.ALIGN_RIGHT);
        PdfPCell div = controlePdf.criarCelula(separador, ControlePdf.FONT_PPB, 2, Element.ALIGN_CENTER);

        div.setPaddingTop(-5f);

        tabelaRodape.addCell(tituloGarcom);
        tabelaRodape.addCell(garcom);
        tabelaRodape.addCell(titulopedido);
        tabelaRodape.addCell(pedido);
        tabelaRodape.addCell(div);

        documento.add(tabelaRodape);

    }

}
