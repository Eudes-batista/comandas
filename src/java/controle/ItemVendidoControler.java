package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroVendaDetalhe;
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
                    .append(" AND ")
                    .append("     STATUS_ITEM = 'N'")
                    .append(" AND STATUS ='P' ")
                    .append(" GROUP BY ")
                    .append("    REFERENCIA,PRDESCRI, VALOR_ITEM")
                    .append(" ORDER BY")
                    .append("   QUANTIDADE DESC");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(ItemVendido.class));
            return query.list();
        }
        return null;
    }

}
