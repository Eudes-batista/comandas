/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.GrupoAcompanhamento;
import org.hibernate.Session;
import servico.GrupoAcompanhamentoService;
import util.HibernateUtil;

/**
 *
 * @author Administrador
 */
@ManagedBean(name = "grupoAcompanhamentoService")
@ViewScoped
public class GrupoAcompanhamentoControle implements GrupoAcompanhamentoService {

    private Session session = null;

    @Override
    public void salvar(GrupoAcompanhamento grupoAcompanhamento) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.saveOrUpdate(grupoAcompanhamento);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public void excluir(GrupoAcompanhamento grupoAcompanhamento) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.delete(grupoAcompanhamento);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public List<GrupoAcompanhamento> pesquisar(String nome) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<GrupoAcompanhamento> grupoAcompanhamentos = null;
        if (session != null) {
            grupoAcompanhamentos = session.createQuery("from GrupoAcompanhamento where nome like '%" + nome + "%' order by nome").list();
            session.close();
        }
        return grupoAcompanhamentos;
    }

    @Override
    public List<GrupoAcompanhamento> pesquisarTodos() {
        session = HibernateUtil.getSessionFactory().openSession();
        List<GrupoAcompanhamento> grupoAcompanhamentos = null;
        if (session != null) {
            grupoAcompanhamentos = session.createQuery("from GrupoAcompanhamento  order by nome").list();
            session.close();
        }
        return grupoAcompanhamentos;
    }

    @Override
    public int verificarId() {
        session = HibernateUtil.getSessionFactory().openSession();
        int count=1;
        if(session != null){
           Object qtd= session.createSQLQuery("select first 1 CODIGO  from grupo_acompanhamento order by codigo desc").uniqueResult();
           if(qtd != null){
               count =Integer.parseInt(String.valueOf(qtd))+1;
           }
           session.close();
        }
        return count;
    }

}
