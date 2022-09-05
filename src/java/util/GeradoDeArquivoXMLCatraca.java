package util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class GeradoDeArquivoXMLCatraca {
    private static GeradoDeArquivoXMLCatraca geradoDeArquivoXMLCatraca = null;

    public boolean criarArquivoXML(String comanda, String status) {
        String caminho = "";
        try {
            caminho = this.buscarConfiguracao();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar informação do arquivo de configuração.");
        }
        File file = new File(caminho + "IMPORTESECULLUM.xml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                this.escreverArquivoXML(file, comanda, status);
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de configuração");
            }
        }
        return false;
    }

    private void escreverArquivoXML(File file, String comanda, String status) {
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("<?xml version=\"1.0\" standalone=\"yes\"?>");
            pw.println("<DATAPACKET Version=\"2.0\"><METADATA>");
            pw.println("<FIELDS>");
            pw.println("<FIELD attrname=\"ID_COMANDA\" fieldtype=\"string\" WIDTH=\"20\"/>");
            pw.println("<FIELD attrname=\"EVENTO\" fieldtype=\"string\" WIDTH=\"1\"/>");
            pw.println("</FIELDS>");
            pw.println("<PARAMS CHANGE_LOG=\"1 0 4\"/>");
            pw.println("</METADATA>");
            pw.println("<ROWDATA>");
            pw.println("<ROW RowState=\"4\" ID_COMANDA=\"" + comanda + "\" EVENTO=\"" + status + "\"/>");
            pw.println("</ROWDATA>");
            pw.println("</DATAPACKET>");
            pw.flush();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao escrever no arquivo xml comanda: " + comanda);
        }
    }

    private String buscarConfiguracao() throws IOException {
        return new GerenciaArquivo().bucarInformacoes().getConfiguracao().getCaminhoCatraca();
    }

    public static GeradoDeArquivoXMLCatraca initGeradoDeArquivoXMLCatraca() {
        if (geradoDeArquivoXMLCatraca == null) {
            geradoDeArquivoXMLCatraca = new GeradoDeArquivoXMLCatraca();
        }
        return geradoDeArquivoXMLCatraca;
    }
}
