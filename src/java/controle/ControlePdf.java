package controle;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import javax.faces.context.FacesContext;

public class ControlePdf {

    public static final Font FONT_G = new Font(Font.getFamily("tahoma"), 25, Font.BOLD);
    public static final Font FONT_M = new Font(Font.getFamily("tahoma"), 15, Font.NORMAL);
    public static final Font FONT_MB = new Font(Font.getFamily("tahoma"), 15, Font.BOLD);
    public static final Font FONT_P = new Font(Font.getFamily("tahoma"), 10, Font.NORMAL);
    public static final Font FONT_PB = new Font(Font.getFamily("tahoma"), 10, Font.BOLD);
    public static final Font FONT_PP = new Font(Font.getFamily("tahoma"), 8, Font.NORMAL);
    public static final Font FONT_PPB = new Font(Font.getFamily("tahoma"), 8, Font.BOLD);

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
