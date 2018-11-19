package servico;

import java.util.List;

public interface UsuarioService {
    public List<Object[]> pequisarUsuarios(String usuario, String senha);
}
