/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servico;

import java.util.List;
import modelo.GrupoAcompanhamento;

/**
 *
 * @author Administrador
 */
public interface GrupoAcompanhamentoService {

    public void salvar(GrupoAcompanhamento grupoAcompanhamento);

    public void excluir(GrupoAcompanhamento grupoAcompanhamento);

    public List<GrupoAcompanhamento> pesquisar(String nome);

    public List<GrupoAcompanhamento> pesquisarTodos();
    
    public int verificarId();
    
}
