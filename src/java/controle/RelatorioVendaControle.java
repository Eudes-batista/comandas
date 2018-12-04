package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaGarcom;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.RelatorioVendaService;
import util.HibernateUtil;

@ManagedBean(name = "relatorioVendaService")
@ViewScoped
public class RelatorioVendaControle implements RelatorioVendaService, Serializable {

    private Session session = null;
    private StringBuilder stringBuilder = null;

    @Override
    public List<VendaGarcom> listarVendasGarcom(FiltroVendaDetalhe filtroVendaDetalhe) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("select ")
                    .append("  VENDEDOR as GARCOM")
                    .append(" ,SUM(QUANTIDADE) as ITENS")
                    .append(" ,SUM(VALOR_PORCENTAGEM) as COMISSAO")
                    .append(" ,SUM(QUANTIDADE*VALOR_ITEM) as VENDAS")
                    .append(" from")
                    .append(" espelho_comanda")
                    .append(" where")
                    .append(" DATA_PRECONTA BETWEEN '").append(filtroVendaDetalhe.getDataInicial()).append(" 00:00:00'")
                    .append(" AND '").append(filtroVendaDetalhe.getDataFinal()).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM='N' AND STATUS='P'")
                    .append(" group by VENDEDOR")
                    .append(" order by VENDAS DESC");
            Query query = session.createSQLQuery(stringBuilder.toString()).setResultTransformer(Transformers.aliasToBean(VendaGarcom.class));
            return query.list();
        }
        return null;
    }

    @Override
    public List<RejeicaoPorcentagemVendedor> listarRejeicoesPorGarcom(FiltroVendaDetalhe filtroVendaDetalhe) {
        return null;
    }

}
