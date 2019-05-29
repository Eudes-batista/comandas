package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroItemVendido;
import modelo.dto.ItemCanceladoGarcom;
import modelo.dto.ItemVendido;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.RelatorioItemVendidoService;
import util.HibernateUtil;

@ManagedBean(name = "relatorioItemVendidoService")
@ViewScoped
public class RelatorioItemVendidoControler implements RelatorioItemVendidoService, Serializable {

    private Session session = null;
    private StringBuilder stringBuilder = null;

    @Override
    public List<ItemVendido> listaItensVendidos(FiltroItemVendido filtroItemVendido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            String grupo = filtroItemVendido.getGrupo() == null ? "" : filtroItemVendido.getGrupo().getT51cdgrp();
            stringBuilder.append("select ")
                    .append("REFERENCIA as REFERENCIA ")
                    .append("   ,PRDESCRI as DESCRICAO")
                    .append("   ,SUM(QUANTIDADE) as QUANTIDADE")
                    .append("   ,VALOR_ITEM as VALOR")
                    .append("   ,SUM(QUANTIDADE*VALOR_ITEM) as TOTAL")
                    .append("   from")
                    .append("   espelho_comanda ")
                    .append("   left outer join ")
                    .append("   scea01 ")
                    .append("on(prrefere=referencia) ")
                    .append("left outer join ")
                    .append("   LAPT51 ")
                    .append("on(T51CDGRP=PRCGRUPO)")
                    .append("where    ")
                    .append("     DATA_PRECONTA BETWEEN '").append(filtroItemVendido.getDataInicial()).append(" 00:00:00' AND '").append(filtroItemVendido.getDataFinal()).append(" 23:59:59' ")
                    .append("     AND ( (REFERENCIA >= '").append(filtroItemVendido.getProduto()).append("' AND REFERENCIA <= '").append(filtroItemVendido.getProdutoFinal()).append("' OR ")
                    .append(" REFERENCIA like '%").append(filtroItemVendido.getProduto()).append("%') ")
                    .append(" AND PRCGRUPO like '%").append(grupo).append("%' AND  ")
                    .append(" VENDEDOR like '%").append(filtroItemVendido.getGarcom()).append("%' )")
                    .append(" AND STATUS_ITEM='N'")
                    .append("     GROUP BY ")
                    .append("     REFERENCIA,PRDESCRI,VALOR_ITEM")
                    .append("     ORDER BY QUANTIDADE DESC");
            Query query = session.createSQLQuery(stringBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ItemVendido.class));
            List<ItemVendido> itemVendidos = query.list();
            session.close();
            return itemVendidos;
        }
        return null;
    }
    
    @Override
    public List<ItemCanceladoGarcom> listarItensCanceladosPorGarcom(FiltroItemVendido filtroItemVendido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            stringBuilder = new StringBuilder();
            String grupo = filtroItemVendido.getGrupo() == null ? "" : filtroItemVendido.getGrupo().getT51cdgrp();
            stringBuilder
                    .append("select ")
                    .append(" ''||NUMERO as NUMERO")
                    .append(",PEDIDO as PEDIDO")
                    .append(",VENDEDOR as GARCOM")
                    .append(",SUM(QUANTIDADE_CANCELADA*VALOR_ITEM) as TOTAL")
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
                    .append(" DATA BETWEEN ").append("'").append(filtroItemVendido.getDataInicial()).append(" 00:00:00' ")
                    .append(" AND '").append(filtroItemVendido.getDataFinal()).append(" 23:59:59'")
                    .append(" AND (REFERENCIA like '%").append(filtroItemVendido.getProduto()).append("%' AND  ").append(" PRCGRUPO like '%").append(grupo).append("%' AND  ").append(" VENDEDOR like '%").append(filtroItemVendido.getGarcom()).append("%' )")
                    .append(" AND (STATUS_ITEM ='C' OR STATUS_ITEM='M' OR STATUS_ITEM='D')")
                    .append(" group by")
                    .append("   VENDEDOR ,QUANTIDADE_CANCELADA ,PRDESCRI ,NOME ,OBSERVACAO_MOTIVO,PEDIDO,foi_produzido,RESPONSAVEL_CANCELAMENTO,NUMERO,OBSERVACAO_DESTINO");
            SQLQuery sQLQuery = session.createSQLQuery(stringBuilder.toString());
            Query setResultTransformer = sQLQuery.setResultTransformer(Transformers.aliasToBean(ItemCanceladoGarcom.class));
            List<ItemCanceladoGarcom> itemCanceladoGarcoms = setResultTransformer.list();
            session.close();
            return itemCanceladoGarcoms;
        }
        return null;
    }

}
