package controle;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Cancelamento;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import servico.CancelamentoService;
import util.HibernateUtil;

@ManagedBean(name = "cancelamentoService")
@ViewScoped
public class CancelamentoControle implements CancelamentoService {

    @Override
    public boolean salvar(Cancelamento cancelamento) {
        Session session = null;
        boolean salvou = false;
        cancelamento.setNumero(gerarChavePrimaria());
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.save(cancelamento);
            session.getTransaction().commit();
            salvou = true;
        } catch (HibernateException ex) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            salvou = false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return salvou;
    }

    @Override
    public List<Cancelamento> listar() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from Cancelamento");
        List<Cancelamento> cancelamentos = query.list();
        session.close();
        return cancelamentos;
    }

    @Override
    public String gerarChavePrimaria() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery("select coalesce(max(cast(numero as integer)),0)+1 as contador from cancelamento_mesa");
        Object obj = query.uniqueResult();
        session.close();
        return String.valueOf(obj);
    }

}
