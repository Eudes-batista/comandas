package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.MotivoCancelamento;
import org.hibernate.Session;
import servico.MotivoCancelamentoService;
import util.HibernateUtil;

@ManagedBean(name = "motivoCancelamentoService")
@ViewScoped
public class MotivoCancelamentoControle implements MotivoCancelamentoService, Serializable {

    private Session session = null;

    @Override
    public void salvar(MotivoCancelamento motivoCancelamento) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.saveOrUpdate(motivoCancelamento);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public void excluir(MotivoCancelamento motivoCancelamento) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.delete(motivoCancelamento);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public List<MotivoCancelamento> pesquisar(String nome) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<MotivoCancelamento> itemAcompanhamentos = null;
        if (session != null) {
            itemAcompanhamentos = session.createQuery("from MotivoCancelamento where nome like '%"+nome+"%'").list();
            session.close();
        }
        return itemAcompanhamentos;
    }

    @Override
    public List<MotivoCancelamento> pesquisarTodos() {
        session = HibernateUtil.getSessionFactory().openSession();
        List<MotivoCancelamento> itemAcompanhamentos = null;
        if (session != null) {
            itemAcompanhamentos = session.createQuery("from MotivoCancelamento").list();
            session.close();
        }
        return itemAcompanhamentos;
    }

    @Override
    public int verificarId() {
        session = HibernateUtil.getSessionFactory().openSession();
        int seguenciaChavePrimaria=1;
        if(session != null){
           Object qtd= session.createSQLQuery("select first 1 CODIGO  from motivo_cancelamento order by codigo desc").uniqueResult();
           if(qtd != null){
               seguenciaChavePrimaria =Integer.parseInt(String.valueOf(qtd))+1;
           }
           session.close();
        }
        return seguenciaChavePrimaria;
    }

    @Override
    public MotivoCancelamento buscarMotivoPorCodigo(int codigo) {
        session = HibernateUtil.getSessionFactory().openSession();
        if(session != null){
           MotivoCancelamento motivoCancelamento =(MotivoCancelamento) session.createQuery("from MotivoCancelamento where codigo ="+codigo+" ").uniqueResult();
           session.close();
           return motivoCancelamento;
        }
        return null;
    }
    
    

}
