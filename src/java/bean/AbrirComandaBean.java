package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import servico.ComandaService;

@ManagedBean(name = "abrirComandaBean")
@ViewScoped
@Getter
@Setter
public class AbrirComandaBean implements Serializable {

    private String mesa;
    private String comanda;
    private boolean checked;
    private String quantidadePessoa;

    @ManagedProperty(value = "#{controle}")
    private ComandaService controleService;
    
    public String verificar() {
        if (!mesa.equals("RSVA")) {
            mesa = String.format("%04d", Integer.parseInt(mesa));
        }
        comanda = String.format("%04d", Integer.parseInt(comanda));
        int verificarComanda = controleService.verificarComanda(mesa, comanda);
        if (verificarComanda != 0) {
            Messages.addGlobalWarn("Comanda já existe em outra mesa. ");
            return "";
        }
        String endereco = "produtos.jsf?comanda=" + comanda + "&mesa=" + mesa +"&pessoas="+quantidadePessoa+ "&faces-redirect=true";
        return endereco;
    }

    public void reserva() {
        if (checked) {
            this.mesa = "RSVA";
        } else {
            this.mesa = "";
        }
    }

    public String getQuantidadePessoa() {
        return this.quantidadePessoa == null ? "1" : this.quantidadePessoa;
    }

}
