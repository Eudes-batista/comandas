package util;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@ManagedBean
@ViewScoped
public class GerenciaEntrada implements Serializable {

    public boolean filtroEntrada() {
        GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();
        gerenciaArquivo.criarArquivo().bucarInformacoes();
        if (gerenciaArquivo.getConfiguracao().getCaminho() == null) {
            enviarParaConexaoBanco();
            return false;
        } else {
            if (HibernateUtil.getSessionFactory() == null) {
                enviarParaConexaoBanco();
                return false;
            }
        }
        return true;
    }

    private void enviarParaConexaoBanco() {
        try {
            Faces.redirect("conectaBanco.jsf");
        } catch (IOException ex) {
            Messages.addGlobalError("Erro ao acessar a pagina conectaBanco.jsf");
        }
    }

}
