/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Acompanhamento;
import org.hibernate.Session;
import servico.AcompanhamentoService;
import util.HibernateUtil;

@ManagedBean(name = "acompanhamentoService")
@ViewScoped
public class AcompanhamentoControle implements AcompanhamentoService {

    private Session session = null;

    @Override
    public void salvar(Acompanhamento acompanhamento) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.saveOrUpdate(acompanhamento);
            session.getTransaction().commit();
            session.close();
        }

    }

    @Override
    public void excluir(Acompanhamento acompanhamento) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.delete(acompanhamento);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public List<Acompanhamento> pesquisar(String nome) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Acompanhamento> acompanhamentos = null;
        if (session != null) {
            acompanhamentos = session.createQuery("from Acompanhamento where nome like '%" + nome + "%' order by nome").list();
            session.close();
        }
        return acompanhamentos;
    }

    @Override
    public List<Acompanhamento> pesquisarTodos() {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Acompanhamento> acompanhamentos = null;
        if (session != null) {
            acompanhamentos = session.createQuery("from Acompanhamento order by nome").list();
            session.close();
        }
        return acompanhamentos;
    }

    @Override
    public int verificarId() {
        session = HibernateUtil.getSessionFactory().openSession();
        int count=1;
        if(session != null){
           Object qtd= session.createSQLQuery("select count(*) as quantidade from acompanhamento").uniqueResult();
           if(qtd != null){
               count =Integer.parseInt(String.valueOf(qtd))+1;
           }
        }
        return count;
    }

    @Override
    public List<Acompanhamento> pesquisarPorGrupo(String grupo) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Acompanhamento> acompanhamentos = null;
        if (session != null) {
            acompanhamentos = session.createQuery("from Acompanhamento where grupo.nome ='" + grupo + "'").list();
            session.close();
        }
        return acompanhamentos;
    }

}
