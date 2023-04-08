package bean;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.dto.Usuario;
import servico.UsuarioService;

@ManagedBean(name = "usuarioBean")
@ViewScoped
@Getter
@Setter
public class UsuarioBean implements Serializable {

    @ManagedProperty(value = "#{usuarioService}")
    private UsuarioService usuarioService;

    private Usuario usuario =new Usuario();

    public String gerarSenha() {
        StringBuilder sb = new StringBuilder();
        String senhaCript = "";
        for (int i = 1; i < 300; i++) {
            sb.append((char) i);
        }
        sb.append(" ");
        for (int i = 1; i <= this.usuario.getSENHA().length(); i++) {
            int cod = sb.indexOf(String.valueOf(this.usuario.getSENHA().charAt(i - 1))) + (i + 11);
            senhaCript += (char) cod;
        }
        return senhaCript;
    }

    public boolean validarGerente() {
        if (getUsuario().getNOME().toUpperCase().isEmpty() && gerarSenha().isEmpty()) {
            return false;
        }
        List<Object[]> usuarios = usuarioService.pequisarUsuarios(getUsuario().getNOME().toUpperCase(), gerarSenha());
        if (usuarios.isEmpty()) {
            return false;
        }
        return String.valueOf(usuarios.get(0)[2]).equals("T");
    }

}
