package controle.teste;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintServiceAttributeSet;

public class Test {

    public static void main(String[] args) {
       
        try {
            DocFlavor docFlavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintService[] ps = PrintServiceLookup.lookupPrintServices(docFlavor, new HashPrintServiceAttributeSet());
            for (PrintService printService : ps) {
                System.out.println(printService.getName());
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        
    }

}
