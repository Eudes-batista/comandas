package controle;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.FiltroItemVendido;
import modelo.dto.ItemVendido;
import org.hibernate.Query;
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
                    .append("     AND (REFERENCIA like '%").append(filtroItemVendido.getProduto()).append("%' AND  ").append(" PRCGRUPO like '%").append(grupo).append("%' AND  ").append(" VENDEDOR like '%").append(filtroItemVendido.getGarcom()).append("%' )")
                    .append("     GROUP BY ")
                    .append("     REFERENCIA,PRDESCRI,VALOR_ITEM")
                    .append("     ORDER BY QUANTIDADE DESC");
            Query query = session.createSQLQuery(stringBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ItemVendido.class));
            return query.list();
        }
        return null;
    }

}
