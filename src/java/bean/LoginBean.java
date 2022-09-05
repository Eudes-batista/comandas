
package bean;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import modelo.dto.Usuario;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import servico.UsuarioService;

@ManagedBean(name="loginBean")
@ViewScoped
@Getter @Setter
public class LoginBean implements Serializable{

    @ManagedProperty(value="#{usuarioService}")
    private UsuarioService usuarioService;
    @ManagedProperty(value="#{usuarioBean}")
    private UsuarioBean usuarioBean;
    
    private Usuario usuario;
    
    public void init() {
        this.usuario=new Usuario();
    }
    
    public void logar(){
        usuario.setNOME(usuario.getNOME().toUpperCase());
        usuarioBean.setUsuario(usuario);
        usuario.setSENHA(usuarioBean.gerarSenha());
        Usuario usuarioEncontrado =usuarioService.validarUsuario(this.usuario);
        if(usuarioEncontrado == null){
            Messages.addGlobalWarn("Usuario ou senha incorreta.");
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",usuarioEncontrado);     
        try {
            Faces.redirect("comandasFastFood.jsf");
        } catch (IOException ex) {
            Messages.addGlobalWarn("Erro ao abrir pagina de lançamentos.");
        }
    }
    
}
