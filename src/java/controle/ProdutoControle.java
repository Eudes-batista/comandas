/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Produto;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.ProdutoService;
import util.HibernateUtil;

/**
 *
 * @author Administrador
 */
@ManagedBean(name = "produtoServico")
@ViewScoped
public class ProdutoControle implements ProdutoService, Serializable {

    private Session session = null;

    @Override
    public List<Object[]> lsitarProdutos() {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> produtos = session.createSQLQuery("select first 30 prrefere,prdescri,EEPLQTB1 from scea01 left outer join scea07 on(eerefere=prrefere) group by prrefere,prdescri,EEPLQTB1 order by prdescri").list();
        if (session != null) {
            session.close();
        }
        return produtos;
    }
    @Override
    public List<Object[]> lsitarProdutoPorGrupo(String grupo) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> produtos = session.createSQLQuery("select first 30 prdescri,EEPLQTB1,prrefere from scea01 left outer join scea07 on(eerefere=prrefere) where PRCGRUPO='"+grupo+"' group by prdescri,EEPLQTB1,prrefere order by prdescri").list();
        if (session != null) {
            session.close();
        }
        return produtos;
    }

    @Override
    public List<Object[]> listarPorReferenciaDescricaoCodigoBarra(String pesquisa) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> produtos = session.createSQLQuery("select first 30 prdescri,prrefere,EEPLQTB1 from scea01 left outer join scea07 on(eerefere=prrefere) where prrefere='"+pesquisa+"' or prdescri like '%"+pesquisa+"%' or prcodbar='"+pesquisa+"' group by prdescri,EEPLQTB1,prrefere order by prdescri").list();
        if (session != null) {
            session.close();
        }
        return produtos;        
    }

    @Override
    public Produto buscarProduto(String referencia) {
        session = HibernateUtil.getSessionFactory().openSession();
        SQLQuery consulta = session.createSQLQuery("select prrefere as referencia,prdescri as descricao,EEPLQTB1 as preco from scea01 left outer join scea07 on(eerefere=prrefere) where prrefere='"+referencia+"' or prcodbar='"+referencia+"' group by prrefere,prdescri,EEPLQTB1");
        Query consultaTransformada = consulta.setResultTransformer(Transformers.aliasToBean(Produto.class));
        Produto produto =(Produto)consultaTransformada.uniqueResult();
        if (session != null) {
            session.close();
        }
        return produto;        
    }
    
    

}
