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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import modelo.ItemAcompanhamento;
import modelo.Lancamento;
import modelo.dto.ItemCanceladoGarcom;
import servico.ItemAcompanhamentoService;
import servico.PdfService;

/**
 *
 * @author administrador
 */
public class PdfCancelamento implements PdfService {

    private final Document documento = ControlePdf.getDocumento();
    private final ControlePdf controlePdf = new ControlePdf();
    private Lancamento lancamento = null;
    private ItemAcompanhamentoService itemAcompanhamentoService;
    private ItemCanceladoGarcom itemCanceladoGarcom;
    private final String separador = "..........................................................";

    public PdfCancelamento(Lancamento lancamento, ItemCanceladoGarcom itemCanceladoGarcom, ItemAcompanhamentoService itemAcompanhamentoService) {
        this.lancamento = lancamento;
        this.itemCanceladoGarcom = itemCanceladoGarcom;
        this.itemAcompanhamentoService = itemAcompanhamentoService;
    }

    public PdfCancelamento() {
    }

    @Override
    public File gerarPdf() throws FileNotFoundException, DocumentException {
        String codigoPedido = lancamento.getPedido();
        File file = new File(controlePdf.buscarCaminho() + "cancelamentoItem" + codigoPedido + ".pdf");
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

        PdfPTable tabelaTitulo = new PdfPTable(3);
        PdfPTable tabelaValores = new PdfPTable(3);

//        ***********TITULO MESA/COMANDA*************      
        PdfPCell titulo = controlePdf.criarCelula("CANCELAMENTO", ControlePdf.FONT_MB, 3, Element.ALIGN_CENTER);
        
        PdfPCell tituloMesa = controlePdf.criarCelula("Mesa", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        PdfPCell tituloComanda = controlePdf.criarCelula("Comanda", ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);

//        ***********HORA*************
        PdfPCell hora = controlePdf.criarCelula(new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()), ControlePdf.FONT_PPB, 3, Element.ALIGN_CENTER);

//        ***********NÚMERO MESA*************
        PdfPCell numeroMesa = controlePdf.criarCelula(lancamento.getMesa(), ControlePdf.FONT_P, 1, Element.ALIGN_LEFT);

//        ***********NÚMERO COMANDA************
        PdfPCell numeroComanda = controlePdf.criarCelula(lancamento.getComanda(), ControlePdf.FONT_P, 2, Element.ALIGN_RIGHT);

        Paragraph divider1 = new Paragraph(-1f, "______________________________");
        PdfPCell divisor = controlePdf.criarCelula("______________________________________", ControlePdf.FONT_PPB, 3, Element.ALIGN_LEFT);

//        ***********POSICIONAMENTO DE CAMPOS*************
        numeroMesa.setPaddingTop(-7f);
        numeroComanda.setPaddingTop(-7f);
        hora.setPaddingTop(-3f);
        divisor.setPaddingTop(-5f);

        tabelaTitulo.addCell(titulo);
        tabelaTitulo.addCell(hora);
        
        tabelaTitulo.addCell(divisor);
        tabelaTitulo.addCell(tituloMesa);
        tabelaTitulo.addCell(tituloComanda);
        tabelaTitulo.setWidthPercentage(100f);

        tabelaValores.addCell(numeroMesa);
        tabelaValores.addCell(numeroComanda);
        tabelaValores.setWidthPercentage(100f);

        documento.add(tabelaTitulo);
        documento.add(tabelaValores);
        documento.add(divider1);

    }

    @Override
    public void criarCorpo() throws DocumentException {

        //        ***********TITULO ITENS*************
        PdfPTable tabelaCabecalhoItens = new PdfPTable(3);
        tabelaCabecalhoItens.setWidthPercentage(100f);
        tabelaCabecalhoItens.setWidths(new float[]{25, 100,50});

        PdfPCell tituloQtd = controlePdf.criarCelula("QTD", ControlePdf.FONT_PPP, 1, Element.ALIGN_LEFT);
        PdfPCell tituloProduto = controlePdf.criarCelula("Produto", ControlePdf.FONT_PPP, 1, Element.ALIGN_LEFT);
        PdfPCell tituloValor = controlePdf.criarCelula("Valor", ControlePdf.FONT_PPP, 1, Element.ALIGN_RIGHT);
        tituloQtd.setPaddingTop(-3f);
        tituloProduto.setPaddingTop(-3f);
        tituloValor.setPaddingTop(-3f);

        tabelaCabecalhoItens.addCell(tituloQtd);
        tabelaCabecalhoItens.addCell(tituloProduto);
        tabelaCabecalhoItens.addCell(tituloValor);
        documento.add(tabelaCabecalhoItens);

//        ***********ITENS*************
        carregarItens(lancamento);
    }

