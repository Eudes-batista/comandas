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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                .append(" PEDIDO as PEDIDO")
                .append(",cast(pessoas_mesa as INTEGER) as QUANTIDADE")
                .append(",extract(MONTH from DATA_PRECONTA)  as mes ")
                .append(" from espelho_comanda ")
                .append(" where")
                .append(" DATA_PRECONTA BETWEEN ").append("'").append(filtroVendaDetalhe.getDataInicial()).append(" 00:00:00' ")
                .append(" AND '").append(filtroVendaDetalhe.getDataFinal()).append(" 23:59:59'")
                .append(" AND STATUS_ITEM ='").append("N").append("'")
                .append(" AND STATUS ='").append("P").append("'")
                .append(" group by ")
                .append("   PEDIDO,DATA_PRECONTA,pessoas_mesa");
        Statement preparedStatement = connection.createStatement();
        ResultSet resultSet = preparedStatement.executeQuery(stringBuilder.toString());
        List<ClienteAtendidoPorMes> clienteAtendidos = new ArrayList<>();
        Map<Integer, Integer> mapPessoas = new HashMap<>();
        while (resultSet.next()) {
            if(resultSet.getObject("mes") == null) break;
            Integer mes = Integer.parseInt(String.valueOf(resultSet.getObject("mes")));
            Integer quantidades = mapPessoas.get(mes);
            boolean existe = true;
            if (quantidades == null) {
                quantidades = 0;
                if (resultSet.getObject("QUANTIDADE") != null) {
                    quantidades = Integer.parseInt(String.valueOf(resultSet.getObject("QUANTIDADE")));
                }
                existe = false;
            }
            if (existe) {
                quantidades += Integer.parseInt(String.valueOf(resultSet.getObject("QUANTIDADE")));
            }
            mapPessoas.put(mes, quantidades);
        }
        if(!mapPessoas.isEmpty())
            mapPessoas.forEach((chave, valor) -> clienteAtendidos.add(new ClienteAtendidoPorMes(valor, chave)));
        return clienteAtendidos;
    }

}
