/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Lapt51;
import org.hibernate.Session;
import servico.GrupoServico;

@ManagedBean(name = "grupoServico")
@ViewScoped
public class GrupoControle implements GrupoServico,Serializable {

    private Session session = null;

    @Override
    public List<Lapt51> listarGrupos() {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Lapt51> grupos = session.createQuery("select g from Lapt51 g ORDER BY g.t51dsgrp").list();
        if (session != null) {
            session.close();
        }
        return grupos;
    }

}