    private void carregarItens(Lancamento lanc) throws DocumentException {
        PdfPTable tabelaItens = new PdfPTable(3);
        tabelaItens.setWidthPercentage(100f);
        tabelaItens.setWidths(new float[]{50, 150,50});
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
        PdfPTable tabelaCancelamento = new PdfPTable(5);
        tabelaCancelamento.setWidthPercentage(100f);
        
        PdfPCell qtd = controlePdf.criarCelula(String.valueOf(lanc.getQuantidade()), ControlePdf.FONT_PPP, 1, Element.ALIGN_LEFT);
        PdfPCell produto = controlePdf.criarCelula(lanc.getDescricao(), ControlePdf.FONT_PPP, 1, Element.ALIGN_LEFT);
        PdfPCell valor = controlePdf.criarCelula(decimalFormat.format(lanc.getPrecoTotal()), ControlePdf.FONT_PPP, 1, Element.ALIGN_RIGHT);
        PdfPCell obs = controlePdf.criarCelula("OBS: " + lanc.getObservacao(), ControlePdf.FONT_PPP, 3, Element.ALIGN_LEFT);
        
        PdfPCell motivo = controlePdf.criarCelula("Motivo: " + itemCanceladoGarcom.getMOTIVO(), ControlePdf.FONT_PP, 5, Element.ALIGN_LEFT);
        PdfPCell observacao = controlePdf.criarCelula("OBS: " + itemCanceladoGarcom.getOBSERVACAO(), ControlePdf.FONT_PP, 5, Element.ALIGN_LEFT);
        PdfPCell cancelamento = controlePdf.criarCelula("QTD.CANCELADA: " + itemCanceladoGarcom.getCANCELAMENTO(), ControlePdf.FONT_PP,5, Element.ALIGN_LEFT);
        PdfPCell responsavel = controlePdf.criarCelula("RESPONSAVEL: " + itemCanceladoGarcom.getRESPONSAVEL(), ControlePdf.FONT_PP,5, Element.ALIGN_LEFT);
        PdfPCell produzido = controlePdf.criarCelula("Foi Produzido: " + itemCanceladoGarcom.getPRODUZIDO(), ControlePdf.FONT_PP, 5, Element.ALIGN_LEFT);
        
        
        PdfPCell div = controlePdf.criarCelula(separador, ControlePdf.FONT_PP, 2, Element.ALIGN_CENTER);

        qtd.setPaddingTop(-7f);
        produto.setPaddingTop(-5f);
        obs.setPaddingTop(-5f);
        valor.setPaddingTop(-5f);
        div.setPaddingTop(-5f);

        tabelaItens.addCell(qtd);
        tabelaItens.addCell(produto);
        tabelaItens.addCell(valor);

        List<ItemAcompanhamento> acompanhamentos = listarAcompanhamentos(lanc);
        for (ItemAcompanhamento acompanhamento : acompanhamentos) {
            PdfPCell itemAcompanhamento = controlePdf.criarCelula(acompanhamento.getAcompanhamento(), ControlePdf.FONT_PP, 2, Element.ALIGN_CENTER);
            itemAcompanhamento.setPaddingTop(-5f);
            tabelaItens.addCell(itemAcompanhamento);
        }
        tabelaItens.addCell(obs);
        
        tabelaCancelamento.addCell(motivo);
        tabelaCancelamento.addCell(observacao);
        tabelaCancelamento.addCell(cancelamento);
        tabelaCancelamento.addCell(responsavel);
        tabelaCancelamento.addCell(produzido);
        
        tabelaCancelamento.addCell(div);

        documento.add(tabelaItens);
        documento.add(tabelaCancelamento);
    }

    private List<ItemAcompanhamento> listarAcompanhamentos(Lancamento lancamento) {
        return itemAcompanhamentoService.pesquisarItem(lancamento.getItem(), lancamento.getPedido());
    }

    @Override
    public void criarRodape() throws DocumentException {

        PdfPTable tabelaRodape = new PdfPTable(4);
        tabelaRodape.setWidthPercentage(100f);

        PdfPCell tituloGarcom = controlePdf.criarCelula("Garçom", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        PdfPCell garcom = controlePdf.criarCelula(lancamento.getVendedor(), ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        PdfPCell titulopedido = controlePdf.criarCelula("Pedido ", ControlePdf.FONT_PP, 1, Element.ALIGN_RIGHT);
        PdfPCell pedido = controlePdf.criarCelula(lancamento.getPedido(), ControlePdf.FONT_PP, 1, Element.ALIGN_RIGHT);
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
