package servico;

import java.util.List;
import modelo.Vendedor;

public interface VendedorService {
    public List<Vendedor> listarVendedor();
    public String validarVendedor(String senha);
}
