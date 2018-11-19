package bean;

import util.GerenciaArquivo;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Configuracao;

@ManagedBean(name = "bancoConecta")
@ViewScoped
public class BancoConecta implements Serializable {

    private final GerenciaArquivo arquivo = new GerenciaArquivo();
    @Getter
    @Setter
    private Configuracao configuracao;

    @PostConstruct
    private void init() {
        this.configuracao = arquivo.bucarInformacoes().getConfiguracao();
    }

    public void salvar() {
        this.arquivo.criarArquivo().setConfiguracao(this.configuracao).salvar();
    }
}
