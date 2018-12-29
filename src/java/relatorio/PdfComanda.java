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
import modelo.Empresa;
import modelo.Lancamento;
import servico.PdfService;
import util.CalcularPreconta;

public class PdfComanda implements PdfService {

    private final ControlePdf controlePdf = new ControlePdf();
    private final Document documento = ControlePdf.getDocumento();
    private List<Lancamento> lancamentos;
    private Empresa empresa;
    private final DecimalFormat df = new DecimalFormat("##,##0.00");

    public PdfComanda() {
    }

    public PdfComanda(List<Lancamento> lancamentos, Empresa empresa) {
        this.lancamentos = lancamentos;
        this.empresa = empresa;
    }

    @Override
    public void criarCabecalho() throws DocumentException {

        Paragraph razao = new Paragraph(empresa.getRazao(), ControlePdf.FONT_PPB);
        Paragraph nomeFantasia = new Paragraph(empresa.getNomeFantasia(), ControlePdf.FONT_PPB);
        Paragraph endereco = new Paragraph(empresa.getEndereco() + ", " + empresa.getNumero(), ControlePdf.FONT_PP);
        Paragraph cepFone = new Paragraph("CEP: " + empresa.getCep() + " Fone: " + empresa.getTelefone(), ControlePdf.FONT_PP);
        Paragraph cnpjInsc = new Paragraph("CNPJ: " + empresa.getCnpj() + "   Insc.Est.: " + empresa.getInscricaoEstadual(), ControlePdf.FONT_PP);
        Paragraph espaco = new Paragraph(10f, " ");

        razao.setAlignment(Element.ALIGN_CENTER);
        nomeFantasia.setAlignment(Element.ALIGN_CENTER);
        endereco.setAlignment(Element.ALIGN_CENTER);
        cepFone.setAlignment(Element.ALIGN_CENTER);
        cnpjInsc.setAlignment(Element.ALIGN_CENTER);

        PdfPTable tabelaTitulo = new PdfPTable(3);
        PdfPTable tabelaValores = new PdfPTable(3);

//        ***********TITULO MESA/COMANDA*************
        PdfPCell tituloMesa = controlePdf.criarCelula("Mesa", ControlePdf.FONT_PPB, 1, Element.ALIGN_LEFT);
        PdfPCell tituloComanda = controlePdf.criarCelula("Comanda", ControlePdf.FONT_PPB, 1, Element.ALIGN_CENTER);
        PdfPCell tituloHora = controlePdf.criarCelula("Hora", ControlePdf.FONT_PPB, 1, Element.ALIGN_CENTER);

//        ***********HORA*************
        PdfPCell hora = controlePdf.criarCelula(new SimpleDateFormat("HH:mm:ss").format(new Date()), ControlePdf.FONT_P, 1, Element.ALIGN_CENTER);

//        ***********NÚMERO MESA*************
        PdfPCell numeroMesa = controlePdf.criarCelula(lancamentos.get(0).getMesa(), ControlePdf.FONT_P, 1, Element.ALIGN_LEFT);

//        ***********NÚMERO COMANDA*************
        PdfPCell numeroComanda = controlePdf.criarCelula(lancamentos.get(0).getComanda(), ControlePdf.FONT_P, 1, Element.ALIGN_CENTER);

        Paragraph divider1 = new Paragraph(-1f, "______________________________");

//        ***********POSICIONAMENTO DE CAMPOS*************
        numeroMesa.setPaddingTop(-7f);
        numeroComanda.setPaddingTop(-7f);
        hora.setPaddingTop(-3f);

        tabelaTitulo.addCell(tituloMesa);
        tabelaTitulo.addCell(tituloComanda);
        tabelaTitulo.addCell(tituloHora);
        tabelaTitulo.setWidthPercentage(100f);

        tabelaValores.addCell(numeroMesa);
        tabelaValores.addCell(numeroComanda);
        tabelaValores.addCell(hora);
        tabelaValores.setWidthPercentage(100f);

        documento.add(razao);
        documento.add(nomeFantasia);
        documento.add(endereco);
        documento.add(cepFone);
        documento.add(cnpjInsc);
        documento.add(espaco);
        documento.add(divider1);
        documento.add(tabelaTitulo);
        documento.add(tabelaValores);
        documento.add(divider1);

    }

