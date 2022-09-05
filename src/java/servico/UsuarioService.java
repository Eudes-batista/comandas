package servico;

import java.util.List;
import modelo.dto.Usuario;

public interface UsuarioService {
    public List<Object[]> pequisarUsuarios(String usuario, String senha);
    public Usuario validarUsuario(Usuario usuario);
}
