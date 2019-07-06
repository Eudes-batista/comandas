package util;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ValoresIniciais {

    private ValoresIniciais() {
        this.criarGeradorDeSeguenciaCancelamentoMesa();
        this.alterarCamposEspelhoComandaSosa98();
        this.incluirValoresIncias();
    }

    private void incluirValoresIncias() {
        executarSql("insert into motivo_cancelamento (codigo,nome) values(99,'EXCLUSAO DE MESA')");
        executarSql("insert into motivo_cancelamento (codigo,nome) values(100,'EXCLUSAO DE COMANDA')");
    }

    private void criarGeradorDeSeguenciaCancelamentoMesa() {
        executarSql("CREATE GENERATOR GEN_CANCELAMENTO_MESA;");
        executarSql("SET GENERATOR GEN_CANCELAMENTO_MESA TO 10;");
    }

    private void alterarCamposEspelhoComandaSosa98() {
        executarSql("alter table espelho_comanda alter pedido type varchar(9)");
        executarSql("alter table sosa98 alter tepedido type varchar(9)");
    }

    private void executarSql(String sql) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
            session.flush();
        } catch (HibernateException ex) {
            new Log().registrarErroAoSalvarValoresPadrao("valor padrao ja existe: " + sql);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static ValoresIniciais iniciar() {
        return new ValoresIniciais();
    }

}
