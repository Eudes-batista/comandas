package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
            if (!lines.isEmpty() && !"".equals(lines.get(0))) {
                getConfiguracao().setCaminho(lines.get(0));
                getConfiguracao().setEmpresa(lines.get(1));
                getConfiguracao().setImpressora(lines.get(2));
                getConfiguracao().setCobraDezPorcento(lines.get(3));
                getConfiguracao().setCondicaoParaImpressao(lines.get(4));
                getConfiguracao().setGerarComandaAutomatico(lines.get(5));
                if(lines.size() > 6){
                    getConfiguracao().setCaminhoCatraca(lines.get(6));
                }
            }
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao consultar no arquivo " + ex.getMessage());
        }
        return this;
    }

    public GerenciaArquivo salvar() {

        if (carregarCaimnho() != null && !carregarCaimnho().isEmpty()) {
            try (PrintWriter pw = new PrintWriter(carregarCaimnho())) {
                pw.println(this.configuracao.getCaminho());
                pw.println(this.configuracao.getEmpresa());
                pw.println(this.configuracao.getImpressora() == null ? "" : this.configuracao.getImpressora());
                pw.println(this.configuracao.getCobraDezPorcento() == null ? "" : this.configuracao.getCobraDezPorcento());
                pw.println(this.configuracao.getCondicaoParaImpressao() == null ? "" : this.configuracao.getCondicaoParaImpressao());
                pw.println(this.configuracao.getGerarComandaAutomatico());
                pw.println(this.configuracao.getCaminhoCatraca());
                pw.flush();
                salvarAsConfiguracoesDoBancoDeDados(this.configuracao.getCaminho());
                Messages.addGlobalInfo("salvo com sucesso!!");
            } catch (IOException | URISyntaxException ex) {
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
        return this.configuracao;
    }

    public URI buscarCaminho() throws URISyntaxException {
        return getClass().getResource("/hibernate.cfg.xml").toURI();
    }

    public List<String> pegarInformacoesHibernateConfig() throws URISyntaxException, IOException {
        File file = new File(buscarCaminho());
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> dados = new ArrayList<>();
        while (br.ready()) {
            dados.add(br.readLine());
        }
        return dados;
    }

    public List<String> mudarCaminhoDoBancoDeDados(String caminhoDoBancoDeDados) throws URISyntaxException, IOException {
        List<String> dados = pegarInformacoesHibernateConfig();
        int count = 0;
        for (String dado : dados) {
            if (dado.contains("<property name=\"hibernate.connection.url\">")) {
                String caminho = "    <property name=\"hibernate.connection.url\">jdbc:firebirdsql:" + caminhoDoBancoDeDados + "</property>";
                dados.set(count, caminho);
            }
            count++;
        }
        return dados;
    }

    public void salvarAsConfiguracoesDoBancoDeDados(String caminhoDoBancoDeDados) throws URISyntaxException, IOException {
        List<String> configuracoesAlteradas = mudarCaminhoDoBancoDeDados(caminhoDoBancoDeDados);
        try (PrintWriter pw = new PrintWriter(new File(buscarCaminho()))) {
            configuracoesAlteradas.forEach(configuracoesAlterada ->pw.println(configuracoesAlterada));
            pw.flush();
        }
    }

}
