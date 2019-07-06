package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroItemVendido;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.ItemPedido;
import modelo.dto.ItemVendido;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.ItemVendidoService;
import util.HibernateUtil;

@ManagedBean(name = "itemVendidoService")
@ViewScoped
public class ItemVendidoControler implements ItemVendidoService, Serializable {

    private Session session = null;
    private StringBuilder stringBuilder = null;

    @Override
    public List<ItemVendido> listarItensVendidos(FiltroVendaDetalhe vendaDetalhe) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT")
                    .append(" REFERENCIA AS REFERENCIA,")
                    .append("   PRDESCRI AS DESCRICAO,")
                    .append("   SUM(QUANTIDADE) AS QUANTIDADE,")
                    .append("   VALOR_ITEM AS VALOR,")
                    .append("   SUM(QUANTIDADE*VALOR_ITEM) AS TOTAL")
                    .append(" FROM")
                    .append("   ESPELHO_COMANDA ")
                    .append("   LEFT OUTER JOIN ")
                    .append("   SCEA01 ON (PRREFERE=REFERENCIA)")
                    .append(" WHERE")
                    .append("   DATA_PRECONTA")
                    .append(" BETWEEN '").append(vendaDetalhe.getDataInicial()).append(" 00:00:00' ").append(" AND '").append(vendaDetalhe.getDataFinal()).append(" 23:59:59' ")
                    .append(" AND STATUS ='P' ")
                    .append(" AND STATUS_ITEM ='N' ")
                    .append(" GROUP BY ")
                    .append("    REFERENCIA,PRDESCRI, VALOR_ITEM")
                    .append(" ORDER BY")
                    .append("   QUANTIDADE DESC");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(ItemVendido.class));
            List<ItemVendido> itemVendidos = query.list();
            session.close();
            return itemVendidos;
        }
        return null;
    }

    @Override
    public List<ItemPedido> buscarItensVendidos(FiltroItemVendido filtroItemVendido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT ")
                    .append(" MESA")
                    .append(",COMANDA")
                    .append(",PEDIDO")
                    .append(",DATA")
                    .append(",DATA_PRECONTA")
                    .append(",REFERENCIA")
                    .append(",PRDESCRI as DESCRICAO")
                    .append(",QUANTIDADE_LANCADA")
                    .append(",QUANTIDADE")
                    .append(",QUANTIDADE_CANCELADA")
                    .append(",VALOR_ITEM")
                    .append(",STATUS_ITEM")
                    .append(",VENDEDOR as GARCOM")
                    .append(",NOME as MOTIVO")
                    .append(",DATA_CANCELAMENTO")
                    .append(",RESPONSAVEL_CANCELAMENTO")
                    .append(" from")
                    .append("  espelho_comanda")
                    .append(" inner join")
                    .append("  scea01 on(prrefere=referencia)")
                    .append(" left outer join ")
                    .append("   MOTIVO_CANCELAMENTO on(codigo=MOTIVO_CANCELAMENTO) ")
                    .append(" where ")
                    .append(" DATA between '").append(filtroItemVendido.getDataInicial()).append(" 00:00:00' and '").append(filtroItemVendido.getDataFinal()).append(" 23:59:59'  ")
                    .append(" and (REFERENCIA = '").append(filtroItemVendido.getProduto()).append("'").append(" or PRDESCRI like '%").append(filtroItemVendido.getProduto()).append("%')  ")
                    .append(" order by  ")
                    .append("   PRDESCRI  ")
                    ;
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(ItemPedido.class));
            List<ItemPedido> itemPedido = query.list();
            session.close();
            return itemPedido;
        }
        return null;
    }

}