    @Override
    public void criarCorpo() throws DocumentException {
        //        ***********TITULO ITENS*************
        PdfPTable tabelaCabecalhoItens = new PdfPTable(4);
        tabelaCabecalhoItens.setWidthPercentage(100f);
        tabelaCabecalhoItens.setWidths(new float[]{25, 120, 30, 30});

        PdfPCell tituloQtd = controlePdf.criarCelula("QTD", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        PdfPCell tituloProduto = controlePdf.criarCelula("Produto", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        PdfPCell tituloValor = controlePdf.criarCelula("Valor", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
        PdfPCell tituloSubTotal = controlePdf.criarCelula("Total", ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);

//        ***********ITENS*************
        PdfPTable tabelaItens = new PdfPTable(4);
        tabelaItens.setWidthPercentage(100f);
        tabelaItens.setWidths(new float[]{25, 120, 30, 30});

        tabelaCabecalhoItens.addCell(tituloQtd);
        tabelaCabecalhoItens.addCell(tituloProduto);
        tabelaCabecalhoItens.addCell(tituloValor);
        tabelaCabecalhoItens.addCell(tituloSubTotal);

        lancamentos.forEach((lancamento) -> {
            PdfPCell qtd = controlePdf.criarCelula(df.format(lancamento.getQuantidade()), ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
            PdfPCell produto = controlePdf.criarCelula(lancamento.getDescricao(), ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
            PdfPCell valor = controlePdf.criarCelula(df.format(lancamento.getPreco()), ControlePdf.FONT_PP, 1, Element.ALIGN_LEFT);
            PdfPCell subTotal = controlePdf.criarCelula(df.format(lancamento.getPrecoTotal()), ControlePdf.FONT_PP, 1, Element.ALIGN_RIGHT);
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
            tabelaItens.addCell(div);
        });
        documento.add(tabelaCabecalhoItens);
        documento.add(tabelaItens);

    }

    @Override
    public void criarRodape() throws DocumentException {
        PdfPTable tabelaRodape = new PdfPTable(4);
        tabelaRodape.setWidthPercentage(100f);
        tabelaRodape.setWidths(new float[]{25, 125, 25, 30});

        PdfPTable tabelaComissao = new PdfPTable(4);
        tabelaComissao.setWidthPercentage(100f);
        tabelaComissao.setWidths(new float[]{25, 125, 25, 30});

        PdfPCell div = controlePdf.criarCelula("________________________________________", ControlePdf.FONT_PPB, 4, Element.ALIGN_CENTER);

        div.setPaddingTop(-5f);
        CalcularPreconta calcularPreconta = new CalcularPreconta(lancamentos);
        double valorSubTotal = calcularPreconta.calcularItens(l -> !l.getComanda().isEmpty());
        double valorOpcional = calcularPreconta.realizarCalculoValorOpcional();
        double resultado = calcularPreconta.realizaCalculoTotal();

        if (calcularPreconta.verificarDezPorcento()) {
            PdfPCell tituloSubTotal = controlePdf.criarCelula("Sub-Total", ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);
            PdfPCell subTotal = controlePdf.criarCelula(df.format(valorSubTotal), ControlePdf.FONT_PP, 2, Element.ALIGN_CENTER);

            PdfPCell dezPorcento = controlePdf.criarCelula("10% OPCIONAL", ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);
            PdfPCell valor = controlePdf.criarCelula(df.format(valorOpcional), ControlePdf.FONT_PP, 2, Element.ALIGN_CENTER);

            tabelaComissao.addCell(tituloSubTotal);
            tabelaComissao.addCell(subTotal);
            tabelaComissao.addCell(dezPorcento);
            tabelaComissao.addCell(valor);
            tabelaComissao.addCell(div);
            documento.add(tabelaComissao);
        }

        double totalCupom = calcularPreconta.verificarDezPorcento() ? resultado : valorSubTotal;

        PdfPCell tituloTotal = controlePdf.criarCelula("Total", ControlePdf.FONT_P, 2, Element.ALIGN_RIGHT);
        PdfPCell total = controlePdf.criarCelula(df.format(totalCupom), ControlePdf.FONT_PB, 2, Element.ALIGN_CENTER);
        PdfPCell vendedor = controlePdf.criarCelula("Garçom " + lancamentos.get(0).getVendedor(), ControlePdf.FONT_PB, 4, Element.ALIGN_LEFT);

        PdfPCell tituloPedido = controlePdf.criarCelula("Pedido ", ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);
        PdfPCell pedido = controlePdf.criarCelula(this.lancamentos.get(0).getPedido(), ControlePdf.FONT_PP, 2, Element.ALIGN_RIGHT);
        
        tabelaRodape.addCell(tituloTotal);
        tabelaRodape.addCell(total);
        tabelaRodape.addCell(div);
        tabelaRodape.addCell(vendedor);
        tabelaRodape.addCell(tituloPedido);
        tabelaRodape.addCell(pedido);

        documento.add(tabelaRodape);
    }

    @Override
    public File gerarPdf() throws FileNotFoundException, DocumentException {
        File file = new File(controlePdf.buscarCaminho() + "comandaPreConta.pdf");
        PdfWriter.getInstance(documento, new FileOutputStream(file));
        documento.open();
        criarCabecalho();
        criarCorpo();
        criarRodape();
        documento.close();
        return file;
    }
}
