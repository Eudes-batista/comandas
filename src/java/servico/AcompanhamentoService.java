/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servico;

import java.util.List;
import modelo.Acompanhamento;

/**
 *
 * @author Administrador
 */
public interface AcompanhamentoService {

    public void salvar(Acompanhamento acompanhamento);

    public void excluir(Acompanhamento acompanhamento);

    public List<Acompanhamento> pesquisar(String nome);

    public List<Acompanhamento> pesquisarPorGrupo(String grupo);

    public List<Acompanhamento> pesquisarTodos();

    public int verificarId();
}
