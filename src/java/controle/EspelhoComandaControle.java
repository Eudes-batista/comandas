package controle;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.EspelhoComanda;
import modelo.Mesa;
import modelo.dto.Cancelamento;
import org.hibernate.Session;
import servico.EspelhoComandaService;
import util.GerenciaArquivo;
import util.HibernateUtil;

@ManagedBean(name = "espelhoComandaService")
@ViewScoped
public class EspelhoComandaControle implements EspelhoComandaService, Serializable {

    private Session session = null;

    @Override
    public void salvar(EspelhoComanda espelhoComanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.save(espelhoComanda);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public void excluir(Integer numero) {
        executarSql("delete from espelho_comanda where numero =" + numero + "");
    }

    @Override
    public void atualizarStatusImpressao(String comanda) {
        executarSql("update espelho_comanda set impressao='1' where comanda='" + comanda + "'");
    }

    @Override
    public void atualizarDataPreconta(String data, Mesa mesa) {
        executarSql("update espelho_comanda set data_preconta='" + data + "',pessoas_pagantes='" + mesa.getPAGANTES() + "' where pedido in(" + mesa.getPEDIDO() + ")");
    }

    @Override
    public void alterar(EspelhoComanda espelhoComanda) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            session.getTransaction().begin();
            session.update(espelhoComanda);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public EspelhoComanda buscarPorId(Integer numero) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            EspelhoComanda espelhoComanda = (EspelhoComanda) session.createQuery("from EspelhoComanda where numero=" + numero).uniqueResult();
            session.close();
            return espelhoComanda;
        }
        return new EspelhoComanda();
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
    public void salvarPessoaQueAutorizouReipressao(String usuario, String item) {
        executarSql("update espelho_comanda set responsavel_reipressao='" + usuario + "' where numero='" + item + "'");
    }

    @Override
    public List<EspelhoComanda> listarAuditoria() {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            return session.createQuery("from EspelhoComanda").list();
        }
        return null;
    }

    @Override
    public void atualizarPorcentagem(String pedido, double porcentagem) {
        executarSql("update espelho_comanda set porcentagem = " + porcentagem + " where pedido = '" + pedido + "'");
        atualizarValorPorcentagemItens(pedido);
    }

    @Override
    public List<Object[]> listarProdutosPedido(String pedido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            return session.createSQLQuery("select numero, EEPLQTB1, quantidade, porcentagem,prdescri from espelho_comanda left outer join \n"
                    + "scea07 on (eerefere = referencia and eecodemp = '" + new GerenciaArquivo().bucarInformacoes().getConfiguracao().getEmpresa() + "') left outer join scea01 on (prrefere = eerefere) where pedido = '" + pedido + "' ").list();
        }
        return null;
    }

    @Override
    public void atualizarValorPorcentagemItens(String pedido) {
        List<Object[]> listarProdutosPedido = listarProdutosPedido(pedido);
        for (Object[] itens : listarProdutosPedido) {
            double valorItem, quantidadeItem, porcentagem, valorTotalItem, porcentagemEmValor, valorPorcentagemItem;

            valorItem = Double.parseDouble(String.valueOf(itens[1]));

            quantidadeItem = Double.parseDouble(String.valueOf(itens[2]));

            porcentagem = Double.parseDouble(String.valueOf(itens[3]));

            String descricao = String.valueOf(itens[4]);

            valorTotalItem = quantidadeItem * valorItem;

            porcentagemEmValor = porcentagem / 100;

            valorPorcentagemItem = valorTotalItem * porcentagemEmValor;

            valorPorcentagemItem = Double.parseDouble(new DecimalFormat("###,##0.00").format(valorPorcentagemItem).replace(".", "").replace(",", "."));
            if (!descricao.contains("couvert".toUpperCase())) {
                executarSql("update espelho_comanda set VALOR_PORCENTAGEM=" + valorPorcentagemItem + " where numero='" + String.valueOf(itens[0]) + "'");
                return;
            }
            executarSql("update espelho_comanda set porcentagem = " + 0 + ",VALOR_PORCENTAGEM=" + 0 + " where numero = '" + String.valueOf(itens[0]) + "'");

        }
    }

    @Override
    public void atualizarStatusItens(Cancelamento cancelamento) {
        executarSql("update espelho_comanda set RESPONSAVEL_CANCELAMENTO='" + cancelamento.getUsuario() + "' ,status_item='"+cancelamento.getStatus()+"',QUANTIDADE_CANCELADA=QUANTIDADE,motivo_cancelamento="+cancelamento.getMotivo()+" where pedido in(" + cancelamento.getPedidos() + ") and MOTIVO_CANCELAMENTO is null ");
    }

    @Override
    public void atualizarResponsavelPreconta(String pedido, String responsavel) {
        executarSql("update espelho_comanda set responsavel_preconta='" + responsavel + "' where pedido in(" + pedido + ")");
    }

    @Override
    public void atualizarResponsavelParcial(String pedido, String responsavel) {
        executarSql("update espelho_comanda set responsavel_parcial='" + responsavel + "' where pedido in(" + pedido + ")");
    }

    @Override
    public void atualizarResponsavelTransferencia(String pedido, String responsavel) {
        executarSql("update espelho_comanda set RESPONSAVEL_TRANSFERENCIA='" + responsavel + "' where pedido in(" + pedido + ") ");
    }

    @Override
    public String buscarDataPreconta(String pedido) {
        session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Object object = session.createSQLQuery("select DATA_PRECONTA as preconta from espelho_comanda where pedido='" + pedido + "' group by DATA_PRECONTA ").uniqueResult();
            String dataPreconta =object != null ? String.valueOf(object) :null;
            session.close();
            return dataPreconta;
        }
        return null;
    }

}
