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
                    .append("select \n")
                    .append(" ''||NUMERO as NUMERO \n")
                    .append(",PEDIDO as PEDIDO \n")
                    .append(",MESA \n")
                    .append(",COMANDA \n")
                    .append(",DATA as DATA_CANCELAMENTO \n")
                    .append(",GARCOM as GARCOM \n")
                    .append(",QUANTIDADE as CANCELAMENTO \n")
                    .append(",PRDESCRI as ITEM \n")
                    .append(",NOME as MOTIVO \n")
                    .append(",UPPER(OBSERVACAO_MOTIVO) as OBSERVACAO \n")
                    .append(",cast(foi_produzido as varchar(1)) as produzido \n")
                    .append(",UPPER(RESPONSAVEL) as RESPONSAVEL \n")
                    .append(",OBSERVACAO_DESTINO as DESTINO \n")
                    .append(" from \n")
                    .append(" cancelamento_mesa \n")
                    .append(" left outer join \n")
                    .append(" SCEA01").append(" on(prrefere=produto) \n")
                    .append(" left outer join \n")
                    .append(" MOTIVO_CANCELAMENTO").append(" on(CODIGO=CODIGO_MOTIVO) \n")
                    .append(" where \n")
                    .append(" DATA BETWEEN ").append("'").append(filtroItemCancelado.getDataInical()).append(" 00:00:00'  \n")
                    .append(" AND '").append(filtroItemCancelado.getDataFinal()).append(" 23:59:59' \n")
                    .append(" group by \n")
                    .append("   GARCOM,QUANTIDADE,PRDESCRI,NOME,OBSERVACAO_MOTIVO,PEDIDO,foi_produzido,RESPONSAVEL,NUMERO,OBSERVACAO_DESTINO,DATA,MESA,COMANDA");
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
        executarSql("update cancelamento_mesa set observacao_destino='" + observacao + "' where numero='" + numero + "'");
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
