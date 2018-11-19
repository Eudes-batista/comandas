package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import modelo.Configuracao;
import org.omnifaces.util.Messages;

public class GerenciaArquivo {

    private Configuracao configuracao;

    public GerenciaArquivo() {
        if (this.configuracao == null) {
            this.configuracao = new Configuracao();
        }
    }

    private String carregarCaimnho() {
        String url = FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/configBanco.txt");
        return url;
    }

    public GerenciaArquivo bucarInformacoes() {
        try {
            List<String> lines = Files.lines(Paths.get(carregarCaimnho())).collect(Collectors.toList());
            if (!lines.isEmpty()) {
                getConfiguracao().setCaminho(lines.get(0));
                getConfiguracao().setEmpresa(lines.get(1));
                getConfiguracao().setTipoImpressao(lines.get(2));
                getConfiguracao().setImpressora(lines.get(3));
                getConfiguracao().setCobraDezPorcento(lines.get(4));
            }
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao consultar no arquivo " + ex.getMessage());
        }
        return this;
    }

    public GerenciaArquivo salvar() {

        if (carregarCaimnho() != null && !carregarCaimnho().isEmpty()) {
            try (PrintWriter pw = new PrintWriter(carregarCaimnho())) {
                pw.println(configuracao.getCaminho());
                pw.println(configuracao.getEmpresa());
                pw.println(configuracao.getTipoImpressao());
                pw.println(configuracao.getImpressora() == null ?"":configuracao.getImpressora());
                pw.println(configuracao.getCobraDezPorcento()== null ?"":configuracao.getCobraDezPorcento());
                Messages.addGlobalInfo("salvo com sucesso!!");
            } catch (IOException ex) {
                Messages.addGlobalError("Erro ao escrever arquivo configBanco.txt " + ex.getMessage());
            }
        }
        return this;
    }

    public GerenciaArquivo criarArquivo() {
        Path path = Paths.get(carregarCaimnho());
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException ex) {
                Messages.addGlobalError("Erro ao criar arquivo configBanco.txt " + ex.getMessage());
            }
        }
        return this;
    }

    public GerenciaArquivo setConfiguracao(Configuracao configuracao) {
        this.configuracao = configuracao;
        return this;
    }

    public Configuracao getConfiguracao() {
        return configuracao;
    }

}
