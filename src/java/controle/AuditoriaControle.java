package controle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Comandas;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.AuditoriaService;
import util.GerenciaArquivo;
import util.HibernateUtil;
import util.JuntaComandas;

@ManagedBean(name = "auditoriaService")
@ViewScoped
public class AuditoriaControle implements AuditoriaService, Serializable {

    private Session session = null;
    private StringBuilder sb = null;
    private final GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();

    @Override
    public List<Comandas> listarComandas() {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand AS COMANDA,sum(tequanti*EEPLQTB1) as TOTAL,testatus as STATUS,tecdmesa as MESA,pessoas_mesa as PESSOAS,tepedido as PEDIDO,prdescri as DESCRICAO,porcentagem as PORCENTAGEM ")
                .append("from  sosa98 ")
                .append(" inner join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append(" inner join espelho_comanda on(numero=tenumero)")
                .append(" inner join scea01 on(prrefere=eerefere)")
                .append(" where status_item='N' ")
                .append(" group by tecomand,testatus,tecdmesa,pessoas_mesa,tepedido,prdescri,porcentagem")
                .append(" order by testatus desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
        Map<String, Comandas> separarPedidos = JuntaComandas.juntarPedidosPorComanda(lista);
        lista.clear();
        for (Map.Entry<String, Comandas> entry : separarPedidos.entrySet()) {
            Comandas value = entry.getValue();
            lista.add(value);
        }
        session.close();
        return lista;
    }

    @Override
    public List<Comandas> pesquisarComandaPorCodigo(String codigo) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand as COMANDA,sum(tequanti*EEPLQTB1) as TOTAL,testatus as STATUS,tecdmesa as MESA,pessoas_mesa as PESSOAS,tepedido as PEDIDO,prdescri as DESCRICAO,porcentagem as PORCENTAGEM ")
                .append(" from  sosa98 ")
                .append(" inner join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append(" inner join espelho_comanda on(numero=tenumero) ")
                .append(" inner join scea01 on(prrefere=eerefere)")
                .append(" where ")
                .append(" tecomand like '%").append(codigo).append("%' ")
                .append(" and status_item='N'")
                .append(" group by tecomand,testatus,tecdmesa,pessoas_mesa,tepedido,prdescri,porcentagem")
                .append(" order by testatus desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
        Map<String, Comandas> separarPedidos = JuntaComandas.juntarPedidosPorComanda(lista);
        lista.clear();
        for (Map.Entry<String, Comandas> entry : separarPedidos.entrySet()) {
            Comandas value = entry.getValue();
            lista.add(value);
        }
        session.close();
        return lista;
    }

}
