package controle;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;
import servico.EmpresaService;
import util.HibernateUtil;

@ManagedBean(name = "empresaService")
@ViewScoped
public class EmpresaControle implements EmpresaService, Serializable {

    @Override
    public Object[] buscarEmpresa(String empresa) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        if (session != null) {
            Object[] o =(Object[]) session.createSQLQuery("select LJNUMCGC as cnpj,LJINSCRI as inscricao,LJRAZSOC as razao,LJNOMINT as nomeFantasia,LJNUMCEP as cep,LJCIDADE as cidade,LJBAIRRO as bairro,LJENDERE as endereco,LJUNIFED as uf,LJTELEFO as telefone,LJNUMERO as numero,LJCODEMP as CODIGO from lapa19 where LJCODEMP='" + empresa + "'").uniqueResult();
            session.close();
            return o;
        }
        return null;
    }

}
