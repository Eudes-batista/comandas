package controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Mesa;
import org.hibernate.Session;
import servico.MesaService;
import util.GerenciaArquivo;
import util.HibernateUtil;

@ManagedBean(name = "mesaService")
@ViewScoped
public class MesaControle implements MesaService, Serializable {

    private final GerenciaArquivo gerenciarArquivoService = new GerenciaArquivo();

    @Override
    public List<Mesa> listarMesas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Mesa> lista=null;
        if (session != null) {
            List<Object[]> list = (List<Object[]>) session.createSQLQuery("select tecdmesa,testatus,tepedido from sosa98 where tecdmesa is not null group by tecdmesa,testatus,tepedido order by tecdmesa,testatus desc").list();
            lista = seperarPedidosPorMesa(list);
            session.close();
        }
        return lista;
    }

    @Override
    public Object somarTotal(String mesa) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Object lista = null;
        if (session != null) {
            lista = session.createSQLQuery("select sum(EEPLQTB1*TEQUANTI) from sosa98 inner join scea07 on(eerefere=terefere and eecodemp='" + gerenciarArquivoService.bucarInformacoes().getConfiguracao().getEmpresa() + "') where tecdmesa='" + mesa + "'").uniqueResult();
            session.close();
        }
        return lista;
    }

    @Override
    public List<Object[]> listarComandasPorMesa(String mesa) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> lista = null;
        if (session != null) {
            lista = session.createSQLQuery("select TECOMAND,TEREFERE,PRDESCRI,TEQUANTI,EEPLQTB1,coalesce((TEQUANTI*EEPLQTB1),0),TEOBSERV from sosa98 inner join scea07 on(eerefere=terefere) inner join scea01 on(prrefere=eerefere) where TECDMESA='" + mesa + "' order by TECOMAND;").list();
            session.close();
        }
        return lista;
    }

    @Override
    public void excluirMesa(String mesa) {
        executarSql("delete from Sosa98 where tecdmesa='" + mesa + "'");
    }

    @Override
    public void transferirMesa(Mesa mesaOrigem, String mesaDestino) {
        executarSql("update sosa98 set tecdmesa='" + mesaDestino.toUpperCase() + "' where tecdmesa='" + mesaOrigem.getMesa() + "'");
        executarSql("update espelho_comanda set mesa='" + mesaDestino.toUpperCase() + "' where pedido in(" + mesaOrigem.getPedido()+ ")");
    }

    @Override
    public List<Mesa> listarMesas(String mesa) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Mesa> lista = null;
        if (session != null) {
            List<Object[]> list = (List<Object[]>) session.createSQLQuery("select tecdmesa,testatus,tepedido from sosa98 where tecdmesa is not null and tecdmesa like '%" + mesa + "%' order by tecdmesa,testatus,tepedido desc").list();
            lista = seperarPedidosPorMesa(list);
            session.close();
        }
        return lista;
    }

    @Override
    public void atualizarStatusPreconta(Mesa mesa) {
        executarSql("update sosa98 set testatus='P' where tecdmesa='" + mesa.getMesa() + "'");
        executarSql("update espelho_comanda set status='P' where pedido in("+mesa.getPedido()+")");
    }

    private void executarSql(String sql) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.createSQLQuery(sql).executeUpdate();
        session.getTransaction().commit();
        session.flush();
        session.close();
    }

    @Override
    public List<Object[]> listarLancamentos(String mesa) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
                .append("Tenumero as numero,Tenumseq as item,")
                .append("TECOMAND as comanda,TEREFERE as referencia,")
                .append("PRDESCRI as descricao,TEQUANTI as quantidade,")
                .append("EEPLQTB1 as preco,coalesce((TEQUANTI*EEPLQTB1),0) as precoTotal,")
                .append("TEVENDED as vendedor,TEOBSERV as observacao,TECDMESA as mesa ,TEIMPRIM as imprimir,TESTATUS as status ")
                .append("from  sosa98 ")
                .append("inner join scea07 on(eerefere=terefere) inner join scea01 on(prrefere=eerefere) ")
                .append("where ")
                .append("TECDMESA='").append(mesa).append("' ")
                .append("group by ")
                .append("Tenumero,Tenumseq,")
                .append("TECOMAND,TEREFERE,")
                .append("PRDESCRI,TEQUANTI,")
                .append("EEPLQTB1,coalesce((TEQUANTI*EEPLQTB1),0),")
                .append("TEVENDED,TEOBSERV,TECDMESA,TEIMPRIM,TESTATUS ")
                .append(" order by TECOMAND;");
        List<Object[]> lista = (List<Object[]>) session.createSQLQuery(sb.toString()).list();
        session.close();
        return lista;

    }

    private List<Mesa> seperarPedidosPorMesa(List<Object[]> mesas) {
        List<Mesa> listaMesas=new ArrayList<>();
        Map<String, List<Object[]>> mapMesa = mesas.stream().collect(Collectors.groupingBy(m -> String.valueOf(m[0])));
        mesas.forEach(m -> {
            String mesa = String.valueOf(m[0]);
            List<Object[]> listpedidos = mapMesa.get(mesa);
            String pedidos = listpedidos.stream().map(b -> String.valueOf(b[2])).collect(Collectors.joining(","));
            listaMesas.add(new Mesa(mesa, String.valueOf(m[1]), pedidos));
        });
        return listaMesas;
    }

}
