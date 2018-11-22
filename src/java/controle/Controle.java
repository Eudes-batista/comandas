package controle;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Comandas;
import modelo.Lancamento;
import modelo.Sosa98;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.omnifaces.util.Messages;
import servico.ComandaService;
import util.GerenciaArquivo;
import util.HibernateUtil;

@ManagedBean(name = "controle")
@ViewScoped
public class Controle implements ComandaService, Serializable {

    private Session session = null;
    private StringBuilder sb = null;
    private final GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();

    @Override
    public List<Object[]> listarComandasPorMesas(String mesa) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand,sum(tequanti*EEPLQTB1),testatus,tecdmesa,pessoas_mesa,tepedido ")
                .append("from  sosa98 ")
                .append("left outer join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("')")
                .append(" left outer join espelho_comanda on(numero=tenumero)")
                .append(" where ")
                .append("tecdmesa='").append(mesa).append("' ")
                .append("group by tecomand,testatus,tecdmesa,pessoas_mesa,tepedido ")
                .append("order by testatus desc");
        List<Object[]> lista = session.createSQLQuery(sb.toString()).list();
        session.close();
        return lista;
    }

    @Override
    public List<Object[]> ListarLancamentos(String codigo, String mesa) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("Tenumero as numero,Tenumseq as item,")
                .append("TECOMAND as comanda,TEREFERE as referencia,")
                .append("PRDESCRI as descricao,TEQUANTI as quantidade,")
                .append("EEPLQTB1 as preco,coalesce((TEQUANTI*EEPLQTB1),0) as precoTotal,")
                .append("TEVENDED as vendedor,TEOBSERV as observacao,TECDMESA as mesa ,TEIMPRIM as imprimir,TESTATUS as status,TEPEDIDO as pedido ")
                .append("from  sosa98 ")
                .append("inner join scea07 on(eerefere=terefere) inner join scea01 on(prrefere=eerefere) ")
                .append("where ")
                .append("TECOMAND='").append(codigo).append("' and TECDMESA='").append(mesa)
                .append("' group by ")
                .append("Tenumero,Tenumseq,")
                .append("TECOMAND,TEREFERE,")
                .append("PRDESCRI,TEQUANTI,")
                .append("EEPLQTB1,coalesce((TEQUANTI*EEPLQTB1),0),")
                .append("TEVENDED,TEOBSERV,TECDMESA,TEIMPRIM,TESTATUS,TEPEDIDO ")
                .append(" order by TECOMAND;");
        List<Object[]> lista = (List<Object[]>) session.createSQLQuery(sb.toString()).list();
        session.close();
        return lista;
    }

    @Override
    public List<Object[]> listarComandasPorCodigo(String mesa, String comanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand,sum(tequanti*EEPLQTB1),testatus,tecdmesa,pessoas_mesa,tepedido ")
                .append("from  sosa98 ")
                .append("inner join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("')")
                .append(" inner join espelho_comanda on(numero=tenumero) ")
                .append(" where ")
                .append("tecdmesa='").append(mesa).append("' and")
                .append(" tecomand like '%").append(comanda).append("%' ")
                .append("group by tecomand,testatus,tecdmesa,pessoas_mesa,tepedido ")
                .append("order by testatus desc");
        List<Object[]> lista = session.createSQLQuery(sb.toString()).list();
        session.close();
        return lista;
    }

    @Override
    public List<Object[]> listarComandas(String codigo) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> lista = null;
        if (session != null) {
            lista = session.createSQLQuery("select TEREFERE,PRDESCRI,TEQUANTI,EEPLQTB1,coalesce((TEQUANTI*EEPLQTB1),0),TEOBSERV,TEVENDED,TENUMSEQ,TEPEDIDO,TEDATCOM from sosa98 inner join scea07 on(eerefere=terefere and eecodemp='" + gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa() + "') inner join scea01 on(prrefere=eerefere) where TECOMAND='" + codigo + "'").list();
            session.close();
        }
        return lista;
    }

    @Override
    public List<Object[]> listarComandas() {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand,sum(tequanti*EEPLQTB1),testatus,tecdmesa,pessoas_mesa,tepedido ")
                .append("from  sosa98 ")
                .append("inner join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append("inner join espelho_comanda on(numero=tenumero)")
                .append("group by tecomand,testatus,tecdmesa,pessoas_mesa,tepedido ")
                .append("order by testatus desc");
        List<Object[]> lista = session.createSQLQuery(sb.toString()).list();
        session.close();
        return lista;
    }

    @Override
    public List<Object[]> pesquisarComandaPorCodigo(String codigo) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand,sum(tequanti*EEPLQTB1),testatus,tecdmesa,pessoas_mesa,tepedido ")
                .append("from  sosa98 ")
                .append("inner join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append("inner join espelho_comanda on(numero=tenumero) ")
                .append("where ")
                .append("tecomand like '%").append(codigo).append("%' ")
                .append("group by tecomand,testatus,tecdmesa,pessoas_mesa,tepedido ")
                .append("order by testatus desc");
        List<Object[]> lista = session.createSQLQuery(sb.toString()).list();
        session.close();
        return lista;
    }

    @Override
    public void salvar(Sosa98 sosa98) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(sosa98);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            Messages.addGlobalError("Erro ao Adiconar Comanda: detalhe do erro \n" + ex.getMessage());
        } finally {
            if (this.session != null) {
                session.close();
            }
        }
    }

    @Override
    public void excluir(String codigo) {
        executarSql("delete from sosa98 where tenumero='" + codigo + "'");
    }

    @Override
    public String gerarSequencia(String comanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Object> result = session.createSQLQuery("select tenumseq from sosa98 where tecomand='" + comanda + "' ")
                .list();
        int proximaSeguencia = 0;
        for (Object object : result) {
            proximaSeguencia = Integer.max(proximaSeguencia, Integer.parseInt(String.valueOf(object)));
        }
        session.close();
        proximaSeguencia++;
        return String.valueOf((proximaSeguencia));
    }

    @Override
    public Sosa98 buscarPorId(String codigo) {
        session = HibernateUtil.getSessionFactory().openSession();
        Sosa98 sosa98 = (Sosa98) session
                .createQuery("from Sosa98 where tenumero='" + codigo + "'").uniqueResult();
        session.close();
        return sosa98;
    }

    @Override
    public int buscarQuantidadeProdutosComanda(String comanda, String mesa) {
        session = HibernateUtil.getSessionFactory().openSession();
        Object count = session.createSQLQuery("select count(*) from  sosa98 where TECOMAND='" + comanda + "' and   TECDMESA='" + mesa + "' group by  TECDMESA").uniqueResult();
        if (count == null) {
            count = 0;
        }
        return Integer.parseInt(String.valueOf(count));
    }

    @Override
    public int verificarComanda(String mesa, String comanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        Object object = null;
        if (session != null) {
            object = session.createSQLQuery("select count(*) from sosa98 where  TECOMAND='" + comanda + "' ").uniqueResult();
            if (object == null) {
                object = 0;
            }
            session.close();
        }
        return Integer.parseInt(String.valueOf(object));
    }

    @Override
    public boolean verificarStatusMesa(String comanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        String status = null;
        if (session != null) {
            try {
                status = "" + session.createSQLQuery("select testatus from sosa98 where  TECOMAND='" + comanda + "' group by testatus ").uniqueResult();
            } catch (NonUniqueResultException ex) {
                return false;
            }
            session.close();
        }
        return "P".equals(status);
    }

    @Override
    public void excluirMesa(String mesa, String comanda) {
        executarSql("delete from sosa98 where tecdmesa='" + mesa + "' and TECOMAND ='" + comanda + "'");
    }

    @Override
    public void transferirComandaParaMesa(String mesa, Comandas comanda) {
        executarSql("update sosa98 set tecdmesa='" + mesa + "' where tecomand='" + comanda.getComanda() + "'");
        executarSql("update espelho_comanda set mesa='" + mesa + "' where pedido='" + comanda.getPedido() + "'");
    }

    @Override
    public void transferirComandaParaComanda(Comandas comandaOrigem, String comandaDestino) {
        List<Object[]> comanda = pesquisarComandaPorCodigo(comandaDestino);
        String mesaDestino, pedido, sqlSoa98, sqlEspelhoComanda;
        int somaQauntidadePessoasMesa;
        if (comanda.isEmpty()) {
            sqlSoa98 = "update sosa98 set tecomand='" + comandaDestino + "' where tecomand='" + comandaOrigem.getComanda() + "'";
            sqlEspelhoComanda = "update espelho_comanda set comanda='" + comandaDestino + "' where pedido ='" + comandaOrigem.getPedido() + "'";
            executarSql(sqlSoa98);
            executarSql(sqlEspelhoComanda);
            return;
        }
        mesaDestino = String.valueOf(comanda.get(0)[3]);
        pedido = String.valueOf(comanda.get(0)[5]);
        somaQauntidadePessoasMesa = Integer.parseInt(String.valueOf(comanda.get(0)[4])) + Integer.parseInt(comandaOrigem.getPessoasMesa());
        sqlSoa98 = "update sosa98 set tecomand='" + comandaDestino + "',tecdmesa='" + mesaDestino + "',tepedido='" + pedido + "' where tecomand='" + comandaOrigem.getComanda() + "'";
        sqlEspelhoComanda = "update espelho_comanda set pessoas_mesa='" + somaQauntidadePessoasMesa + "',comanda='" + comandaDestino + "',mesa='" + mesaDestino + "',pedido='" + pedido + "' where pedido in('" + comandaOrigem.getPedido() + "','" + pedido + "')";
        executarSql(sqlSoa98);
        executarSql(sqlEspelhoComanda);
        List<Object[]> itensTransferencia = pesquisarItensTransferencia(pedido);
        for (int i = 0; i < itensTransferencia.size(); i++) {
            transferirItens(String.valueOf(itensTransferencia.get(i)), String.valueOf(i + 1));
        }
    }

    @Override
    public List<Object[]> pesquisarItensTransferencia(String pedidos) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> resultado = session.createSQLQuery("select TENUMERO from  sosa98 inner join espelho_comanda on(numero=tenumero) where pedido ='" + pedidos + "';").list();
        session.close();
        return resultado;
    }

    @Override
    public void transferirItens(String numero, String item) {
        executarSql("update sosa98 set tenumseq='" + item + "' where tenumero='" + numero + "'");
        executarSql("update espelho_comanda set numero_item='" + item + "' where numero='" + numero + "'");
    }

    @Override
    public void atualizarStatusImpressao(String comanda) {
        executarSql("update sosa98 set TEIMPRIM='1' where tecomand='" + comanda + "'");
    }

    @Override
    public void atualizarStatusPreconta(String comanda) {
        executarSql("update sosa98 set testatus='P' where tecomand='" + comanda + "'");
    }

    private void executarSql(String sql) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.createSQLQuery(sql).executeUpdate();
        session.getTransaction().commit();
        session.flush();
        session.close();
    }

    @Override
    public int verificarNumeroPedido(String comanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        int pedido = 0;
        if (session != null) {
            try {
                Object uniqueResult = session.createSQLQuery("select tepedido from sosa98 where tecomand='" + comanda + "' group by tepedido ").uniqueResult();
                pedido = uniqueResult == null ? pedido : Integer.parseInt(String.valueOf(uniqueResult));
            } catch (NonUniqueResultException ex) {
                return 0;
            }
            session.close();
        }
        return pedido;
    }

    @Override
    public void alterar(Sosa98 sosa98) {
        executarSql("update sosa98 set TEOBSERV='" + sosa98.getTeobserv() + "' where tenumero='" + sosa98.getId().getTenumero() + "'");
    }

    @Override
    public void alterarQuantidadeItem(double quantidade, String numero) {
        executarSql("update sosa98 set tequanti=" + quantidade + " where tenumero='" + numero + "'");
    }

    @Override
    public void transferenciaItensParaMesaComanda(Comandas comanda, List<Lancamento> lancamentos) {
        List<Object[]> comandas = pesquisarComandaPorCodigo(comanda.getComanda());
        String pedido;
        if (comandas.isEmpty()) {
            pedido = new ControlePedido(this, comanda.getComanda()).gerarNumero();
        } else {
            pedido = String.valueOf(comandas.get(0)[5]);
        }
        String numeros = lancamentos.stream().map(Lancamento::getNumero).collect(Collectors.joining(","));
        executarSql("update          sosa98 set tepedido='" + pedido + "' ,tecdmesa='" + comanda.getMesa() + "' ,tecomand='" + comanda.getComanda() + "' where tenumero in(" + numeros + ")");
        executarSql("update espelho_comanda set   pedido='" + pedido + "' ,mesa='" + comanda.getMesa() + "' ,comanda='" + comanda.getComanda() + "' where   numero in(" + numeros + ")");
        List<Object[]> itensTransferencia = pesquisarItensTransferencia(pedido);
        for (int i = 0; i < itensTransferencia.size(); i++) {
            transferirItens(String.valueOf(itensTransferencia.get(i)), String.valueOf(i + 1));
        }
    }

}
