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
import modelo.Lancamento;
import org.omnifaces.util.Messages;
import servico.MesaService;

public class Log implements Serializable {

    private final MesaService mesaService;
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

    public Log() {
        this.mesaService = null;
    }

    public void registrarExclusao(String mesa, String usuario) {
        mesaService.listarLancamentos(mesa).forEach(m -> {
            try (PrintWriter pw = new PrintWriter(new FileWriter(path.toFile(), true))) {
                pw.println("usuario data-hora " + usuario.toUpperCase() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                pw.println("mesa    " + m[10]);
                pw.println("comanda " + m[2]);
                pw.println("referencia " + m[3]);
                pw.println("descricao " + m[4]);
                pw.println("quantidade " + m[5]);
                pw.println("valor_unitario " + m[6]);
                pw.println("valor_total " + m[7]);
                pw.println("observacao " + m[9]);
                pw.println("vendedor " + m[8]);
                pw.println("_________________________");
                pw.flush();
            } catch (IOException ex) {
                Messages.addGlobalError("Erro ao gravar informação no arquivo de  log.txt");
            }
        });
    }

    public void salvarLancamento(Lancamento lancamento, String usuario) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path.toFile(), true))) {
            pw.println();
            pw.println("------- Salvando produto ---------");
            pw.println("usuario data-hora " + usuario.toUpperCase() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            pw.println("mesa    " + lancamento.getMesa());
            pw.println("comanda " + lancamento.getComanda());
            pw.println("referencia " + lancamento.getReferencia());
            pw.println("descricao " + lancamento.getDescricao());
            pw.println("quantidade " + lancamento.getQuantidade());
            pw.println("valor_unitario " + lancamento.getPreco());
            pw.println("valor_total " + lancamento.getPrecoTotal());
            pw.println("observacao " + lancamento.getObservacao());
            pw.println("vendedor " + lancamento.getVendedor());
            pw.println("pedido " + lancamento.getPedido());
            pw.println("_________________________");
            pw.flush();
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao gravar informação no arquivo de  log.txt");
        }
    }

    public void registrarErroAoSalvarProduto(String error, Lancamento lancamento) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path.toFile(), true))) {
            pw.println();
            pw.println("------- error ao salvar produto ---------");
            pw.println("usuario data-hora " + lancamento.getVendedor().toUpperCase() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            pw.println("mesa    " + lancamento.getMesa());
            pw.println("comanda " + lancamento.getComanda());
            pw.println("referencia " + lancamento.getReferencia());
            pw.println("quantidade " + lancamento.getQuantidade());
            pw.println("observacao " + lancamento.getObservacao());
            pw.println("vendedor " + lancamento.getVendedor());
            pw.println("pedido " + lancamento.getPedido());
            pw.println("_________________________");
            pw.flush();
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao gravar informação no arquivo de  log.txt");
        }
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
        String nomeArquivo = "log-" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ".txt";
        String url = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/" + nomeArquivo);
        return url;
    }
}
