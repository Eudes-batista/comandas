/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servico;

import java.util.List;
import modelo.Produto;

/**
 *
 * @author Administrador
 */
public interface ProdutoService {
    public List<Produto> lsitarProdutos();
    public List<Produto> lsitarProdutoPorGrupo(String grupo);
    public List<Produto> listarPorReferenciaDescricaoCodigoBarra(String pesquisa);
    public Produto buscarProduto(String referencia);
    
}
