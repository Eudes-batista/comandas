package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroRelatorioPreconta;
import modelo.dto.RelatorioPreconta;
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
                    + "   PEDIDO,   \n"
                    + "   RESPONSAVEL_TRANSFERENCIA,    \n"
                    + "   RESPONSAVEL_PARCIAL,\n"
                    + "   RESPONSAVEL_PRECONTA,\n"
                    + "   MESA, \n"
                    + "   COMANDA, \n"
                    + "   DATA_PRECONTA\n"
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
                    + "PEDIDO,   \n"
                    + "   RESPONSAVEL_TRANSFERENCIA,    \n"
                    + "   RESPONSAVEL_PARCIAL,\n"
                    + "   RESPONSAVEL_PRECONTA,   \n"
                    + "   MESA,\n"
                    + "   COMANDA, DATA_PRECONTA;";
            return session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(RelatorioPreconta.class)).list();
        }
        return null;
    }

}
