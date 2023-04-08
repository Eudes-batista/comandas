package servico;

import java.util.List;
import modelo.Produto;

public interface ProdutoService {
    public List<Produto> lsitarProdutos();
    public List<Produto> lsitarProdutoPorGrupo(String grupo);
    public List<Produto> listarPorReferenciaDescricaoCodigoBarra(String pesquisa);
    public Produto buscarProduto(String referencia);   
    
    public List<Produto> lsitarProdutoPorGrupoFastFood(String grupo);
    public List<Produto> listarPorReferenciaDescricaoCodigoBarraFastFood(String pesquisa);
    public List<Produto> lsitarProdutosFastFood();
    public Produto buscarProdutoFastFood(String referencia);   
}
