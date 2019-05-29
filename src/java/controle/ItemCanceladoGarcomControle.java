package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroItemCancelado;
import modelo.dto.ItemCanceladoGarcom;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.ItemCanceladoGarcomService;
import util.HibernateUtil;

@ManagedBean(name = "itemCanceladoGarcomService")
@ViewScoped
public class ItemCanceladoGarcomControle implements ItemCanceladoGarcomService, Serializable {

    private Session session = null;
    private StringBuilder stringBuilder = null;

    @Override
    public List<ItemCanceladoGarcom> listarItensCanceladosPorGarcom(FiltroItemCancelado filtroItemCancelado) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            stringBuilder
                    .append("select ")
                    .append(" ''||NUMERO as NUMERO")
                    .append(",PEDIDO as PEDIDO")
                    .append(",DATA_CANCELAMENTO as DATA_CANCELAMENTO")
                    .append(",VENDEDOR as GARCOM")
                    .append(",SUM(QUANTIDADE_CANCELADA) as TOTAL")
                    .append(",QUANTIDADE_CANCELADA as CANCELAMENTO")
                    .append(",PRDESCRI as ITEM")
                    .append(",NOME as MOTIVO")
                    .append(",UPPER(OBSERVACAO_MOTIVO) as OBSERVACAO")
                    .append(",cast(foi_produzido as varchar(1)) as produzido")
                    .append(",UPPER(RESPONSAVEL_CANCELAMENTO) as RESPONSAVEL")
                    .append(",OBSERVACAO_DESTINO as DESTINO")
                    .append(" from")
                    .append(" espelho_comanda")
                    .append(" left outer join")
                    .append(" SCEA01").append(" on(prrefere=referencia)")
                    .append(" left outer join")
                    .append(" MOTIVO_CANCELAMENTO").append(" on(CODIGO=MOTIVO_CANCELAMENTO)")
                    .append(" where")
                    .append(" DATA BETWEEN ").append("'").append(filtroItemCancelado.getDataInical()).append(" 00:00:00' ")
                    .append(" AND '").append(filtroItemCancelado.getDataFinal()).append(" 23:59:59'")
                    .append(" AND (STATUS_ITEM ='C' OR STATUS_ITEM='M' OR STATUS_ITEM='D')")
                    .append(" group by")
                    .append("   VENDEDOR ,QUANTIDADE_CANCELADA ,PRDESCRI ,NOME ,OBSERVACAO_MOTIVO,PEDIDO,foi_produzido,RESPONSAVEL_CANCELAMENTO,NUMERO,OBSERVACAO_DESTINO,DATA_CANCELAMENTO");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(ItemCanceladoGarcom.class));
            List<ItemCanceladoGarcom> itemCanceladoGarcoms = setResultTransformer.list();
            session.close();
            return itemCanceladoGarcoms;
        }
        return null;
    }

    @Override
    public void atualizarObservacaoDestino(String numero, String observacao) {
        executarSql("update espelho_comanda set observacao_destino='" + observacao + "' where numero='" + numero + "'");
    }

    private void executarSql(String sql) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.createSQLQuery(sql).executeUpdate();
        session.getTransaction().commit();
        session.flush();
        session.close();
    }
}
