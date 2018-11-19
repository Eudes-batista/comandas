package servico;

import java.util.List;

public interface VendedorService {
    public List<Object[]> listarVendedor();
    public String validarVendedor(String senha);
}
