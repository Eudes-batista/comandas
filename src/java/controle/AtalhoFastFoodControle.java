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
public class AtalhoFastFoodControle implements AtalhoFastFoodService, Serializable {

    private Session session = null;

    @Override
    public void salvar(AtalhoFastFood atalhoFastFood) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.saveOrUpdate(atalhoFastFood);
            session.getTransaction().commit();
            session.close();
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

    public int verificarId() {
        session = HibernateUtil.getSessionFactory().openSession();
        int count = 1;
        if (session != null) {
            Object qtd = session.createSQLQuery("select first 1 CODIGO  from atalho_fastfood order by codigo desc").uniqueResult();
            if (qtd != null) {
                count = Integer.parseInt(String.valueOf(qtd)) + 1;
            }
            session.close();
        }
        return count;
    }

}
