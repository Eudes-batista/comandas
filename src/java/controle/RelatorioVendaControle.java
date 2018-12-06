package controle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.ClienteAtendido;
import modelo.dto.FiltroVendaDetalhe;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaGarcom;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
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
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("select ")
                    .append(" PEDIDO as PEDIDO,")
                    .append(" VENDEDOR AS VENDEDOR")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" PORCENTAGEM <").append(10)
                    .append(" AND DATA_PRECONTA BETWEEN ").append("'").append(filtroVendaDetalhe.getDataInicial()).append(" 00:00:00' ")
                    .append(" AND '").append(filtroVendaDetalhe.getDataFinal()).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" group by ")
                    .append(" VENDEDOR,PEDIDO ");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(RejeicaoPorcentagemVendedor.class));
            List<RejeicaoPorcentagemVendedor> lista = setResultTransformer.list();
            List<RejeicaoPorcentagemVendedor> rejeicaoPorcentagemVendedores = lista;
            Map<String, Integer> mapa = new HashMap<>();
            rejeicaoPorcentagemVendedores.forEach((rejeicaoPorcentagemVendedor) -> {
                Integer qtd = mapa.get(rejeicaoPorcentagemVendedor.getVENDEDOR());
                if (qtd == null) {
                    qtd = 0;
                }
                qtd++;
                mapa.put(rejeicaoPorcentagemVendedor.getVENDEDOR(), qtd);
            });
            lista.clear();
            mapa.forEach((chave,valor) -> lista.add(new RejeicaoPorcentagemVendedor("", chave, valor)));
            return lista;
        }
        return null;
    }

    @Override
    public List<ClienteAtendido> listarClientesAtendidos(FiltroVendaDetalhe filtroVendaDetalhe) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("select ")
                    .append(" pedido as pedido")
                    .append(",pessoas_mesa as pessoas")
                    .append(" from espelho_comanda ")
                    .append(" where")
                    .append(" DATA_PRECONTA BETWEEN ").append("'").append(filtroVendaDetalhe.getDataInicial()).append(" 00:00:00' ")
                    .append(" AND '").append(filtroVendaDetalhe.getDataFinal()).append(" 23:59:59'")
                    .append(" AND STATUS_ITEM ='").append("N").append("'")
                    .append(" AND STATUS ='").append("P").append("'")
                    .append(" group by ")
                    .append(" pedido,pessoas_mesa ");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(ClienteAtendido.class));
            return setResultTransformer.list();
        }
        return null;
    }

}
