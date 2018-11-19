package servico;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileNotFoundException;

public interface PdfService {

    public void criarCabecalho() throws DocumentException;

    public void criarCorpo() throws DocumentException;

    public void criarRodape() throws DocumentException;

    public File gerarPdf() throws FileNotFoundException, DocumentException;
}
