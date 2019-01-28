package controle;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Comandas;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import servico.PesquisaMesasService;
import util.GerenciaArquivo;
import util.HibernateUtil;

@ManagedBean(name = "pesquisaMesasService")
@ViewScoped
public class PesquisaMesasControle implements PesquisaMesasService, Serializable {

    private Session session = null;
    private StringBuilder sb = null;
    private final GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();

    @Override
    public List<Comandas> listarComandas(String dataInicial,String dataFinal) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("COMANDA AS COMANDA,sum(QUANTIDADE*VALOR_ITEM) as TOTAL,STATUS as STATUS,MESA as MESA,pessoas_mesa as PESSOAS,PEDIDO as PEDIDO,prdescri as DESCRICAO,porcentagem as PORCENTAGEM,DATA_PRECONTA  ")
                .append("from  espelho_comanda ")
                .append(" inner join scea07 on(eerefere=referencia and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append(" inner join scea01 on(prrefere=eerefere)")
                .append(" where DATA_PRECONTA BETWEEN '").append(dataInicial).append(" 00:00:00' ").append(" and '").append(dataFinal).append(" 23:59:59'")
                .append(" group by COMANDA,STATUS,mesa,pessoas_mesa,pedido,prdescri,porcentagem,DATA_PRECONTA ")
                .append(" order by STATUS desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
        Map<Date, Comandas> separarPedidos = separarPedidos(lista);
        lista.clear();
        for (Map.Entry<Date, Comandas> entry : separarPedidos.entrySet()) {
            Comandas value = entry.getValue();
            lista.add(value);
        }
        session.close();
        return lista;
    }

    private Map<Date, Comandas> separarPedidos(List<Comandas> lista) {
        Map<Date, Comandas> mapAuditoria = new HashMap<>();
        for (Comandas comandas : lista) {
            Date chave = comandas.getDATA_PRECONTA();
            Comandas comanda = mapAuditoria.get(chave);
            double total = 0;
            if (comanda == null) {
                comanda = comandas;
                total = comanda.getTOTAL();
            } else {
                total += comanda.getTOTAL()+comandas.getTOTAL();
            }
            if (!comandas.getDESCRICAO().contains("COUVERT")) {
                comanda.setDESCRICAO("");
            }
            comanda.setTOTAL(total);
            mapAuditoria.put(chave, comanda);
        }
        return mapAuditoria;
    }

    @Override
    public List<Comandas> pesquisarComandaPorCodigo(String codigo,String dataInicial,String dataFinal) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("COMANDA AS COMANDA,sum(QUANTIDADE*VALOR_ITEM) as TOTAL,STATUS as STATUS,MESA as MESA,pessoas_mesa as PESSOAS,PEDIDO as PEDIDO,prdescri as DESCRICAO,porcentagem as PORCENTAGEM,DATA_PRECONTA  ")
                .append(" from  espelho_comanda ")
                .append(" inner join scea07 on(eerefere=referencia and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append(" inner join scea01 on(prrefere=eerefere)")
                .append(" where ")
                .append(" tecomand like '%").append(codigo).append("%' AND ")
                .append(" DATA_PRECONTA BETWEEN '").append(dataInicial).append(" 00:00:00' ").append(" and '").append(dataFinal).append(" 23:59:59'")
                .append(" group by COMANDA,STATUS,MESA,pessoas_mesa,pedido,prdescri,porcentagem,DATA_PRECONTA ")
                .append(" order by STATUS desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
        Map<Date, Comandas> separarPedidos = separarPedidos(lista);
        lista.clear();
        for (Map.Entry<Date, Comandas> entry : separarPedidos.entrySet()) {
            Comandas value = entry.getValue();
            lista.add(value);
        }
        session.close();
        return lista;
    }

}
