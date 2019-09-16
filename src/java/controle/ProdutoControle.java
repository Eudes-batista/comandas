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
    public List<Produto> lsitarProdutos() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Query query = session.createSQLQuery("select first 20 prrefere as REFERENCIA,prdescri as DESCRICAO,EEPLQTB1 as PRECO,T51DSGRP as GRUPO from scea07 left outer join scea01 on(prrefere=eerefere) left outer join LAPT51 on(T51CDGRP=PRCGRUPO) where PRDATCAN is null group by prrefere,prdescri,EEPLQTB1,T51DSGRP order by prdescri").setResultTransformer(Transformers.aliasToBean(Produto.class));
            List<Produto> produtos = query.list();
            session.close();
            return produtos;
        }
        return null;
    }
    

    @Override
    public List<Produto> lsitarProdutoPorGrupo(String grupo) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Query query = session.createSQLQuery("select first 20 prrefere as REFERENCIA,prdescri as DESCRICAO,EEPLQTB1 as PRECO,T51DSGRP as GRUPO from scea07 left outer join scea01 on(prrefere=eerefere) left outer join LAPT51 on(T51CDGRP=PRCGRUPO) where PRDATCAN is null and PRCGRUPO='" + grupo + "' group by prrefere,prdescri,EEPLQTB1,T51DSGRP order by prdescri").setResultTransformer(Transformers.aliasToBean(Produto.class));
            List<Produto> produtos = query.list();
            session.close();
            return produtos;
        }
        return null;
    }

    @Override
    public List<Produto> listarPorReferenciaDescricaoCodigoBarra(String pesquisa) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Query query = session.createSQLQuery("select first 20 prrefere as REFERENCIA,prdescri as DESCRICAO,EEPLQTB1 as PRECO,T51DSGRP as GRUPO from scea07 left outer join scea01 on(prrefere=eerefere) left outer join LAPT51 on(T51CDGRP=PRCGRUPO) where PRDATCAN is null and prrefere='" + pesquisa + "' or prdescri like '%" + pesquisa + "%' or prcodbar='" + pesquisa + "' group by prrefere,prdescri,EEPLQTB1,T51DSGRP order by prdescri").setResultTransformer(Transformers.aliasToBean(Produto.class));
            List<Produto> produtos = query.list();
            session.close();
            return produtos;
        }
        return null;
    }

    @Override
    public Produto buscarProduto(String referencia) {
        session = HibernateUtil.getSessionFactory().openSession();
        SQLQuery consulta = session.createSQLQuery("select prrefere as referencia,prdescri as descricao,EEPLQTB1 as preco from scea01 left outer join scea07 on(eerefere=prrefere) where PRDATCAN is null and prrefere='" + referencia + "' or prcodbar='" + referencia + "' group by prrefere,prdescri,EEPLQTB1");
        Query consultaTransformada = consulta.setResultTransformer(Transformers.aliasToBean(Produto.class));
        Produto produto = (Produto) consultaTransformada.uniqueResult();
        if (session != null) {
            session.close();
        }
        return produto;
    }
    
    @Override
    public Produto buscarProdutoFastFood(String referencia) {
        session = HibernateUtil.getSessionFactory().openSession();
        SQLQuery consulta = session.createSQLQuery("select prrefere as referencia,prdescri as descricao,EEPLQTB1 as preco,PRUNIDAD as UNIDADE from scea01 left outer join scea07 on(eerefere=prrefere) where PRDATCAN is null and prrefere='" + referencia + "' or prcodbar='" + referencia + "' group by prrefere,prdescri,EEPLQTB1,PRUNIDAD");
        Query consultaTransformada = consulta.setResultTransformer(Transformers.aliasToBean(Produto.class));
        Produto produto = (Produto) consultaTransformada.uniqueResult();
        if (session != null) {
            session.close();
        }
        return produto;
    }

    @Override
    public List<Produto> lsitarProdutosFastFood() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Query query = session.createSQLQuery("select first 20 prrefere as REFERENCIA,prdescri as DESCRICAO,EEPLQTB1 as PRECO,T51DSGRP as GRUPO,PRUNIDAD as UNIDADE from scea07 left outer join scea01 on(prrefere=eerefere) left outer join LAPT51 on(T51CDGRP=PRCGRUPO) where PRDATCAN is null group by prrefere,prdescri,EEPLQTB1,T51DSGRP,PRUNIDAD order by prdescri").setResultTransformer(Transformers.aliasToBean(Produto.class));
            List<Produto> produtos = query.list();
            session.close();
            return produtos;
        }
        return null;
    }
    
    @Override
    public List<Produto> lsitarProdutoPorGrupoFastFood(String grupo) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Query query = session.createSQLQuery("select first 20 prrefere as REFERENCIA,prdescri as DESCRICAO,EEPLQTB1 as PRECO,T51DSGRP as GRUPO,PRUNIDAD as UNIDADE from scea07 left outer join scea01 on(prrefere=eerefere) left outer join LAPT51 on(T51CDGRP=PRCGRUPO) where PRCGRUPO='" + grupo + "' group by prrefere,prdescri,EEPLQTB1,T51DSGRP,PRUNIDAD order by prdescri").setResultTransformer(Transformers.aliasToBean(Produto.class));
            List<Produto> produtos = query.list();
            session.close();
            return produtos;
        }
        return null;
    }
    
    @Override
    public List<Produto> listarPorReferenciaDescricaoCodigoBarraFastFood(String pesquisa) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Query query = session.createSQLQuery("select first 20 prrefere as REFERENCIA,prdescri as DESCRICAO,EEPLQTB1 as PRECO,T51DSGRP as GRUPO,PRUNIDAD as UNIDADE from scea07 left outer join scea01 on(prrefere=eerefere) left outer join LAPT51 on(T51CDGRP=PRCGRUPO) where prrefere='" + pesquisa + "' or prdescri like '%" + pesquisa + "%' or prcodbar='" + pesquisa + "' group by prrefere,prdescri,EEPLQTB1,T51DSGRP,PRUNIDAD order by prdescri").setResultTransformer(Transformers.aliasToBean(Produto.class));
            List<Produto> produtos = query.list();
            session.close();
            return produtos;
        }
        return null;
    }
}
