package controle;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.AtalhoFastFood;
import org.hibernate.Session;
import org.hibernate.Transaction;
import servico.AtalhoFastFoodService;
import util.HibernateUtil;

@ManagedBean(name = "atalhoFastFoodService")
@ViewScoped
public class AtalhoFastFoodControle implements AtalhoFastFoodService,Serializable {

    private Session session = null;

    @Override
    public void salvar(AtalhoFastFood atalhoFastFood) throws Exception {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(atalhoFastFood);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void excluir(AtalhoFastFood atalhoFastFood) throws Exception {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(atalhoFastFood);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public AtalhoFastFood carregar() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            return (AtalhoFastFood) session.createQuery("from AtalhoFastFood ").uniqueResult();
        }
        return null;
    }

}
