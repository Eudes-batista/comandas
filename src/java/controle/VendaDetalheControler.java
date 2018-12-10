package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaDetalhe;
import modelo.dto.VendaGarcom;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.VendaDetalheService;
import util.HibernateUtil;

@ManagedBean(name = "vendaDetalheService")
@ViewScoped
public class VendaDetalheControler implements VendaDetalheService, Serializable {

    private Session session = null;
    private StringBuilder sql;

    @Override
    public List<VendaGarcom> listarVendaGarcom(FiltroVendaDetalhe filtroVendaDetalhe) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            sql = new StringBuilder();
            sql.append("select ")
                    .append(" SUM(QUANTIDADE) as itens,")
                    .append(" SUM(QUANTIDADE*VALOR_ITEM) as vendas,")
                    .append(" VENDEDOR as garcom,")
                    .append(" SUM(VALOR_PORCENTAGEM) as COMISSAO,")
                    .append(" RVMETMES as META,")
                    .append(" (RVMETMES-SUM(QUANTIDADE*VALOR_ITEM)) as VALOR_A_ALCANCAR")
                    .append(" from")
                    .append(" espelho_comanda")
                    .append(" left outer join")
                    .append(" sfta01")
                    .append(" on(RVNOMEVE=VENDEDOR)")
                    .append(" where")
                    .append(" STATUS = 'P' AND STATUS_ITEM = 'N' AND")
                    .append(" DATA_PRECONTA BETWEEN '").append(filtroVendaDetalhe.getDataInicial()).append(" 00:00:00'").append(" and '").append(filtroVendaDetalhe.getDataFinal()).append(" 23:59:59'")
                    .append(" GROUP BY")
                    .append(" VENDEDOR,META");
            SQLQuery sQLQuery = session.createSQLQuery(sql.toString());
            Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(VendaGarcom.class));
            return query.list();
        }
        return null;
    }

    @Override
    public List<RejeicaoPorcentagemVendedor> listarReijeicaoPorcentagemPorVendedor(FiltroVendaDetalhe filtroVendaDetalhe) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            sql = new StringBuilder();
            sql.append("select ")
                    .append(" PEDIDO as PEDIDO")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" PORCENTAGEM <").append(10)
                    .append(" AND DATA_PRECONTA BETWEEN ").append("'").append(filtroVendaDetalhe.getDataInicial()).append(" 00:00:00' ")
                    .append(" AND '").append(filtroVendaDetalhe.getDataFinal()).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" AND VENDEDOR ='").append(filtroVendaDetalhe.getCargom()).append("'")
                    .append(" group by ")
                    .append(" PEDIDO ");
            SQLQuery sQLQuery = session.createSQLQuery(sql.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(RejeicaoPorcentagemVendedor.class));
            return setResultTransformer.list();
        }
        return null;
    }

    @Override
    public List<VendaDetalhe> listarRejeicaoDezPorcentoPorGarcom(FiltroVendaDetalhe filtroVendaDetalhe) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            sql = new StringBuilder();
            sql.append("select ")
                    .append(" PEDIDO as PEDIDO")
                    .append(",REFERENCIA as REFERENCIA")
                    .append(",PRDESCRI as DESCRICAO")
                    .append(",QUANTIDADE as QUANTIDADE")
                    .append(",VALOR_ITEM as VALOR")
                    .append(",SUM(VALOR_ITEM*QUANTIDADE) as TOTAL")
                    .append(",PORCENTAGEM as PORCENTAGEM")
                    .append(",VALOR_PORCENTAGEM VALOR_PORCENTAGEM")
                    .append(",DATA  as DATA")
                    .append(" from espelho_comanda ")
                    .append(" inner join")
                    .append(" scea01 on(prrefere=referencia)")
                    .append(" where")
                    .append(" PORCENTAGEM <").append(10).append(" and substr(prdescri,1,7) != 'COUVERT' " )
                    .append(" AND DATA_PRECONTA BETWEEN ").append("'").append(filtroVendaDetalhe.getDataInicial()).append(" 00:00:00' ")
                    .append(" AND '").append(filtroVendaDetalhe.getDataFinal()).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" AND VENDEDOR ='").append(filtroVendaDetalhe.getCargom()).append("'")
                    .append(" group by")
                    .append("    PEDIDO,REFERENCIA,PRDESCRI,QUANTIDADE,VALOR_ITEM,PORCENTAGEM,VALOR_PORCENTAGEM,DATA");
            System.out.println("sql = " + sql.toString());
            SQLQuery sQLQuery = session.createSQLQuery(sql.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(VendaDetalhe.class));
            return setResultTransformer.list();
        }
        return null;
    }

    @Override
    public List<VendaDetalhe> listarItensVendidosPorGarcom(FiltroVendaDetalhe filtroVendaDetalhe) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            sql = new StringBuilder();
            sql.append("select ")
                    .append(" PEDIDO as PEDIDO")
                    .append(",REFERENCIA as REFERENCIA")
                    .append(",PRDESCRI as DESCRICAO")
                    .append(",QUANTIDADE as QUANTIDADE")
                    .append(",VALOR_ITEM as VALOR")
                    .append(",SUM(VALOR_ITEM*QUANTIDADE) as TOTAL")
                    .append(",DATA  as DATA")
                    .append(" from espelho_comanda ")
                    .append(" inner join")
                    .append(" scea01 on(prrefere=referencia)")
                    .append(" where")
                    .append(" DATA_PRECONTA BETWEEN ").append("'").append(filtroVendaDetalhe.getDataInicial()).append(" 00:00:00' ")
                    .append(" AND '").append(filtroVendaDetalhe.getDataFinal()).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" AND VENDEDOR ='").append(filtroVendaDetalhe.getCargom()).append("'")
                    .append(" group by")
                    .append("    PEDIDO,REFERENCIA,PRDESCRI,QUANTIDADE,VALOR_ITEM,DATA")
                    .append(" order by PEDIDO");
            SQLQuery sQLQuery = session.createSQLQuery(sql.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(VendaDetalhe.class));
            return setResultTransformer.list();
        }
        return null;
    }

}
