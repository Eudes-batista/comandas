package controle;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.ClienteAtendido;
import modelo.dto.Couvert;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.TotalVenda;
import modelo.dto.VendaGarcom;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.DashboardService;
import util.HibernateUtil;

@ManagedBean(name = "dashboardService")
@ViewScoped
public class DashboardControle implements DashboardService, Serializable {

    private Session session = null;
    private StringBuilder stringBuilder = null;

    @Override
    public TotalVenda listarTotasVendasDoDia() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            String data = LocalDate.now().toString();
            stringBuilder.append("select ")
                    .append("sum(QUANTIDADE*VALOR_ITEM) as VENDAS")
                    .append(",SUM(QUANTIDADE) as ITENS")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" DATA_PRECONTA BETWEEN ").append("'").append(data).append(" 00:00:00' ")
                    .append(" AND '").append(data).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(TotalVenda.class));
            TotalVenda totalVenda = (TotalVenda) setResultTransformer.uniqueResult();
            session.close();
            return totalVenda;
        }
        return null;
    }

    @Override
    public List<ClienteAtendido> listarClientesAtendidos() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            String data = LocalDate.now().toString();
            stringBuilder.append("select ")
                    .append(" pedido as pedido")
                    .append(",pessoas_mesa as pessoas")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" DATA_PRECONTA BETWEEN ").append("'").append(data).append(" 00:00:00' ")
                    .append(" AND '").append(data).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" group by ")
                    .append(" pedido,pessoas_mesa ");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(ClienteAtendido.class));
            List<ClienteAtendido> clienteAtendidos = setResultTransformer.list();
            session.close();
            return clienteAtendidos;
        }
        return null;
    }

    @Override
    public List<ClienteAtendido> listarClientesEmAtendimento() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            String data = LocalDate.now().toString();
            stringBuilder.append("select ")
                    .append(" pedido as pedido")
                    .append(",pessoas_mesa as pessoas")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" DATA BETWEEN ").append("'").append(data).append(" 00:00:00' ")
                    .append(" AND '").append(data).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS =''")
                    .append(" group by ")
                    .append(" pedido,pessoas_mesa ");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(ClienteAtendido.class));
            List<ClienteAtendido> clienteAtendidos = setResultTransformer.list();
            session.close();
            return clienteAtendidos;
        }
        return null;        
    }
    
    @Override
    public List<VendaGarcom> listarVendasPorGarcom() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            String data = LocalDate.now().toString();
            stringBuilder.append("select ")
                    .append(" VENDEDOR as GARCOM,")
                    .append(" SUM(QUANTIDADE*VALOR_ITEM) AS VENDAS")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" DATA_PRECONTA BETWEEN ").append("'").append(data).append(" 00:00:00' ")
                    .append(" AND '").append(data).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" group by ")
                    .append(" VENDEDOR ");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(VendaGarcom.class));
            List<VendaGarcom> vendaGarcoms = setResultTransformer.list();
            session.close();
            return vendaGarcoms;
        }
        return null;
    }

    @Override
    public List<RejeicaoPorcentagemVendedor> listarRejeicaoPorcentagemVendedor() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            String data = LocalDate.now().toString();
            stringBuilder.append("select ")
                    .append(" PEDIDO as PEDIDO,")
                    .append(" VENDEDOR AS VENDEDOR")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" PORCENTAGEM <").append(10)
                    .append(" AND DATA_PRECONTA BETWEEN ").append("'").append(data).append(" 00:00:00' ")
                    .append(" AND '").append(data).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" group by ")
                    .append(" VENDEDOR,PEDIDO ");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(RejeicaoPorcentagemVendedor.class));
            List<RejeicaoPorcentagemVendedor> rejeicaoPorcentagemVendedors = setResultTransformer.list();
            session.close();
            return rejeicaoPorcentagemVendedors;
        }
        return null;

    }

    @Override
    public List<VendaGarcom> listarComissaoGarcom() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            String data = LocalDate.now().toString();
            stringBuilder.append("select ")
                    .append(" VENDEDOR as GARCOM,")
                    .append(" SUM(VALOR_PORCENTAGEM) AS VENDAS")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" DATA_PRECONTA BETWEEN ").append("'").append(data).append(" 00:00:00' ")
                    .append(" AND '").append(data).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" group by ")
                    .append(" VENDEDOR ORDER BY VENDAS ASC");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(VendaGarcom.class));
            List<VendaGarcom> vendaGarcoms = setResultTransformer.list();
            session.close();
            return vendaGarcoms;
        }
        return null;

    }

    @Override
    public Couvert listarTotalCouvert() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            String data = LocalDate.now().toString();
            stringBuilder.append("select ")
                    .append(" prdescri as DESCRICAO,")
                    .append(" sum(quantidade*valor_item) as VALOR")
                    .append(" from espelho_comanda ")
                    .append(" left outer join")
                    .append(" scea01")
                    .append(" on(prrefere=referencia)")
                    .append(" where")
                    .append(" prdescri like '%COUVERT%'")
                    .append(" AND DATA_PRECONTA BETWEEN ").append("'").append(data).append(" 00:00:00' ")
                    .append(" AND '").append(data).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" group by ")
                    .append(" prdescri");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(Couvert.class));
            Couvert couvert = (Couvert) setResultTransformer.uniqueResult();
            session.close();
            return couvert;
        }
        return null;         
    }

}
