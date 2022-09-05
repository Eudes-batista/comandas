/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import org.omnifaces.util.Messages;

/**
 *
 * @author Administrador
 */
public class ControleRelatorio {

    public static boolean imprimir(String caminhoDaImpressora,StringBuilder str) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(caminhoDaImpressora))) {
            try (PrintStream printStream = new PrintStream(fileOutputStream)) {
                printStream.print(str);
                printStream.println();
                printStream.flush();
                return true;
            }
        } catch (Exception ex) {
            Messages.addGlobalError("Impressora desligada ou cambo desconectado.\n" + caminhoDaImpressora);
        }
        return false;
    }

}
