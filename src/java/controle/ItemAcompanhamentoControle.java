package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.ItemAcompanhamento;
import modelo.Lancamento;
import org.hibernate.Session;
import servico.ItemAcompanhamentoService;
import util.HibernateUtil;

@ManagedBean(name = "itemAcompanhamentoService")
@ViewScoped
public class ItemAcompanhamentoControle implements ItemAcompanhamentoService, Serializable {

    private Session session = null;

    @Override
    public void salvar(ItemAcompanhamento itemAcompanhamento) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.save(itemAcompanhamento);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public void excluir(ItemAcompanhamento itemAcompanhamento) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.delete(itemAcompanhamento);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public List<ItemAcompanhamento> pesquisarTodos() {
        session = HibernateUtil.getSessionFactory().openSession();
        List<ItemAcompanhamento> itemAcompanhamentos = null;
        if (session != null) {
            itemAcompanhamentos = session.createQuery("from ItemAcompanhamento").list();
            session.close();
        }
        return itemAcompanhamentos;
    }

    @Override
    public List<ItemAcompanhamento> pesquisarNome(String nome) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<ItemAcompanhamento> itemAcompanhamentos = null;
        if (session != null) {
            itemAcompanhamentos = session.createQuery("from ItemAcompanhamento where acompanhamento like '%"+nome+"%'").list();
            session.close();
        }
        return itemAcompanhamentos;
    }

    @Override
    public List<ItemAcompanhamento> pesquisarItem(String item,String pedido) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<ItemAcompanhamento> itemAcompanhamentos = null;
        if (session != null) {
            itemAcompanhamentos = session.createQuery("from ItemAcompanhamento where item ='"+item+"' and pedido='"+pedido+"'").list();
            session.close();
        }
        return itemAcompanhamentos;
    }

    @Override
    public void excluirTodos(String Item,String pedido) {
        executarSql("delete from item_acompanhamento where item='"+Item+"' and pedido='"+pedido+"'");
    }

    @Override
    public void excluirTodos(String pedido) {
        executarSql("delete from item_acompanhamento where pedido='"+pedido+"'");
    }

    @Override
    public void atualizarStatusAcompanhamento(Lancamento lancamento, String status) {
        executarSql("update item_acompanhamento set status='"+status+"' where pedido='"+lancamento.getPedido()+"' and item='"+lancamento.getItem()+"'");
    }
    
    private void executarSql(String sql) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.createSQLQuery(sql).executeUpdate();
        session.getTransaction().commit();
        session.flush();
        session.close();
    }
    
}
