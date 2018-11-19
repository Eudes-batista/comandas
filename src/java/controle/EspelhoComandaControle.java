package controle;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.EspelhoComanda;
import modelo.Mesa;
import org.hibernate.Session;
import servico.EspelhoComandaService;
import util.HibernateUtil;

@ManagedBean(name = "espelhoComandaService")
@ViewScoped
public class EspelhoComandaControle implements EspelhoComandaService, Serializable {

    private Session session = null;

    @Override
    public void salvar(EspelhoComanda espelhoComanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.save(espelhoComanda);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public void excluir(Integer numero) {
        executarSql("delete from espelho_comanda where numero =" + numero + "");
    }

    @Override
    public void atualizarStatusImpressao(String comanda) {
        executarSql("update espelho_comanda set impressao='P' where comanda='" + comanda + "'");
    }

    @Override
    public void atualizarDataPreconta(String data, Mesa mesa) {
        executarSql("update espelho_comanda set data_preconta='" + data + "',pessoas_pagantes='" + mesa.getQuantidadePessoasPagantes() + "' where pedido in(" + mesa.getPedido() + ")");
    }

    @Override
    public void alterar(EspelhoComanda espelhoComanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.update(espelhoComanda);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public EspelhoComanda buscarPorId(Integer numero) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            EspelhoComanda espelhoComanda =(EspelhoComanda) session.createQuery("from EspelhoComanda where numero="+numero).uniqueResult();
            session.close();
            return espelhoComanda;
        }
        return new EspelhoComanda();
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
