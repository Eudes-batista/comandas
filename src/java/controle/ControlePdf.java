package controle;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import javax.faces.context.FacesContext;

public class ControlePdf {

    public static final Font FONT_G = new Font(Font.getFamily("arial"), 26, Font.BOLD);
    public static final Font FONT_M = new Font(Font.getFamily("arial"), 16, Font.NORMAL);
    public static final Font FONT_MB = new Font(Font.getFamily("arial"), 16, Font.BOLD);
    public static final Font FONT_P = new Font(Font.getFamily("arial"), 11, Font.NORMAL);
    public static final Font FONT_PB = new Font(Font.getFamily("arial"), 11, Font.BOLD);
    public static final Font FONT_PP = new Font(Font.getFamily("arial"), 9, Font.NORMAL);
    public static final Font FONT_PPB = new Font(Font.getFamily("arial"), 9, Font.BOLD);
    public static final Font FONT_PPP = new Font(Font.getFamily("arial"), 9, Font.NORMAL);

    private static Document documento = null;

    public PdfPCell criarCelula(String texto, Font fonte, int colunas, int alinhamento) {
        PdfPCell cell = new PdfPCell();
        Paragraph paragrafoCell = new Paragraph(texto, fonte);
        paragrafoCell.setAlignment(alinhamento);
        cell.addElement(paragrafoCell);
        cell.setBorder(0);
        cell.setColspan(colunas);
        return cell;
    }

    public static Document getDocumento() {
        documento = new Document(new Rectangle(227, 842), 1f, 20f, 1f, 1f);
        return documento;
    }

    public String buscarCaminho() {
        return FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/relatorio/");
    }
    
}
