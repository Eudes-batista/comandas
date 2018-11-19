package controle;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintServiceAttributeSet;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

public class ControleImpressao {

    private final String impressora;

    public ControleImpressao(String impressora) {
        this.impressora = impressora;
    }

    private PrintService detectaImpressoras() {
        PrintService service = null;
        try {
            DocFlavor docFlavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintService[] ps = PrintServiceLookup.lookupPrintServices(docFlavor, new HashPrintServiceAttributeSet());
            for (PrintService printService : ps) {
                if (printService.getName().toUpperCase().contains(impressora.toUpperCase())) {
                    service = printService;
                }
            }
            return service;
        } catch (Exception e) {
            return service;
        }
    }

    public boolean imprime(File file) throws IOException, PrinterException {
        PDDocument doc = PDDocument.load(file);
        PrintService servico = detectaImpressoras();
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(doc));
        job.setPrintService(servico);
        job.setJobName(file.getName());
        job.print();
        return false;
    }

}
