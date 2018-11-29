package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.dto.VendaGarcom;
import servico.VendaDetalheService;

@ManagedBean(name = "vendaDetalheBean")
@ViewScoped
@Getter
@Setter
public class VendaDetalheBean implements Serializable{
    
    @ManagedProperty(value = "#{vendaDetalheService}")
    private VendaDetalheService vendaDetalheService;
    
    private List<VendaGarcom> vendasGarcom;
    
    private String dataInicial;
    
    private String dataFinal;
    
    public void init(){
        listarVendasGarcom();
    }
    
    private void listarVendasGarcom() {
        this.vendasGarcom = this.vendaDetalheService.listarVendaGarcom();
        
    }
    
    public int getQuantidadeRejeicaoDezPorcento(String vendedor) {
        if(dataInicial == null && dataFinal == null){
            dataInicial = LocalDate.now().toString();
            dataFinal   = LocalDate.now().toString();
        }    
        return vendaDetalheService.listarReijeicaoPorcentagemPorVendedor(vendedor, dataInicial, dataFinal).size();
    }
    
    
}
