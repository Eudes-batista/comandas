package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Vendedor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.VendedorService;
import util.HibernateUtil;

@ManagedBean(name = "vendedorService")
@ViewScoped
public class VendedorControle implements VendedorService, Serializable {

    @Override
    public List<Vendedor> listarVendedor() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Query query = session
                    .createSQLQuery("select RVIDEVEN as CODIGO,RVNOMEVE as NOME from SFTA01 order by RVNOMEVE").setResultTransformer(Transformers.aliasToBean(Vendedor.class));
            List<Vendedor> vendedores = query.list();
            session.close();
            return vendedores;
        }
        return null;
    }

    @Override
    public String validarVendedor(String senha) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Object vendedor = session.createSQLQuery("select RVNOMEVE as vendedor from sfta01 left outer join lapa02 on(L2NOMEUS=RVNOMEVE) where L2SENHAU = '" + senha + "'").uniqueResult();
        session.close();
        return String.valueOf(vendedor);
    }

}
