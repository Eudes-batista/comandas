package controle;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaDetalheRejeicao;
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
    public List<VendaGarcom> listarVendaGarcom() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            sql = new StringBuilder();
            String data = LocalDate.now().toString();
            sql.append("select ")
                    .append("COUNT(PEDIDO) as itens,")
                    .append(" SUM(QUANTIDADE*VALOR_ITEM) as vendas,")
                    .append(" VENDEDOR as garcom,")
                    .append(" SUM(VALOR_PORCENTAGEM) as COMISSAO")
                    .append(" from")
                    .append(" espelho_comanda")
                    .append(" where")
                    .append(" STATUS = 'P' AND STATUS_ITEM = 'N' AND")
                    .append(" DATA_PRECONTA BETWEEN '").append(data).append(" 00:00:00'").append(" and '").append(data).append(" 23:59:59'")
                    .append(" GROUP BY")
                    .append(" VENDEDOR");
            SQLQuery sQLQuery = session.createSQLQuery(sql.toString());
            Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(VendaGarcom.class));
            return query.list();
        }
        return null;
    }

    @Override
    public List<RejeicaoPorcentagemVendedor> listarReijeicaoPorcentagemPorVendedor(String vendedor, String dataIncial, String dataFinal) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            sql = new StringBuilder();
            sql.append("select ")
                    .append(" PEDIDO as PEDIDO")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" PORCENTAGEM <").append(10)
                    .append(" AND DATA BETWEEN ").append("'").append(dataIncial).append(" 00:00:00' ")
                    .append(" AND '").append(dataFinal).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" AND VENDEDOR ='").append(vendedor).append("'")
                    .append(" group by ")
                    .append(" PEDIDO ");
            SQLQuery sQLQuery = session.createSQLQuery(sql.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(RejeicaoPorcentagemVendedor.class));
            return setResultTransformer.list();
        }
        return null;
    }

    @Override
    public List<VendaDetalheRejeicao> listarRejeicaoDezPorcentoPorGarcom(String vendedor, String dataIncial, String dataFinal) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            sql = new StringBuilder();
            sql.append("select ")
                    .append(" PEDIDO as PEDIDO")
                    .append(",PRDESCRI as DESCRICAO")
                    .append(",VALOR_ITEM as VALOR")
                    .append(",CAST(PORCENTAGEM as DECIMAL(6,2)) || '%' as PORCENTAGEM")
                    .append(",'R$ ' || CAST(VALOR_PORCENTAGEM as DECIMAL(6,2))  VALOR_PORCENTAGEM")
                    .append(",DATA  as DATA")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" PORCENTAGEM <").append(10)
                    .append(" AND DATA_PRECONTA BETWEEN ").append("'").append(dataIncial).append(" 00:00:00' ")
                    .append(" AND '").append(dataFinal).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" AND VENDEDOR ='").append(vendedor).append("'");
            SQLQuery sQLQuery = session.createSQLQuery(sql.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(VendaDetalheRejeicao.class));
            return setResultTransformer.list();
        }
        return null;
    }

}
