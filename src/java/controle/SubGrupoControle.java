package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;
import servico.SubGrupoService;
import util.HibernateUtil;

@ManagedBean(name="subGrupoService")
@ViewScoped
public class SubGrupoControle implements SubGrupoService,Serializable{
    @Override
    public List<Object[]> listarSubGrupo(String referencia) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> produto = session
                .createSQLQuery("select PRSUBGRP from scea01 where PRREFERE='" + referencia + "' or PRCODBAR='" + referencia + "'").list();
        session.close();
        return produto;
    }
}
