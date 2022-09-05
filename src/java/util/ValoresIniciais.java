package util;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ValoresIniciais {

    private ValoresIniciais() {
        this.criarGeradorDeSeguenciaCancelamentoMesa();
        this.alterarCamposEspelhoComandaSosa98();
        this.incluirValoresIncias();
        this.apagarProcedure();
        this.criarProcedureInserirComandas();
        this.apagarTriggerExcluirComandaSosa98();
        this.criarTriggerExcluirComandaSosa98();
    }

    private void incluirValoresIncias() {
        executarSql("update or insert into motivo_cancelamento (codigo,nome) values(99,'EXCLUSAO DE MESA')");
        executarSql("update or insert into motivo_cancelamento (codigo,nome) values(100,'EXCLUSAO DE COMANDA')");
    }

    private void criarGeradorDeSeguenciaCancelamentoMesa() {
        executarSql("CREATE GENERATOR GEN_CANCELAMENTO_MESA;");
        executarSql("SET GENERATOR GEN_CANCELAMENTO_MESA TO 1;");
    }

    private void alterarCamposEspelhoComandaSosa98() {
        executarSql("alter table espelho_comanda alter pedido type varchar(9)");
        executarSql("alter table sosa98 alter tepedido type varchar(9)");
        executarSql("alter table item_acompanhamento alter pedido type varchar(9)");
    }

    private void criarProcedureInserirComandas() {
        String sql = "create procedure INSERIR_COMANDAS (comanda varchar(4)) as\n"
                + "  DECLARE VARIABLE quantidade INTEGER; \n"
                + "begin    \n"
                + "  select count(TECOMAND) as qtd from sosa98 where TECOMAND = :comanda INTO quantidade;  \n"
                + "  IF (quantidade = 1) THEN\n"
                + "    BEGIN\n"
                + "      insert into COMANDAS_EXCLUIDAS (COMANDA) select TECOMAND from sosa98 where TECOMAND=:comanda group by TECOMAND;\n"
                + "    END\n"
                + "END";
        this.executarSql(sql);
    }

    private void apagarProcedure() {
        String sql = "DROP PROCEDURE INSERIR_COMANDAS;";
        this.executarSql(sql);
    }

    private void apagarTriggerExcluirComandaSosa98() {
        String sql = "DROP TRIGGER ANTES_DELETAR;";
        this.executarSql(sql);
    }

    private void criarTriggerExcluirComandaSosa98() {
        String sql = " CREATE TRIGGER ANTES_DELETAR FOR SOSA98 BEFORE DELETE POSITION 0 AS \n"
                + "BEGIN \n"
                + "  execute procedure INSERIR_COMANDAS OLD.TECOMAND; \n"
                + "END ";
        this.executarSql(sql);
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
