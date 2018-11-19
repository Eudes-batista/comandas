package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;
import servico.UsuarioService;
import util.HibernateUtil;

@ManagedBean(name = "usuarioService")
@ViewScoped
public class UsuarioControle implements UsuarioService,Serializable {

    @Override
    public List<Object[]> pequisarUsuarios(String usuario, String senha) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> usuarios = session
                .createSQLQuery("select L2NOMEUS,L2SENHAU,L2GERENT from lapa02 where L2NOMEUS='" + usuario + "' and L2SENHAU='" + senha + "'").list();
        session.close();
        return usuarios;
    }
}
