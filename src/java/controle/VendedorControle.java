package controle;

import java.io.Serializable;
import util.HibernateUtil;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;
import servico.VendedorService;

@ManagedBean(name = "vendedorService")
@ViewScoped
public class VendedorControle implements VendedorService, Serializable {

    @Override
    public List<Object[]> listarVendedor() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> vendedores = session
                .createSQLQuery("select RVIDEVEN,RVNOMEVE from SFTA01 order by RVNOMEVE").list();
        session.close();
        return vendedores;
    }

    @Override
    public String validarVendedor(String senha) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Object uniqueResult = session.createSQLQuery("select RVNOMEVE as vendedor from sfta01 left outer join lapa02 on(L2NOMEUS=RVNOMEVE) where L2SENHAU = '" + senha + "'").uniqueResult();
        return String.valueOf(uniqueResult);
    }

}
