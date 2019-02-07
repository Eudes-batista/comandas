package controle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Comandas;
import modelo.EspelhoComanda;
import modelo.ItemAcompanhamento;
import modelo.Lancamento;
import modelo.Sosa98;
import modelo.Sosa98Id;
import modelo.dto.ItemAcompanhamentoTransferencia;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import servico.ComandaService;
import servico.EspelhoComandaService;
import servico.ItemAcompanhamentoService;
import util.GerenciaArquivo;
import util.HibernateUtil;
import util.Log;

@ManagedBean(name = "controle")
@ViewScoped
public class Controle implements ComandaService, Serializable {

    private Session session = null;
    private StringBuilder sb = null;
    private final GerenciaArquivo gerenciaArquivo = new GerenciaArquivo();

    @Getter
    @Setter
    @ManagedProperty(value = "#{espelhoComandaService}")
    private EspelhoComandaService espelhoComandaService;
    @Getter
    @Setter
    @ManagedProperty(value = "#{itemAcompanhamentoService}")
    private ItemAcompanhamentoService itemAcompanhamentoService;

    @Override
    public List<Comandas> listarComandasPorMesas(String mesa) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand as COMANDA,sum(tequanti*EEPLQTB1) as TOTAL,testatus as STATUS,tecdmesa as MESA,tepedido as PEDIDO ")
                .append("from  sosa98 ")
                .append("left outer join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("')")
                .append(" where ")
                .append("tecdmesa='").append(mesa).append("' ")
                .append("group by tecomand,testatus,tecdmesa,tepedido ")
                .append("order by testatus desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
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
    public List<Comandas> listarComandasPorCodigo(String mesa, String comanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand as COMANDA,sum(tequanti*EEPLQTB1) TOTAL,testatus STATUS,tecdmesa MESA,tepedido AS PEDIDO ")
                .append("from  sosa98 ")
                .append("inner join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("')")
                .append(" where ")
                .append("tecdmesa='").append(mesa).append("' and")
                .append(" tecomand like '%").append(comanda).append("%' ")
                .append("group by tecomand,testatus,tecdmesa,tepedido ")
                .append("order by testatus desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
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
    public List<Comandas> listarComandas() {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand AS COMANDA,sum(tequanti*EEPLQTB1) as TOTAL,testatus as STATUS,tecdmesa as MESA,tepedido as PEDIDO ")
                .append("from  sosa98 ")
                .append(" inner join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append(" group by tecomand,testatus,tecdmesa,tepedido")
                .append(" order by testatus desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
        session.close();
        return lista;
    }

    @Override
    public List<Comandas> pesquisarComandaPorCodigo(String codigo) {
        session = HibernateUtil.getSessionFactory().openSession();
        sb = new StringBuilder();
        sb.append("select ")
                .append("tecomand as COMANDA,sum(tequanti*EEPLQTB1) as TOTAL,testatus as STATUS,tecdmesa as MESA,tepedido as PEDIDO")
                .append(" from  sosa98 ")
                .append(" inner join scea07 on(eerefere=terefere and eecodemp='").append(gerenciaArquivo.bucarInformacoes().getConfiguracao().getEmpresa()).append("') ")
                .append(" where ")
                .append(" tecomand like '%").append(codigo).append("%' ")
                .append(" group by tecomand,testatus,tecdmesa,tepedido")
                .append(" order by testatus desc");
        SQLQuery sQLQuery = session.createSQLQuery(sb.toString());
        Query query = sQLQuery.setResultTransformer(Transformers.aliasToBean(Comandas.class));
        List<Comandas> lista = query.list();
        session.close();
        return lista;
    }

    @Override
    public void salvar(Sosa98 sosa98) throws Exception {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(sosa98);
            transaction.commit();
        } catch (Exception ex) {
            registrarErroAoSalvarProduto(sosa98, ex);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            if (this.session != null) {
                session.close();
            }
        }
    }

    private void registrarErroAoSalvarProduto(Sosa98 sosa98, Exception ex) {
        Lancamento lancamento = new Lancamento();
        lancamento.setComanda(sosa98.getTecomand());
        lancamento.setMesa(sosa98.getTecdmesa());
        lancamento.setItem(sosa98.getId().getTenumseq());
        lancamento.setNumero(sosa98.getId().getTenumero());
        lancamento.setReferencia(sosa98.getTerefere());
        lancamento.setQuantidade(sosa98.getTequanti());
        lancamento.setObservacao(sosa98.getTeobserv());
        lancamento.setVendedor(sosa98.getTevended());
        lancamento.setStatus(sosa98.getTestatus());
        lancamento.setPedido(sosa98.getTepedido());
        new Log().registrarErroAoSalvarProduto(ex.getMessage(), lancamento);
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
    public String verificarComandaNaMesa(String comanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        Object object = null;
        if (session != null) {
            object = session.createSQLQuery("select first 1 TECDMESA from sosa98 where  TECOMAND='" + comanda + "' ").uniqueResult();
            if (object == null) {
                object = 0;
            }
            session.close();
        }
        return String.valueOf(object);
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
        executarSql("update sosa98 set tecdmesa='" + mesa + "' where tecomand='" + comanda.getCOMANDA() + "'");
        executarSql("update espelho_comanda set mesa='" + mesa + "' where pedido='" + comanda.getPEDIDO() + "'");
    }

    @Override
    public void transferirComandaParaComanda(Comandas comandaOrigem, String comandaDestino) {
        List<Comandas> comanda = pesquisarComandaPorCodigo(comandaDestino);
        String mesaDestino, pedido, sqlSoa98, sqlEspelhoComanda, status;
        int somaQuantidadePessoasMesa;
        if (comanda.isEmpty()) {
            sqlSoa98 = "update sosa98 set tecomand='" + comandaDestino + "' where tecomand='" + comandaOrigem.getCOMANDA() + "'";
            sqlEspelhoComanda = "update espelho_comanda set comanda='" + comandaDestino + "' where pedido ='" + comandaOrigem.getPEDIDO() + "'";
            executarSql(sqlSoa98);
            executarSql(sqlEspelhoComanda);
            return;
        }
        mesaDestino = String.valueOf(comanda.get(0).getMESA());
        pedido = String.valueOf(comanda.get(0).getPEDIDO());
        status = String.valueOf(comanda.get(0).getSTATUS());
        somaQuantidadePessoasMesa = Integer.parseInt(String.valueOf(buscarNumeroDePessoas(pedido))) + buscarNumeroDePessoas(comandaOrigem.getPEDIDO());
        int ultimoItemComandaDestino = buscarUltimoItemComandaDestino(pedido);
        List<Object> buscarSeguenciaDeItemComanda = buscarSeguenciaDeItemComanda(comandaOrigem.getPEDIDO());
        for (Object item : buscarSeguenciaDeItemComanda) {
            List<ItemAcompanhamentoTransferencia> pesquisarItensComAcompanhamento = pesquisarItensComAcompanhamento(comandaOrigem.getPEDIDO(), String.valueOf(item));
            if (pesquisarItensComAcompanhamento.isEmpty()) {
                ultimoItemComandaDestino++;
                while (verificarSeJaExisteSeguenciaItem(String.valueOf(ultimoItemComandaDestino), comandaOrigem.getPEDIDO())) {
                    ultimoItemComandaDestino++;
                }
                executarSql("update sosa98 set tenumseq='" + ultimoItemComandaDestino + "' where tepedido='" + comandaOrigem.getPEDIDO() + "' and tenumseq='" + item + "'");
                executarSql("update espelho_comanda set NUMERO_ITEM='" + ultimoItemComandaDestino + "' where pedido='" + comandaOrigem.getPEDIDO() + "' and numero_item='" + item + "'");
            } else {
                ItemAcompanhamentoTransferencia itemTransferencia = new ItemAcompanhamentoTransferencia(Integer.parseInt(String.valueOf(item)), comandaOrigem.getPEDIDO());
                atualizarSeguenciaItemComanda(itemTransferencia, pedido);
            }
        }
        sqlSoa98 = "update sosa98 set tecomand='" + comandaDestino + "',tecdmesa='" + mesaDestino + "',tepedido='" + pedido + "',testatus='" + status + "' where tecomand='" + comandaOrigem.getCOMANDA() + "'";
        sqlEspelhoComanda = "update espelho_comanda set pessoas_mesa='" + somaQuantidadePessoasMesa + "',comanda='" + comandaDestino + "',mesa='" + mesaDestino + "',pedido='" + pedido + "',status='" + status + "' where pedido in('" + comandaOrigem.getPEDIDO() + "','" + pedido + "')";
        executarSql(sqlSoa98);
        executarSql(sqlEspelhoComanda);
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
    public void atualizarStatusPreconta(Comandas comanda) {
        executarSql("update sosa98 set testatus='P' where tecomand='" + comanda.getCOMANDA() + "'");
        executarSql("update espelho_comanda set data_preconta='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "',status='P' where pedido='" + comanda.getPEDIDO() + "'");
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
    public void transferenciaItensParaComanda(Comandas comanda, List<Lancamento> lancamentosTransferencia, List<Lancamento> lancamentosOrigem, String usuarioTransferencia) {
        List<Comandas> comandas = pesquisarComandaPorCodigo(comanda.getCOMANDA());
        String pedido, status, pessoas, numeros = lancamentosTransferencia.stream().map(Lancamento::getNumero).collect(Collectors.joining(",")), numero = "";
        if (comandas.isEmpty()) {
            pedido = new ControlePedido(this, comanda.getCOMANDA()).gerarNumero();
            status = "";
            pessoas = "1";
            for (Lancamento lancamento : lancamentosTransferencia) {
                Lancamento lancamentoOrigem = lancamentosOrigem.stream().filter(l -> l.getNumero().equals(lancamento.getNumero())).findFirst().get();
                List<ItemAcompanhamentoTransferencia> itemAcompanhamentoTransferencias;
                if (lancamento.getQuantidade() == lancamentoOrigem.getQuantidade()) {
                    itemAcompanhamentoTransferencias = pesquisarItensComAcompanhamento(lancamento.getPedido(), lancamento.getItem());
                    if (!itemAcompanhamentoTransferencias.isEmpty()) {
                        ItemAcompanhamentoTransferencia itemAcompanhamentoTransferencia = new ItemAcompanhamentoTransferencia(Integer.parseInt(lancamento.getItem()), lancamento.getPedido());
                        atualizarSeguenciaItemComanda(itemAcompanhamentoTransferencia, pedido);
                    }
                    if (!numero.isEmpty()) {
                        numeros = gerarNumeroDeAtualizacao(numeros, numero);
                    }
                    executarSql("update          sosa98 set testatus='" + status + "' ,tepedido='" + pedido + "' ,tecdmesa='" + comanda.getMESA() + "' ,tecomand='" + comanda.getCOMANDA() + "' where tenumero in(" + numeros + ")");
                    executarSql("update espelho_comanda set RESPONSAVEL_TRANSFERENCIA='" + usuarioTransferencia.toUpperCase() + "',pessoas_mesa='" + pessoas + "',status='" + status + "' ,pedido='" + pedido + "' ,mesa='" + comanda.getMESA() + "' ,comanda='" + comanda.getCOMANDA() + "' where   numero in(" + numeros + ")");
                    return;
                }
                numero += "," + lancamento.getNumero();
                transferenciaParcialDeItens(comanda, lancamento, lancamentoOrigem, usuarioTransferencia, pedido, pessoas);
            }
        } else {
            pedido = String.valueOf(comandas.get(0).getPEDIDO());
            status = comandas.get(0).getSTATUS();
            pessoas = String.valueOf(buscarNumeroDePessoas(pedido));
            for (Lancamento lancamento : lancamentosTransferencia) {
                Lancamento lancamentoOrigem = lancamentosOrigem.stream().filter(l -> l.getNumero().equals(lancamento.getNumero())).findFirst().get();
                if (lancamento.getQuantidade() == lancamentoOrigem.getQuantidade()) {
                    List<ItemAcompanhamentoTransferencia> itemAcompanhamentoTransferencias = pesquisarItensComAcompanhamento(lancamento.getPedido(), lancamento.getItem());
                    if (itemAcompanhamentoTransferencias.isEmpty()) {
                        int ultimoItemComandaDestino = buscarUltimoItemComandaDestino(pedido);
                        int seguencia = ultimoItemComandaDestino + 1;
                        executarSql("update sosa98 set tenumseq='" + seguencia + "' where tepedido='" + lancamentosTransferencia.get(0).getPedido() + "' and tenumseq='" + lancamentosTransferencia.get(0).getItem() + "'");
                        executarSql("update espelho_comanda set NUMERO_ITEM='" + seguencia + "' where pedido='" + lancamentosTransferencia.get(0).getPedido() + "' and numero_item='" + lancamentosTransferencia.get(0).getItem() + "'");
                    } else {
                        ItemAcompanhamentoTransferencia item = new ItemAcompanhamentoTransferencia(Integer.parseInt(lancamento.getItem()), lancamento.getPedido());
                        atualizarSeguenciaItemComanda(item, pedido);
                    }
                    if (!numero.isEmpty()) {
                        numeros = gerarNumeroDeAtualizacao(numeros, numero);
                    }
                    executarSql("update          sosa98 set testatus='" + status + "' ,tepedido='" + pedido + "' ,tecdmesa='" + comanda.getMESA() + "' ,tecomand='" + comanda.getCOMANDA() + "' where tenumero in(" + numeros + ")");
                    executarSql("update espelho_comanda set RESPONSAVEL_TRANSFERENCIA='" + usuarioTransferencia.toUpperCase() + "',pessoas_mesa='" + pessoas + "',status='" + status + "' ,pedido='" + pedido + "' ,mesa='" + comanda.getMESA() + "' ,comanda='" + comanda.getCOMANDA() + "' where   numero in(" + numeros + ")");
                    return;
                }
                numero += "," + lancamento.getNumero();
                transferenciaParcialDeItens(comanda, lancamento, lancamentoOrigem, usuarioTransferencia, pedido, pessoas);
            }
        }
    }

    private String gerarNumeroDeAtualizacao(String numeros, String numero) {
        String[] splitNumeros = numeros.split(",");
        numero = numero.replaceFirst(",", "");
        for (String numeroCorrente : splitNumeros) {
            if (numero.contains(numeroCorrente)) {
                int index = numeros.indexOf(numeroCorrente);
                int tamanho = numeroCorrente.length();
                int indexFinal = tamanho + index;
                index = index - 1 == -1 ? 0 : index - 1;
                String numeroEcontrado = numeros.substring(index, indexFinal);
                numeros = numeros.replaceFirst(numeroEcontrado, "");
            }
        }
        return numeros.replaceFirst(",", "");
    }

    private void transferenciaParcialDeItens(Comandas comanda, Lancamento lancamento, Lancamento lancamentoOrigem, String usuarioTransferencia, String pedido, String pessoas) {
        lancamento.setNumero(gerarNumero() + lancamento.getItem());
        lancamento.setPedido(pedido);
        lancamento.setComanda(comanda.getCOMANDA());
        lancamento.setMesa(comanda.getMESA());
        preencherSosa98(lancamento, usuarioTransferencia.toUpperCase(), pessoas);
        salvarAcompanhamento(pedido, lancamentoOrigem);
        double quantidadeItem = lancamentoOrigem.getQuantidade() - lancamento.getQuantidade();
        alterarQuantidade(quantidadeItem, lancamentoOrigem.getNumero());
    }

    @Override
    public List<ItemAcompanhamentoTransferencia> pesquisarItensComAcompanhamento(String pedido, String item) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            return session.createSQLQuery("select ITEM,PEDIDO from sosa98 inner join item_acompanhamento on(pedido=tepedido) where tepedido='" + pedido + "' and item='" + item + "' group by ITEM,PEDIDO")
                    .setResultTransformer(Transformers.aliasToBean(ItemAcompanhamentoTransferencia.class))
                    .list();
        }
        return null;
    }

    private List<ItemAcompanhamento> pesquisarAcompanhamento(String pedido, String item) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            return session.createQuery("from ItemAcompanhamento where pedido='" + pedido + "' and item='" + item + "'")
                    .list();
        }
        return null;
    }

    private void salvarAcompanhamento(String pedido, Lancamento lancamentoOrigem) {
        List<ItemAcompanhamento> itemAcompanhamentos = pesquisarAcompanhamento(lancamentoOrigem.getPedido(), lancamentoOrigem.getItem());
        if (!itemAcompanhamentos.isEmpty()) {
            itemAcompanhamentos.forEach(itemAcompanhamento -> {
                itemAcompanhamento.setPedido(pedido);
                itemAcompanhamentoService.salvar(itemAcompanhamento);
            });
        }
    }

    private void preencherSosa98(Lancamento lancamento, String usuario, String pessoas) {
        Date data = new Date();
        Sosa98 sosa98 = new Sosa98(new Sosa98Id(lancamento.getNumero(), lancamento.getItem()),
                lancamento.getComanda(),
                lancamento.getReferencia(),
                lancamento.getQuantidade(),
                data,
                lancamento.getVendedor(),
                lancamento.getObservacao(),
                lancamento.getMesa(),
                lancamento.getStatus(),
                lancamento.getImprimir(),
                lancamento.getPedido()
        );
        try {
            salvar(sosa98);
            salvarEspelho(lancamento, data, usuario, pessoas);
        } catch (Exception ex) {
            registrarErroAoSalvarProduto(sosa98, ex);
        }
    }

    private void salvarEspelho(Lancamento lancamento, Date data, String usuario, String pessoas) {
        EspelhoComanda espelhoComanda = new EspelhoComanda();
        espelhoComanda.setNumero(Integer.parseInt(lancamento.getNumero()));
        espelhoComanda.setPedido(lancamento.getPedido());
        espelhoComanda.setMesa(lancamento.getMesa());
        espelhoComanda.setComanda(lancamento.getComanda());
        espelhoComanda.setNumeroItem(lancamento.getItem());
        espelhoComanda.setReferencia(lancamento.getReferencia());
        espelhoComanda.setQuantidade(lancamento.getQuantidade());
        espelhoComanda.setVendedor(lancamento.getVendedor());
        espelhoComanda.setImpressao(lancamento.getImprimir());
        espelhoComanda.setStatus(lancamento.getStatus());
        espelhoComanda.setObservacao(lancamento.getObservacao());
        espelhoComanda.setValorItem(lancamento.getPreco());
        espelhoComanda.setStatusItem("N");
        espelhoComanda.setPessoasMesa(pessoas);
        espelhoComanda.setRespansavelTransferencia(usuario);
        espelhoComanda.setData(data);
        if (!new GerenciaArquivo().bucarInformacoes().getConfiguracao().getCobraDezPorcento().isEmpty()) {
            espelhoComanda.setPorcentagem(10d);
            double valorComDezPOrcento = lancamento.getPrecoTotal() * 0.10;
            espelhoComanda.setValorPorcentagem(valorComDezPOrcento);
        }
        espelhoComandaService.salvar(espelhoComanda);
    }

    private void alterarQuantidade(double quantidade, String numero) {
        alterarQuantidadeItem(quantidade, numero);
        executarSql("update espelho_comanda set quantidade=" + quantidade + " where numero='" + numero + "'");
    }

    private void atualizarSeguenciaItemComanda(ItemAcompanhamentoTransferencia acompanhamentoTransferencia, String pedido) {
        int ultimoItemComandaDestino = buscarUltimoItemComandaDestino(pedido);
        String novaSeguenciaDoItem = String.valueOf(ultimoItemComandaDestino + 1);
        while (verificarSeJaExisteSeguenciaItem(novaSeguenciaDoItem, acompanhamentoTransferencia.getPEDIDO())) {
            novaSeguenciaDoItem = String.valueOf((Integer.parseInt(novaSeguenciaDoItem) + 1));
        }
        executarSql("update sosa98 set tenumseq='" + novaSeguenciaDoItem + "' where tepedido='" + acompanhamentoTransferencia.getPEDIDO() + "' and tenumseq='" + acompanhamentoTransferencia.getITEM() + "'");
        executarSql("update espelho_comanda set NUMERO_ITEM='" + novaSeguenciaDoItem + "' where pedido='" + acompanhamentoTransferencia.getPEDIDO() + "' and numero_item='" + acompanhamentoTransferencia.getITEM() + "'");
        executarSql("update item_acompanhamento set item ='" + novaSeguenciaDoItem + "',pedido='" + pedido + "' where pedido='" + acompanhamentoTransferencia.getPEDIDO() + "' and item='" + acompanhamentoTransferencia.getITEM() + "'");
    }

    private int buscarUltimoItemComandaDestino(String pedido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Object uniqueResult = session.createSQLQuery("select first 1 tenumseq from sosa98 where tepedido='" + pedido + "' group by tenumseq order by tenumseq desc")
                    .uniqueResult();
            return uniqueResult == null ? 1 : Integer.parseInt(String.valueOf(uniqueResult));
        }
        return 0;
    }

    private List<Object> buscarSeguenciaDeItemComanda(String pedido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            List<Object> seguencias = session.createSQLQuery("select tenumseq from sosa98 where tepedido='" + pedido + "' group by tenumseq order by tenumseq")
                    .list();
            return seguencias;
        }
        return null;
    }

    private boolean verificarSeJaExisteSeguenciaItem(String item, String pedido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Object uniqueResult = session.createSQLQuery("select count(*) from sosa98 where tepedido='" + pedido + "' and tenumseq='" + item + "'")
                    .uniqueResult();
            return Integer.parseInt(String.valueOf(uniqueResult)) != 0;
        }
        return false;
    }

    @Override
    public int verificarSePedidoJaExiste(String pedido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Object uniqueResult = session.createSQLQuery("select count(*) from espelho_comanda where pedido='" + pedido + "' group by pedido")
                    .uniqueResult();
            return uniqueResult != null ? Integer.parseInt(String.valueOf(uniqueResult)) : 0;
        }
        return 0;
    }

    @Override
    public int buscarNumeroDePessoas(String pedido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Object uniqueResult = session.createSQLQuery("select pessoas_mesa from espelho_comanda where pedido='" + pedido + "' group by pessoas_mesa")
                    .uniqueResult();
            return uniqueResult != null ? Integer.parseInt(String.valueOf(uniqueResult)) : 0;
        }
        return 0;
    }

    /*
     *Gera chave primaria dos itens
     */
    @Override
    public String gerarNumero() {
        String chave = gerarChave();
        while (verrificarSequenciaChavePrimaria(chave)) {
            chave = gerarChave();
        }
        return chave;
    }

    private String gerarChave() {
        LocalDate data = LocalDate.now();
        LocalTime hora = LocalTime.now();
        String chave = String.valueOf(((data.getYear() * data.getMonthValue() * data.getDayOfMonth()) + (2217 * hora.getHour())) + (hora.getSecond() + hora.getNano()));
        chave = chave.length() > 6 ? chave.substring(0, 6) : chave;
        return chave;
    }

    private boolean verrificarSequenciaChavePrimaria(String numero) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Object seguencias = session.createSQLQuery("select tenumero from sosa98 where tenumero='" + numero + "'")
                    .uniqueResult();
            return seguencias != null;
        }
        return false;
    }
    
    @Override
    public String gerarNumeroComanda() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Object seguencias = session.createSQLQuery("select max(cast(comanda as integer)) from espelho_comanda")
                    .uniqueResult();
            seguencias = seguencias == null ? "0000" : seguencias;
            seguencias = String.format("%04d", (Integer.parseInt(String.valueOf(seguencias))+1));
            return String.valueOf(seguencias);
        }
        return "0001";
    }
    
    
}
