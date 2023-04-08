package controle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Comandas;
import modelo.dto.ItemPedido;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.PesquisaMesasService;
import util.GerenciaArquivo;
import util.HibernateUtil;
import util.JuntaComandas;

@ManagedBean(name = "pesquisaMesasService")
@ViewScoped
public class PesquisaMesasControle implements PesquisaMesasService, Serializable {

    private Session session = null;
    private StringBuilder sb = null;
    private final GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();

    @Override
    public List<Comandas> listarComandas(String dataInicial, String dataFinal) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("COMANDA AS COMANDA,sum(QUANTIDADE*VALOR_ITEM) as TOTAL,STATUS as STATUS,MESA as MESA,pessoas_mesa as PESSOAS,PEDIDO as PEDIDO,prdescri as DESCRICAO,porcentagem as PORCENTAGEM,DATA_PRECONTA  ")
                .append("from  espelho_comanda ")
                .append(" inner join scea07 on(eerefere=referencia and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append(" inner join scea01 on(prrefere=eerefere)")
                .append(" where DATA_PRECONTA BETWEEN '").append(dataInicial).append(" 00:00:00' ").append(" and '").append(dataFinal).append(" 23:59:59'")
                .append(" and status_item='N'")
                .append(" group by COMANDA,STATUS,mesa,pessoas_mesa,pedido,prdescri,porcentagem,DATA_PRECONTA ")
                .append(" order by STATUS desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
        Map<String, Comandas> separarPedidos = JuntaComandas.juntarPorPedidoPreconta(lista);
        lista.clear();
        for (Map.Entry<String, Comandas> entry : separarPedidos.entrySet()) {
            Comandas value = entry.getValue();
            lista.add(value);
        }
        session.close();
        return lista;
    }

    @Override
    public List<Comandas> pesquisarComandaPorCodigo(String codigo, String dataInicial, String dataFinal) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("COMANDA AS COMANDA,sum(QUANTIDADE*VALOR_ITEM) as TOTAL,STATUS as STATUS,MESA as MESA,pessoas_mesa as PESSOAS,PEDIDO as PEDIDO,prdescri as DESCRICAO,porcentagem as PORCENTAGEM,DATA_PRECONTA  ")
                .append(" from  espelho_comanda ")
                .append(" inner join scea07 on(eerefere=referencia and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append(" inner join scea01 on(prrefere=eerefere)")
                .append(" where ")
                .append(" COMANDA like '%").append(codigo).append("%'")
                .append(" AND DATA_PRECONTA BETWEEN '").append(dataInicial).append(" 00:00:00' ").append(" and '").append(dataFinal).append(" 23:59:59'")
                .append(" and status_item='N'")
                .append(" group by COMANDA,STATUS,MESA,pessoas_mesa,pedido,prdescri,porcentagem,DATA_PRECONTA ")
                .append(" order by STATUS desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
        Map<String, Comandas> separarPedidos = JuntaComandas.juntarPorPedidoPreconta(lista);
        lista.clear();
        for (Map.Entry<String, Comandas> entry : separarPedidos.entrySet()) {
            Comandas value = entry.getValue();
            lista.add(value);
        }
        session.close();
        return lista;
    }

    @Override
    public List<ItemPedido> listarItemPorPedido(String pedido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            sb = new StringBuilder();
            sb.append("select ")
                    .append(" PEDIDO as PEDIDO")
                    .append(",MESA as MESA")
                    .append(",COMANDA as COMANDA")
                    .append(",DATA as DATA")
                    .append(",REFERENCIA as REFERENCIA")
                    .append(",PRDESCRI as DESCRICAO")
                    .append(",iif(FOI_PRODUZIDO is null,'NAO','SIM') as FOI_PRODUZIDO")
                    .append(",iif(IMPRESSAO ='1','SIM','NAO') as IMPRESSAO")
                    .append(",iif(STATUS_ITEM ='N','NORNAL','CANCELADO') as STATUS_ITEM")
                    .append(",QUANTIDADE as QUANTIDADE")
                    .append(",QUANTIDADE_LANCADA as QUANTIDADE_LANCADA")
                    .append(",QUANTIDADE_CANCELADA as QUANTIDADE_CANCELADA")
                    .append(",VALOR_ITEM as VALOR_ITEM")
                    .append(",(QUANTIDADE*VALOR_ITEM) as TOTAL")
                    .append(",VENDEDOR as GARCOM")
                    .append(",DATA_CANCELAMENTO as DATA_CANCELAMENTO")
                    .append(" from")
                    .append("  espelho_comanda")
                    .append(" left outer join")
                    .append("  SCEA01 on(PRREFERE=REFERENCIA)")
                    .append(" where ")
                    .append("  PEDIDO='").append(pedido).append("'");
            SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
            List<ItemPedido> itemPedidos = sQLQuery.setResultTransformer(Transformers.aliasToBean(ItemPedido.class)).list();
            session.close();
            return itemPedidos;
        }
        return null;
    }

}
