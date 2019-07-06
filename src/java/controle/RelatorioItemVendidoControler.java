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
                    .append("    REFERENCIA as REFERENCIA ")
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
                    .append("     STATUS_ITEM ='N' ")
                    .append(" AND DATA_PRECONTA BETWEEN '").append(filtroItemVendido.getDataInicial()).append(" 00:00:00' AND '").append(filtroItemVendido.getDataFinal()).append(" 23:59:59' ")
                    .append("     AND ( (REFERENCIA >= '").append(filtroItemVendido.getProduto()).append("' AND REFERENCIA <= '").append(filtroItemVendido.getProdutoFinal()).append("' OR ")
                    .append(" REFERENCIA like '%").append(filtroItemVendido.getProduto()).append("%') ")
                    .append(" AND PRCGRUPO like '%").append(grupo).append("%' AND  ")
                    .append(" VENDEDOR like '%").append(filtroItemVendido.getGarcom()).append("%' )")
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
                    .append(" DATA BETWEEN ").append("'").append(filtroItemVendido.getDataInicial()).append(" 00:00:00'  \n")
                    .append(" AND '").append(filtroItemVendido.getDataFinal()).append(" 23:59:59' \n")
                    .append(" AND (REFERENCIA like '%").append(filtroItemVendido.getProduto()).append("%' AND  ").append(" PRCGRUPO like '%").append(grupo).append("%' AND  ").append(" VENDEDOR like '%").append(filtroItemVendido.getGarcom()).append("%' ) \n")
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

}
