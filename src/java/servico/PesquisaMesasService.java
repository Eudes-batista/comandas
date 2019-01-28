/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servico;

import java.util.List;
import modelo.Comandas;

/**
 *
 * @author Administrador
 */
public interface PesquisaMesasService {
    
    public List<Comandas> listarComandas(String dataInicial,String dataFinal);

    public List<Comandas> pesquisarComandaPorCodigo(String codigo,String dataInicial,String dataFinal);
}
