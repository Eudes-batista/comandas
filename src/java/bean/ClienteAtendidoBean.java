package bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.dto.ClienteAtendidoPorMes;
import modelo.dto.FiltroVendaDetalhe;
import org.omnifaces.util.Messages;
import servico.ClienteAtendidoService;

@ManagedBean(name = "clienteAtendidoBean")
@ViewScoped
@Getter @Setter
public class ClienteAtendidoBean implements Serializable{
    
    @ManagedProperty(value="#{clienteAtendidoService}")
    private ClienteAtendidoService clienteAtendidoService;
    
    private List<ClienteAtendidoPorMes> clienteAtendidos;
    
    private FiltroVendaDetalhe filtroVendaDetalhe;
    

    public void init() {
        filtroVendaDetalhe = new FiltroVendaDetalhe();
        LocalDate date = LocalDate.now();
        filtroVendaDetalhe.setDataInicial(date.getYear()+"-01-01");
        filtroVendaDetalhe.setDataFinal(date.getYear()+"-12-31");
    }
    
    public void listarClientesAtendidos() {
        try {
            clienteAtendidos = clienteAtendidoService.clienteAtendidos(filtroVendaDetalhe);
        } catch (SQLException ex) {
            Messages.addGlobalError("Erro na Execuçao do sql. "+ex.getMessage());
        }
    }
    
    
    public String getQuantidadePessoas(){
        listarClientesAtendidos();
        return  clienteAtendidos != null?clienteAtendidos.toString():"";
    }
    
}
