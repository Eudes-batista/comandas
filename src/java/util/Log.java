package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.context.FacesContext;
import org.omnifaces.util.Messages;
import servico.MesaService;

public class Log implements Serializable {

    private  final MesaService mesaService;
    Path path = Paths.get(getCaimnho());

    public Log(MesaService mesaService) {
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException ex) {
                Messages.addGlobalError("Erro ao criar arquivo de log.txt");
            }
        }
        this.mesaService = mesaService;
    }

    public void registrarExclusao(String mesa, String usuario) {
        mesaService.listarLancamentos(mesa).forEach(m -> {
            try(PrintWriter pw = new PrintWriter(new FileWriter(path.toFile(), true))){
                pw.println("usuario data-hora "+usuario.toUpperCase() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                pw.println("mesa    "+ m[10]);
                pw.println("comanda "+m[2]);
                pw.println("referencia "+m[3]);
                pw.println("descricao "+m[4]);
                pw.println("quantidade "+m[5]);
                pw.println("valor_unitario "+ m[6]);
                pw.println("valor_total "+m[7]);
                pw.println("observacao "+m[9]);
                pw.println("vendedor "+m[8]);
                pw.println("_________________________");
                pw.flush();
            } catch (IOException ex) {
                Messages.addGlobalError("Erro ao gravar informação no arquivo de  log.txt");
            }
        });
    }
    
    public StringBuilder recuperarLog() {
    StringBuilder str = new StringBuilder();
        try {
            Files.lines(path).forEach(l -> str.append(l).append("\n"));
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao ler informação no arquivo de  log.txt");
        }
        return str;
    }
    

    private String getCaimnho() {
        String nomeArquivo = "log-"+new SimpleDateFormat("dd-MM-yyyy").format(new Date())+".txt";
        String url = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/"+nomeArquivo);
        return url;
    }
}
