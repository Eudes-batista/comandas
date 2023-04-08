package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroRelatorioPreconta;
import modelo.dto.ItemVendido;
import modelo.dto.RelatorioPreconta;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.RelatorioPrecontaService;
import util.HibernateUtil;

@ManagedBean(name = "relatorioPrecontaService")
@ViewScoped
public class RelatorioPrecontaControle implements RelatorioPrecontaService, Serializable {

    private Session session = null;

    @Override
    public List<RelatorioPreconta> listarTudo(FiltroRelatorioPreconta filtroRelatorioPreconta) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            String sql = "select \n"
                    + "   PEDIDO, \n"
                    + "   RESPONSAVEL_TRANSFERENCIA,    \n"
                    + "   RESPONSAVEL_PARCIAL,\n"
                    + "   RESPONSAVEL_PRECONTA,\n"
                    + "   MESA, \n"
                    + "   COMANDA, \n"
                    + "   DATA_PRECONTA,\n"
                    + "   MESA_ORIGEM,\n"
                    + "   RESPONSAVEL_REABRIU_MESA,\n"
                    + "   MESA_REABERTA\n"
                    + "from \n"
                    + "   espelho_comanda \n"
                    + "where \n"
                    + "data\n"
                    + "   between '" + filtroRelatorioPreconta.getDataInicial() + " 00:00:00' and '" + filtroRelatorioPreconta.getDataFinal() + " 23:59:59'\n"
                    + "   AND PEDIDO LIKE '%" + filtroRelatorioPreconta.getPedido() + "%'\n"
                    + "   AND VENDEDOR LIKE '%" + filtroRelatorioPreconta.getVendedor() + "%'\n"
                    + "   AND MESA LIKE '%" + filtroRelatorioPreconta.getMesa() + "%'\n"
                    + "   AND COMANDA LIKE '%" + filtroRelatorioPreconta.getComanda() + "%'\n"
                    + "GROUP BY\n"
                    + "   PEDIDO, \n"
                    + "   RESPONSAVEL_TRANSFERENCIA, \n"
                    + "   RESPONSAVEL_PARCIAL,\n"
                    + "   RESPONSAVEL_PRECONTA, \n"
                    + "   MESA,\n"
                    + "   COMANDA,\n"
                    + "   DATA_PRECONTA,\n"
                    + "   MESA_ORIGEM,\n"
                    + "   RESPONSAVEL_REABRIU_MESA,\n"
                    + "   MESA_REABERTA\n"
                    ;
            List<RelatorioPreconta> relatorioPrecontas = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(RelatorioPreconta.class)).list();
            session.close();
            return relatorioPrecontas;
        }
        return null;
    }

    @Override
    public List<ItemVendido> listarItensVendidos(FiltroRelatorioPreconta filtroRelatorioPreconta) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            StringBuilder stringBuilder = new StringBuilder();
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
                    .append(" BETWEEN '").append(filtroRelatorioPreconta.getDataInicial()).append(" 00:00:00' ").append(" AND '").append(filtroRelatorioPreconta.getDataFinal()).append(" 23:59:59' ")
                    .append(" AND  PEDIDO = '").append(filtroRelatorioPreconta.getPedido()).append("'")
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

}
