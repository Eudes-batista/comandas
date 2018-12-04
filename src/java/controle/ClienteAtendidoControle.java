/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.dto.ClienteAtendidoPorMes;
import modelo.dto.FiltroVendaDetalhe;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import servico.ClienteAtendidoService;
import util.HibernateUtil;

/**
 *
 * @author Administrador
 */
@ManagedBean(name = "clienteAtendidoService")
@ViewScoped
public class ClienteAtendidoControle implements ClienteAtendidoService, Serializable {

    private StringBuilder stringBuilder = null;

    @Override
    public List<ClienteAtendidoPorMes> clienteAtendidos(FiltroVendaDetalhe filtroVendaDetalhe) throws SQLException {
        Connection connection = null;
        connection = ((SessionFactoryImplementor) HibernateUtil.getSessionFactory()).getConnectionProvider().getConnection();
        stringBuilder = new StringBuilder();
        stringBuilder.append("select ")
                .append(" SUM(cast(pessoas_mesa as INTEGER)) as QUANTIDADE")
                .append(",extract(MONTH from DATA_PRECONTA)  as mes ")
                .append(" from espelho_comanda ")
                .append(" where")
                .append(" DATA_PRECONTA BETWEEN ").append("'").append(filtroVendaDetalhe.getDataInicial()).append(" 00:00:00' ")
                .append(" AND '").append(filtroVendaDetalhe.getDataFinal()).append(" 23:59:59'")
                .append(" AND STATUS_ITEM ='").append("N").append("'")
                .append(" AND STATUS ='").append("P").append("'")
                .append(" group by ")
                .append(" extract(MONTH from DATA_PRECONTA)");
        Statement preparedStatement = connection.createStatement();
        ResultSet resultSet = preparedStatement.executeQuery(stringBuilder.toString());
        List<ClienteAtendidoPorMes> clienteAtendidos = new ArrayList<>();
        while (resultSet.next()) {
            clienteAtendidos.add(new  ClienteAtendidoPorMes(Integer.parseInt(String.valueOf(resultSet.getObject("QUANTIDADE"))),Integer.parseInt(String.valueOf(resultSet.getObject("mes")))));
        }
        return clienteAtendidos;
    }

}
