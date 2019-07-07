package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Transferencia;
import modelo.dto.FiltroTransferencia;
import org.hibernate.Query;
import org.hibernate.Session;
import servico.TransferenciaService;
import util.HibernateUtil;
import util.Log;

@ManagedBean(name = "transferenciaService")
@ViewScoped
public class TransferenciaControle implements TransferenciaService,Serializable {

    private Session session = null;

    @Override
    public void salvar(Transferencia transferencia) {
        transferencia.setNumero(gerarChavePrimaria());
        this.session = HibernateUtil.getSessionFactory().openSession();
        try {
            this.session.getTransaction().begin();
            this.session.save(transferencia);
            this.session.getTransaction().commit();
        } catch (Exception ex) {
            new Log().registrarErroAoSalvarTransferencia(ex.getMessage(), transferencia);
            if (this.session != null && this.session.getTransaction().isActive()) {
                this.session.getTransaction().rollback();
            }
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    @Override
    public List<Transferencia> listarTransferencias(FiltroTransferencia filtroTransferencia) {
        this.session = HibernateUtil.getSessionFactory().openSession();
        Query query = this.session.createQuery("from Transferencia where data between '" + filtroTransferencia.getDataInicial() + " 00:00:00' and '" + filtroTransferencia.getDataFinal() + " 23:59:59'");
        List<Transferencia> transferencias = query.list();
        this.session.close();
        return transferencias;
    }

    public String gerarChavePrimaria() {
        this.session = HibernateUtil.getSessionFactory().openSession();
        Query query = this.session.createSQLQuery("select coalesce(max(cast(numero as integer)),0)+1 as contador from transferencia_mesa");
        Object obj = query.uniqueResult();
        this.session.close();
        return String.valueOf(obj);
    }
}
