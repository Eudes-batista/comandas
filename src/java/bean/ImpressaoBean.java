package bean;

import com.itextpdf.text.DocumentException;
import controle.ControleImpressao;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import modelo.Impressao;
import modelo.Lancamento;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.omnifaces.util.Messages;
import relatorio.PdfPedido;
import util.GerenciaArquivo;

@ManagedBean(name = "impressaoBean")
@ViewScoped
@Getter
@Setter
public class ImpressaoBean implements Serializable {

    @ManagedProperty(value = "#{impressao}")
    private Impressao impressao;

    private Path path;

    private List<Impressao> impressoes = new ArrayList<>();

    public void init() {
        criarArquivo();
        listar();
    }

    private boolean prepararImpressao(StringBuilder str) {
        return Files.exists(getPath()) && !str.toString().isEmpty();
    }

    private void listar() {
        impressoes.clear();
        try {
            Ini ini = new Ini(new File(buscarCaminho()));
            ini.entrySet().forEach((Map.Entry<String, Section> t) -> impressoes.add(new Impressao(t.getKey(), t.getValue().get("impressora"))));
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao ler arquivo Config.ini");
        }
    }

    public void salvar() {
        try {
            Ini ini = new Ini(new File(buscarCaminho()));
            ini.put(this.impressao.getSubgrupo().toUpperCase(), "impressora", this.impressao.getCaminhoImpressora());
            ini.store();
            listar();
            this.impressao = null;
            this.impressao = new Impressao();
            Messages.addGlobalInfo("Salvo com sucesso!!");
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao salvar: detalhe do erro \n" + ex.getMessage());
        }
    }

    public void excluir(Impressao impressoa) {
        try {
            Ini ini = new Ini(new File(buscarCaminho()));
            ini.remove(impressoa.getSubgrupo(), "impressora");
            ini.store();
            listar();
            Messages.addGlobalInfo("excluido com sucesso!!");
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao excluir: detalhe do erro \n" + ex.getMessage());
        }
    }

    public void testarConexaoImpressora(Impressao impressoa) {
        GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();
        if (gerenciaArquivo.bucarInformacoes().getConfiguracao().getTipoImpressao().equals("rede")) {
            StringBuilder str = prepararDocumento();
            boolean prepararImpressao = prepararImpressao(str);
            if (prepararImpressao) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(new File(impressoa.getCaminhoImpressora()))) {
                    try (PrintStream printStream = new PrintStream(fileOutputStream)) {
                        printStream.println(str);
                        for (int i = 0; i < 10; i++) {
                            printStream.println();
                            printStream.flush();
                        }
                    }
                } catch (Exception ex) {
                    Messages.addGlobalError("Impressora desligada ou cambo desconectado.\n" + impressoa.getCaminhoImpressora().toLowerCase());
                }
            }
        } else {
            List<Lancamento> lancamentos = new ArrayList<>();
            Lancamento lancamento = new Lancamento();
            lancamento.setMesa("0001");
            lancamento.setComanda("0001");
            lancamento.setNumero("0011");
            lancamento.setItem("0001");
            lancamento.setReferencia("0001");
            lancamento.setDescricao("Coca cola");
            lancamento.setQuantidade(1);
            lancamento.setPreco(10);
            lancamento.setPrecoTotal(20);
            lancamento.setObservacao("Sem gelo");
            lancamento.setVendedor("Loja");
            lancamentos.add(lancamento);
            try {
                new ControleImpressao(impressoa.getCaminhoImpressora()).imprime(new PdfPedido(lancamentos, null).gerarPdf());
            } catch (FileNotFoundException | DocumentException ex) {
                Messages.addGlobalError("caminho do arquivo está errado");
            } catch (IOException | PrinterException ex) {
                Messages.addGlobalError("Impressora desligada ou cambo desconectado.\n" + impressoa.getCaminhoImpressora().toLowerCase());
            }
        }

    }

    private void criarArquivo() {
        try {
            if (!Files.exists(Paths.get(buscarCaminho()))) {
                setPath(Files.createFile(Paths.get(buscarCaminho())));
            } else {
                setPath(Paths.get(buscarCaminho()));
            }
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao criar arquivo");
        }
    }

    public String buscarCaminho() {
        return FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/Config.ini");
    }

    private StringBuilder prepararDocumento() {
        StringBuilder str = new StringBuilder();
        str.append("\t").append("Comanda: 0001").append("\n");
        str.append("\n");
        str.append("ITEM \t\t QUANTIDADE");
        str.append("\n");
        str.append("COCA COLA \t\t 1");
        str.append("\n");
        str.append("OBS:");
        str.append("\n");
        str.append("    COCA COLA  COM GELO");
        return str;
    }

}
