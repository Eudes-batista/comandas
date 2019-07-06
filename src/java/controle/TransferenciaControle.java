package controle;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Transferencia;
import modelo.dto.FiltroTransferencia;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import servico.TransferenciaService;
import util.HibernateUtil;
import util.Log;

@ManagedBean(name = "transferenciaService")
@ViewScoped
public class TransferenciaControle implements TransferenciaService{
    private Session session = null;
    
    
    @Override
    public void salvar(Transferencia transferencia)  {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(transferencia);
            transaction.commit();
        } catch (Exception ex) {
            new Log().registrarErroAoSalvarTransferencia(ex.getMessage(), transferencia);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            if (this.session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public List<Transferencia> listarTransferencias(FiltroTransferencia filtroTransferencia){
        session = HibernateUtil.getSessionFactory().openSession();
        Query query =session.createQuery("from Transferencia where data between '"+filtroTransferencia.getDataInicial()+" 00:00:00' and '"+filtroTransferencia.getDataFinal()+" 23:59:59'");
        List<Transferencia> transferencias=query.list();
        session.close();
        return transferencias;
    }
}
