/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servico;

import java.sql.SQLException;
import java.util.List;
import modelo.dto.ClienteAtendidoPorMes;
import modelo.dto.FiltroVendaDetalhe;

/**
 *
 * @author Administrador
 */
public interface ClienteAtendidoService {
    public List<ClienteAtendidoPorMes> clienteAtendidos(FiltroVendaDetalhe filtroVendaDetalhe) throws SQLException;
}
