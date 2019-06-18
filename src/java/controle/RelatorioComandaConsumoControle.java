package controle;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroRelatorioPreconta;
import modelo.dto.RelatorioComandaConsumo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.RelatorioComandaConsumoService;
import util.HibernateUtil;

@ManagedBean(name = "relatorioComandaConsumoService")
@ViewScoped
public class RelatorioComandaConsumoControle implements RelatorioComandaConsumoService {

    @Override
    public List<RelatorioComandaConsumo> listarComandasEmConsumo(FiltroRelatorioPreconta filtroRelatorioPreconta) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
                .append("MESA as MESA")
                .append(",COMANDA as COMANDA")
                .append(",PEDIDO as PEDIDO")
                .append(",iif(STATUS <> 'P','EM CONSUMO','PRECONTA') as STATUS")
                .append(",sum(quantidade*valor_item) TOTAL")
                .append(",iif(responsavel_parcial <> '' and responsavel_parcial is null , responsavel_parcial, 'SEM RESPONSAVEL') as PARCIAL")
                .append(" from  espelho_comanda ")
                .append("where ")
                .append("    status <> 'P' ")
                .append(" and (pedido like '%").append(filtroRelatorioPreconta.getPedido()).append("%' ")
                .append(" or mesa like '%").append(filtroRelatorioPreconta.getMesa()).append("%' ")
                .append(" or comanda like '%").append(filtroRelatorioPreconta.getComanda()).append("%') ")
                .append(" and data between '").append(filtroRelatorioPreconta.getDataInicial()).append(" 00:00:00' ")
                .append(" and '").append(filtroRelatorioPreconta.getDataFinal()).append(" 23:59:59' ")
                .append(" group by MESA,COMANDA,PEDIDO,STATUS,responsavel_parcial");
        Query query = session.createSQLQuery(sb.toString()).setResultTransformer(Transformers.aliasToBean(RelatorioComandaConsumo.class));
        List<RelatorioComandaConsumo> comandasConsumo = query.list();
        session.close();
        return comandasConsumo;
    }

}
